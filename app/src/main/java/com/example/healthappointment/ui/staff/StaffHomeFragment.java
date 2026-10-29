package com.example.healthappointment.ui.staff;

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
import com.example.healthappointment.databinding.FragmentStaffHomeBinding;
import com.example.healthappointment.ui.auth.AuthViewModel;
import com.example.healthappointment.util.SessionManager;

public class StaffHomeFragment extends Fragment {

    private FragmentStaffHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStaffHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SessionManager session = SessionManager.getInstance(requireContext());
        binding.tvWelcome.setText("Xin chào, " + session.getFullName());

        AuthViewModel authViewModel =
                new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        binding.btnLogout.setOnClickListener(v -> {
            authViewModel.logout();
            NavHostFragment.findNavController(this).navigate(R.id.action_global_login);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
