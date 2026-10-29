package com.example.healthappointment.ui.patient;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.healthappointment.data.local.entity.Appointment;
import com.example.healthappointment.databinding.FragmentAppointmentDetailBinding;
import com.example.healthappointment.util.StatusUtil;

public class AppointmentDetailFragment extends Fragment {

    private FragmentAppointmentDetailBinding binding;

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

        AppointmentDetailViewModel viewModel =
                new ViewModelProvider(this).get(AppointmentDetailViewModel.class);

        int appointmentId = requireArguments().getInt("appointmentId", -1);
        viewModel.start(appointmentId);

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

            // Các nút sẽ được gắn chức năng ở 6.2, 6.3 và Bước 7
            binding.btnEdit.setVisibility(StatusUtil.canEdit(a.status) ? View.VISIBLE : View.GONE);
            binding.btnCancel.setVisibility(StatusUtil.canCancel(a.status) ? View.VISIBLE : View.GONE);
            binding.btnReview.setVisibility(StatusUtil.canReview(a.status) ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}