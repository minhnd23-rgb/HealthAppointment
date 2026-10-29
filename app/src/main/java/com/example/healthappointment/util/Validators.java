package com.example.healthappointment.util;

import android.util.Patterns;
public final class Validators {

    private Validators() {}

    public static String validateFullName(String name) {
        if (name == null || name.trim().isEmpty()) return "Vui lòng nhập họ tên";
        if (name.trim().length() < 2) return "Họ tên quá ngắn";
        if (name.trim().length() > 60) return "Họ tên quá dài";
        return null;
    }

    public static String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) return "Vui lòng nhập email";
        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) return "Email không hợp lệ";
        return null;
    }

    public static String validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return "Vui lòng nhập số điện thoại";
        // 10 số bắt đầu bằng 0, hoặc +84 theo sau 9 số
        if (!phone.trim().matches("^(0\\d{9}|\\+84\\d{9})$")) return "Số điện thoại không hợp lệ";
        return null;
    }
    public static String validatePassword(String password) {
        if (password == null || password.isEmpty()) return "Vui lòng nhập mật khẩu";
        if (password.length() < 8) return "Mật khẩu tối thiểu 8 ký tự";
        boolean hasLetter = false, hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        if (!hasLetter || !hasDigit) return "Mật khẩu phải có cả chữ và số";
        return null;
    }

    public static String validateConfirmPassword(String password, String confirm) {
        if (confirm == null || confirm.isEmpty()) return "Vui lòng nhập lại mật khẩu";
        if (!confirm.equals(password)) return "Mật khẩu nhập lại không khớp";
        return null;
    }
}
