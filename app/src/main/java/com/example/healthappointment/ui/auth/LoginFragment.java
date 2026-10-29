package com.example.healthappointment.ui.auth;

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

import com.example.healthappointment.databinding.FragmentLoginBinding;
import com.example.healthappointment.util.Validators;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private AuthViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        binding.btnLogin.setOnClickListener(v -> attemptLogin());
        binding.btnGoRegister.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(com.example.healthappointment.R.id.action_login_to_register));

        viewModel.getState().observe(getViewLifecycleOwner(), this::render);
    }

    private void attemptLogin() {
        String email = text(binding.etEmail.getText());
        String password = binding.etPassword.getText() == null
                ? "" : binding.etPassword.getText().toString();

        String emailError = Validators.validateEmail(email);
        String passError = password.isEmpty() ? "Vui lòng nhập mật khẩu" : null;
        binding.tilEmail.setError(emailError);
        binding.tilPassword.setError(passError);
        if (emailError != null || passError != null) return;

        viewModel.login(email, password);
    }

    private void render(AuthState state) {
        boolean loading = state.status == AuthState.Status.LOADING;
        binding.progress.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!loading);
        binding.btnGoRegister.setEnabled(!loading);

        switch (state.status) {
            case ERROR:
                binding.tilPassword.setError(state.message);
                viewModel.consumeState();
                break;
            case SUCCESS:
                // Tạm thời: chỉ hiện Toast. Ở bước 3.4 sẽ chuyển sang màn hình Patient/Staff.
                Toast.makeText(requireContext(),
                        "Xin chào " + state.user.fullName + " (" + state.user.role + ")",
                        Toast.LENGTH_LONG).show();
                viewModel.consumeState();
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
