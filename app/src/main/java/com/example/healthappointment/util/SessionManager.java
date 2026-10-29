package com.example.healthappointment.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

/**
 * Lưu phiên đăng nhập (userId, role, tên) bằng EncryptedSharedPreferences.
 * Dùng: SessionManager.getInstance(context)
 */
public class SessionManager {

    private static final String FILE_NAME = "health_session_secure";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ROLE = "role";
    private static final String KEY_FULL_NAME = "full_name";

    private static volatile SessionManager instance;
    private final SharedPreferences prefs;

    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private SessionManager(Context context) {
        SharedPreferences p;
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            p = EncryptedSharedPreferences.create(
                    context,
                    FILE_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (Exception e) {
            // Dự phòng: nếu Keystore lỗi (một số emulator/thiết bị) thì dùng prefs thường
            p = context.getSharedPreferences(FILE_NAME + "_fallback", Context.MODE_PRIVATE);
        }
        prefs = p;
    }

    public void saveSession(int userId, String role, String fullName) {
        prefs.edit()
                .putInt(KEY_USER_ID, userId)
                .putString(KEY_ROLE, role)
                .putString(KEY_FULL_NAME, fullName)
                .apply();
    }

    public boolean isLoggedIn() {
        return getUserId() != -1;
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public String getRole() {
        return prefs.getString(KEY_ROLE, null);
    }

    public String getFullName() {
        return prefs.getString(KEY_FULL_NAME, "");
    }

    public boolean isStaff() {
        return Constants.ROLE_STAFF.equals(getRole());
    }

    public boolean isPatient() {
        return Constants.ROLE_PATIENT.equals(getRole());
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
