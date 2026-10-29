package com.example.healthappointment.ui.patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.healthappointment.R;
import com.example.healthappointment.data.local.entity.Doctor;
import com.example.healthappointment.data.local.entity.Schedule;
import com.example.healthappointment.databinding.FragmentDoctorDetailBinding;
import com.example.healthappointment.databinding.ItemScheduleBinding;
import com.example.healthappointment.util.DayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DoctorDetailFragment extends Fragment {

    private FragmentDoctorDetailBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDoctorDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int doctorId = requireArguments().getInt("doctorId");

        binding.btnBack.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack());
        binding.btnBook.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putInt("doctorId", doctorId);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_doctorDetail_to_booking, b);
        });

        CatalogViewModel viewModel =
                new ViewModelProvider(requireActivity()).get(CatalogViewModel.class);
        viewModel.setDoctorId(doctorId);

        viewModel.getDoctor().observe(getViewLifecycleOwner(), this::renderDoctor);
        viewModel.getSchedules().observe(getViewLifecycleOwner(), this::renderSchedules);
    }

    private void renderDoctor(Doctor d) {
        if (d == null) return;
        binding.tvName.setText(d.degree + " " + d.fullName);
        binding.tvSpecialization.setText("Chuyên môn: " + d.specialization);
        binding.tvExperience.setText("Kinh nghiệm: " + d.experienceYears + " năm");
        binding.tvPhone.setText("Điện thoại: " + d.phone);
        binding.tvDescription.setText(d.description);
    }

    private void renderSchedules(List<Schedule> list) {
        binding.scheduleContainer.removeAllViews();
        boolean empty = list == null || list.isEmpty();
        binding.tvNoSchedule.setVisibility(empty ? View.VISIBLE : View.GONE);
        if (empty) return;

        TreeMap<Integer, List<Schedule>> byDay = new TreeMap<>();
        for (Schedule s : list) {
            List<Schedule> shifts = byDay.get(s.dayOfWeek);
            if (shifts == null) {
                shifts = new ArrayList<>();
                byDay.put(s.dayOfWeek, shifts);
            }
            shifts.add(s);
        }

        for (Map.Entry<Integer, List<Schedule>> e : byDay.entrySet()) {
            List<Schedule> shifts = e.getValue();
            Collections.sort(shifts, (a, b) -> a.startTime.compareTo(b.startTime));

            List<String> parts = new ArrayList<>();
            for (Schedule s : shifts) {
                parts.add(s.startTime + " - " + s.endTime);
            }

            ItemScheduleBinding row = ItemScheduleBinding.inflate(
                    getLayoutInflater(), binding.scheduleContainer, false);
            row.tvDay.setText(DayUtils.dayName(e.getKey()));
            row.tvTime.setText(String.join("   •   ", parts));
            binding.scheduleContainer.addView(row.getRoot());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
