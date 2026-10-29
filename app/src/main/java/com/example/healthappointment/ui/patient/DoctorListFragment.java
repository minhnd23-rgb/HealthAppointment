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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.healthappointment.databinding.FragmentDoctorListBinding;

public class DoctorListFragment extends Fragment {

    private FragmentDoctorListBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDoctorListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = requireArguments();
        int departmentId = args.getInt("departmentId");
        String departmentName = args.getString("departmentName", "");

        binding.tvTitle.setText("Bác sĩ khoa " + departmentName);
        binding.btnBack.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack());

        CatalogViewModel viewModel =
                new ViewModelProvider(requireActivity()).get(CatalogViewModel.class);
        viewModel.setDepartmentId(departmentId);

        DoctorAdapter adapter = new DoctorAdapter(doctor ->
                // Tạm thời: Toast. Ở 4.3 sẽ mở trang chi tiết bác sĩ.
                Toast.makeText(requireContext(), doctor.fullName, Toast.LENGTH_SHORT).show());
        binding.rvDoctors.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvDoctors.setAdapter(adapter);

        viewModel.getDoctors().observe(getViewLifecycleOwner(), list -> {
            adapter.submitList(list);
            boolean empty = list == null || list.isEmpty();
            binding.tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.rvDoctors.setAdapter(null);
        binding = null;
    }
}