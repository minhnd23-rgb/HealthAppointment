package com.example.healthappointment.ui.auth;

import com.example.healthappointment.data.local.entity.User;

/** Trạng thái của một lần đăng nhập/đăng ký, để Fragment quan sát. */
public class AuthState {

    public enum Status { IDLE, LOADING, SUCCESS, ERROR }

    public final Status status;
    public final String message;
    public final User user;

    private AuthState(Status status, String message, User user) {
        this.status = status;
        this.message = message;
        this.user = user;
    }

    public static AuthState idle() { return new AuthState(Status.IDLE, null, null); }
    public static AuthState loading() { return new AuthState(Status.LOADING, null, null); }
    public static AuthState success(User user) { return new AuthState(Status.SUCCESS, null, user); }
    public static AuthState error(String message) { return new AuthState(Status.ERROR, message, null); }
}
