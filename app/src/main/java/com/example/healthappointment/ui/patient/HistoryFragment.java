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
import com.example.healthappointment.databinding.FragmentHistoryBinding;
import com.example.healthappointment.ui.common.AppointmentAdapter;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HistoryViewModel viewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        AppointmentAdapter adapter = new AppointmentAdapter(false, item -> {
            Bundle args = new Bundle();
            args.putInt("appointmentId", item.appointment.id);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_history_to_appointmentDetail, args);
        });
        binding.rvHistory.setAdapter(adapter);

        viewModel.getAppointments().observe(getViewLifecycleOwner(), list -> {
            adapter.submitList(list);
            boolean empty = list == null || list.isEmpty();
            binding.tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.rvHistory.setAdapter(null);
        binding = null;
    }
}
