package com.example.healthappointment.ui.patient;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.healthappointment.data.local.entity.Appointment;
import com.example.healthappointment.databinding.DialogReviewBinding;
import com.example.healthappointment.databinding.FragmentAppointmentDetailBinding;
import com.example.healthappointment.util.StatusUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AppointmentDetailFragment extends Fragment {

    private FragmentAppointmentDetailBinding binding;
    private AppointmentDetailViewModel viewModel;
    private int appointmentId = -1;
    private boolean canReviewStatus = false;
    private boolean alreadyReviewed = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAppointmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AppointmentDetailViewModel.class);

        appointmentId = requireArguments().getInt("appointmentId", -1);
        viewModel.start(appointmentId);
        viewModel.refreshReviewed(appointmentId);

        binding.btnCancel.setOnClickListener(v -> confirmCancel());
        binding.btnReview.setOnClickListener(v -> showReviewDialog());

        viewModel.getDetail().observe(getViewLifecycleOwner(), item -> {
            if (item == null) return;
            Appointment a = item.appointment;

            binding.tvDoctor.setText(item.doctorName);
            binding.tvDepartment.setText(item.departmentName);
            binding.tvDateTime.setText(a.timeSlot + "  •  "
                    + StatusUtil.formatDate(a.appointmentDate));
            binding.tvSymptoms.setText(
                    TextUtils.isEmpty(a.symptoms) ? "Không có" : a.symptoms);
            StatusUtil.applyBadge(binding.tvStatus, a.status);

            boolean hasNote = !TextUtils.isEmpty(a.staffNote);
            binding.tvStaffNoteLabel.setVisibility(hasNote ? View.VISIBLE : View.GONE);
            binding.tvStaffNote.setVisibility(hasNote ? View.VISIBLE : View.GONE);
            binding.tvStaffNote.setText(a.staffNote);

            // Chức năng sửa lịch hẹn tạm bỏ, nên luôn ẩn nút Sửa
            binding.btnEdit.setVisibility(View.GONE);
            binding.btnCancel.setVisibility(StatusUtil.canCancel(a.status) ? View.VISIBLE : View.GONE);

            canReviewStatus = StatusUtil.canReview(a.status);
            updateReviewButton();
        });

        viewModel.getReviewed().observe(getViewLifecycleOwner(), reviewed -> {
            alreadyReviewed = Boolean.TRUE.equals(reviewed);
            updateReviewButton();
        });

        viewModel.getMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg == null) return;
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            viewModel.consumeMessage();
        });
    }

    private void updateReviewButton() {
        if (binding == null) return;
        boolean show = canReviewStatus && !alreadyReviewed;
        binding.btnReview.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void confirmCancel() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Hủy lịch hẹn")
                .setMessage("Bạn có chắc muốn hủy lịch hẹn này không?")
                .setNegativeButton("Không", null)
                .setPositiveButton("Hủy lịch", (dialog, which) -> viewModel.cancel(appointmentId))
                .show();
    }

    private void showReviewDialog() {
        DialogReviewBinding d = DialogReviewBinding.inflate(getLayoutInflater());

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Đánh giá bác sĩ")
                .setView(d.getRoot())
                .setNegativeButton("Đóng", null)
                .setPositiveButton("Gửi", null)
                .create();
        dialog.show();

        // Gắn sau show() để khi chưa chọn sao thì hộp thoại không bị đóng
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            int rating = (int) d.ratingBar.getRating();
            if (rating < 1) {
                Toast.makeText(requireContext(), "Vui lòng chọn số sao", Toast.LENGTH_SHORT).show();
                return;
            }
            String comment = d.etComment.getText() == null
                    ? "" : d.etComment.getText().toString();
            viewModel.submitReview(appointmentId, rating, comment);
            dialog.dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}