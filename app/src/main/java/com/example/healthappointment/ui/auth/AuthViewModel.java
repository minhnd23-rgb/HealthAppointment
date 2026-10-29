package com.example.healthappointment.ui.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.healthappointment.data.local.entity.User;
import com.example.healthappointment.data.repository.Callback;
import com.example.healthappointment.data.repository.UserRepository;
import com.example.healthappointment.util.SessionManager;

public class AuthViewModel extends AndroidViewModel {

    private final UserRepository repository;
    private final SessionManager session;
    private final MutableLiveData<AuthState> state = new MutableLiveData<>(AuthState.idle());

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        session = SessionManager.getInstance(application);
    }

    public LiveData<AuthState> getState() { return state; }

    public void login(String email, String password) {
        state.setValue(AuthState.loading());
        repository.login(email, password, new Callback<User>() {
            @Override public void onSuccess(User user) { onAuthSuccess(user); }
            @Override public void onError(String message) { state.postValue(AuthState.error(message)); }
        });
    }

    public void register(String fullName, String email, String phone, String password) {
        state.setValue(AuthState.loading());
        repository.register(fullName, email, phone, password, new Callback<User>() {
            @Override public void onSuccess(User user) { onAuthSuccess(user); }
            @Override public void onError(String message) { state.postValue(AuthState.error(message)); }
        });
    }

    /** Lưu phiên rồi báo thành công (đang chạy trên luồng nền nên dùng postValue). */
    private void onAuthSuccess(User user) {
        session.saveSession(user.id, user.role, user.fullName);
        state.postValue(AuthState.success(user));
    }

    /** Fragment gọi sau khi đã xử lý SUCCESS/ERROR, để không bị xử lý lại khi xoay màn hình. */
    public void consumeState() {
        state.setValue(AuthState.idle());
    }

    public void logout() {
        session.clear();
    }
}
