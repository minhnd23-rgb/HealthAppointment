package com.example.healthappointment.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "doctors",
        foreignKeys = @ForeignKey(
                entity = Department.class,
                parentColumns = "id",
                childColumns = "departmentId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("departmentId")}
)
public class Doctor {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int departmentId;
    @NonNull public String fullName;
    public String degree;            // học vị, ví dụ "Bác sĩ CKI"
    public String specialization;    // chuyên môn
    public int experienceYears;
    public String description;
    public String phone;

    public Doctor(int departmentId, @NonNull String fullName, String degree,
                  String specialization, int experienceYears,
                  String description, String phone) {
        this.departmentId = departmentId;
        this.fullName = fullName;
        this.degree = degree;
        this.specialization = specialization;
        this.experienceYears = experienceYears;
        this.description = description;
        this.phone = phone;
    }
}
