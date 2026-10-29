package com.example.healthappointment.data.repository;

import android.content.Context;

import com.example.healthappointment.data.local.AppDatabase;
import com.example.healthappointment.data.local.dao.UserDao;
import com.example.healthappointment.data.local.entity.User;
import com.example.healthappointment.util.Constants;
import com.example.healthappointment.util.HashUtil;

public class UserRepository {

    private final UserDao userDao;

    public UserRepository(Context context) {
        this.userDao = AppDatabase.getInstance(context).userDao();
    }

    /** Đăng ký tài khoản bệnh nhân (PATIENT). Nhân viên chỉ có sẵn từ dữ liệu mẫu. */
    public void register(String fullName, String email, String phone,
                         String password, Callback<User> callback) {
        AppDatabase.databaseExecutor.execute(() -> {
            try {
                String normalizedEmail = email.trim().toLowerCase();
                if (userDao.findByEmail(normalizedEmail) != null) {
                    callback.onError("Email này đã được đăng ký");
                    return;
                }
                String salt = HashUtil.generateSalt();
                String hash = HashUtil.hash(password, salt);
                User user = new User(fullName.trim(), normalizedEmail, phone.trim(),
                        hash, salt, Constants.ROLE_PATIENT, System.currentTimeMillis());
                userDao.insert(user);

                // Đọc lại để có id do Room tự sinh
                User saved = userDao.findByEmail(normalizedEmail);
                if (saved == null) {
                    callback.onError("Không thể tạo tài khoản, vui lòng thử lại");
                    return;
                }
                callback.onSuccess(saved);
            } catch (Exception e) {
                callback.onError("Lỗi đăng ký: " + e.getMessage());
            }
        });
    }

    /** Đăng nhập. Sai email hoặc sai mật khẩu đều báo cùng một thông điệp (tránh lộ email nào tồn tại). */
    public void login(String email, String password, Callback<User> callback) {
        AppDatabase.databaseExecutor.execute(() -> {
            try {
                User user = userDao.findByEmail(email.trim().toLowerCase());
                if (user == null || !HashUtil.verify(password, user.salt, user.passwordHash)) {
                    callback.onError("Email hoặc mật khẩu không đúng");
                    return;
                }
                callback.onSuccess(user);
            } catch (Exception e) {
                callback.onError("Lỗi đăng nhập: " + e.getMessage());
            }
        });
    }
}
