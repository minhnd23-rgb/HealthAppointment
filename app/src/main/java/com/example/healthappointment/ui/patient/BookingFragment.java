package com.example.healthappointment.ui.patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.healthappointment.R;
import com.example.healthappointment.data.local.entity.Doctor;
import com.example.healthappointment.databinding.FragmentBookingBinding;
import com.example.healthappointment.databinding.ItemSlotChipBinding;
import com.example.healthappointment.util.SlotItem;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class BookingFragment extends Fragment {

    private static final DateTimeFormatter DISPLAY =
            DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", Locale.forLanguageTag("vi-VN"));

    private FragmentBookingBinding binding;
    private BookingViewModel viewModel;
    private boolean rebuilding = false; // true khi đang dựng lại các chip, bỏ qua sự kiện chọn

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBookingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int doctorId = requireArguments().getInt("doctorId");

        viewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);
        viewModel.start(doctorId);

        CatalogViewModel catalog =
                new ViewModelProvider(requireActivity()).get(CatalogViewModel.class);
        catalog.setDoctorId(doctorId);
        catalog.getDoctor().observe(getViewLifecycleOwner(), this::renderDoctor);

        binding.btnBack.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack());
        binding.btnPickDate.setOnClickListener(v -> showDatePicker());
        binding.btnConfirm.setOnClickListener(v -> confirm());

        binding.chipGroupSlots.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (rebuilding) return;
            if (checkedIds.isEmpty()) {
                viewModel.selectSlot(null);
            } else {
                Chip chip = group.findViewById(checkedIds.get(0));
                viewModel.selectSlot(chip == null ? null : (String) chip.getTag());
            }
        });

        updateDateLabel();

        viewModel.getSlots().observe(getViewLifecycleOwner(), this::renderSlots);

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            boolean busy = Boolean.TRUE.equals(loading);
            binding.progress.setVisibility(busy ? View.VISIBLE : View.GONE);
            binding.btnConfirm.setEnabled(!busy);
            binding.btnPickDate.setEnabled(!busy);
        });

        viewModel.getBookResult().observe(getViewLifecycleOwner(), result -> {
            if (result == null) return;
            viewModel.consumeBookResult();
            Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show();
            if (result.success) {
                viewModel.resetSelection();
                NavHostFragment.findNavController(this)
                        .popBackStack(R.id.patientHomeFragment, false);
            }
        });
    }

    private void renderDoctor(Doctor d) {
        if (d == null) return;
        binding.tvDoctor.setText("Bác sĩ: " + d.degree + " " + d.fullName);
    }

    private void showDatePicker() {
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        long end = today + 60L * 24 * 60 * 60 * 1000; // cho đặt trước tối đa 60 ngày

        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setStart(today)
                .setEnd(end)
                .setValidator(DateValidatorPointForward.from(today))
                .build();

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Chọn ngày khám")
                .setCalendarConstraints(constraints)
                .setSelection(today)
                .build();

        picker.addOnPositiveButtonClickListener(selection -> {
            LocalDate date = Instant.ofEpochMilli(selection)
                    .atZone(ZoneOffset.UTC).toLocalDate();
            viewModel.selectDate(date.toString()); // yyyy-MM-dd
            updateDateLabel();
        });
        picker.show(getChildFragmentManager(), "datePicker");
    }

    private void updateDateLabel() {
        String date = viewModel.getSelectedDate();
        if (date == null) {
            binding.btnPickDate.setText("Chọn ngày khám");
        } else {
            binding.btnPickDate.setText(LocalDate.parse(date).format(DISPLAY));
        }
    }

    private void renderSlots(List<SlotItem> list) {
        rebuilding = true;
        binding.chipGroupSlots.removeAllViews();

        boolean hasDate = viewModel.getSelectedDate() != null;
        binding.tvSlotsLabel.setVisibility(hasDate ? View.VISIBLE : View.GONE);

        if (list == null) {
            binding.tvNoSlot.setVisibility(View.GONE);
            rebuilding = false;
            return;
        }
        binding.tvNoSlot.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);

        String selected = viewModel.getSelectedSlot();
        for (SlotItem s : list) {
            ItemSlotChipBinding cb = ItemSlotChipBinding.inflate(
                    getLayoutInflater(), binding.chipGroupSlots, false);
            Chip chip = cb.getRoot();
            chip.setId(View.generateViewId());
            chip.setTag(s.time);
            chip.setText(s.time);
            chip.setEnabled(s.available);
            binding.chipGroupSlots.addView(chip);
            if (s.available && s.time.equals(selected)) chip.setChecked(true);
        }
        rebuilding = false;
    }

    private void confirm() {
        String date = viewModel.getSelectedDate();
        String slot = viewModel.getSelectedSlot();
        String symptoms = binding.etSymptoms.getText() == null
                ? "" : binding.etSymptoms.getText().toString().trim();

        if (date == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn ngày khám", Toast.LENGTH_SHORT).show();
            return;
        }
        if (slot == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn khung giờ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (symptoms.isEmpty()) {
            binding.tilSymptoms.setError("Vui lòng mô tả triệu chứng");
            return;
        }
        if (symptoms.length() > 500) {
            binding.tilSymptoms.setError("Tối đa 500 ký tự");
            return;
        }
        binding.tilSymptoms.setError(null);
        viewModel.book(symptoms);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
