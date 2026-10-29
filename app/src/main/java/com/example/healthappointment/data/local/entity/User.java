package com.example.healthappointment.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "users", indices = {@Index(value = "email", unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull public String fullName;
    @NonNull public String email;      // luôn lưu chữ thường
    public String phone;
    @NonNull public String passwordHash;
    @NonNull public String salt;
    @NonNull public String role;       // PATIENT hoặc STAFF
    public long createdAt;

    public User(@NonNull String fullName, @NonNull String email, String phone,
                @NonNull String passwordHash, @NonNull String salt,
                @NonNull String role, long createdAt) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.role = role;
        this.createdAt = createdAt;
    }
}
