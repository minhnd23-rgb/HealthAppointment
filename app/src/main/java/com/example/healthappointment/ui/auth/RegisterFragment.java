package com.example.healthappointment.ui.auth;

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
import com.example.healthappointment.databinding.FragmentRegisterBinding;
import com.example.healthappointment.util.Validators;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private AuthViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        binding.btnRegister.setOnClickListener(v -> attemptRegister());
        binding.btnBackLogin.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack());

        viewModel.getState().observe(getViewLifecycleOwner(), this::render);
    }

    private void attemptRegister() {
        String name = text(binding.etFullName.getText());
        String email = text(binding.etEmail.getText());
        String phone = text(binding.etPhone.getText());
        String password = binding.etPassword.getText() == null
                ? "" : binding.etPassword.getText().toString();
        String confirm = binding.etConfirm.getText() == null
                ? "" : binding.etConfirm.getText().toString();

        String eName = Validators.validateFullName(name);
        String eEmail = Validators.validateEmail(email);
        String ePhone = Validators.validatePhone(phone);
        String ePass = Validators.validatePassword(password);
        String eConfirm = Validators.validateConfirmPassword(password, confirm);

        binding.tilFullName.setError(eName);
        binding.tilEmail.setError(eEmail);
        binding.tilPhone.setError(ePhone);
        binding.tilPassword.setError(ePass);
        binding.tilConfirm.setError(eConfirm);

        if (eName != null || eEmail != null || ePhone != null
                || ePass != null || eConfirm != null) return;

        viewModel.register(name, email, phone, password);
    }

    private void render(AuthState state) {
        boolean loading = state.status == AuthState.Status.LOADING;
        binding.progress.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnRegister.setEnabled(!loading);
        binding.btnBackLogin.setEnabled(!loading);

        switch (state.status) {
            case ERROR:
                binding.tilEmail.setError(state.message);
                viewModel.consumeState();
                break;
            case SUCCESS:
                // Đăng ký xong là đã đăng nhập sẵn (AuthViewModel đã lưu phiên)
                viewModel.consumeState();
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_register_to_patientHome);
                break;
            default:
                break;
        }
    }

    private String text(CharSequence cs) {
        return cs == null ? "" : cs.toString().trim();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
