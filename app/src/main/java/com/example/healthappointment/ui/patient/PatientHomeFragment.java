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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.healthappointment.R;
import com.example.healthappointment.databinding.FragmentPatientHomeBinding;
import com.example.healthappointment.ui.auth.AuthViewModel;
import com.example.healthappointment.util.SessionManager;

public class PatientHomeFragment extends Fragment {

    private FragmentPatientHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPatientHomeBinding.inflate(inflater, container, false);
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

        CatalogViewModel catalogViewModel =
                new ViewModelProvider(requireActivity()).get(CatalogViewModel.class);

        DepartmentAdapter adapter = new DepartmentAdapter(department -> {
            Bundle args = new Bundle();
            args.putInt("departmentId", department.id);
            args.putString("departmentName", department.name);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_patientHome_to_doctorList, args);
        });
        binding.rvDepartments.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvDepartments.setAdapter(adapter);

        catalogViewModel.getDepartments().observe(getViewLifecycleOwner(), adapter::submitList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.rvDepartments.setAdapter(null);
        binding = null;
    }
}
