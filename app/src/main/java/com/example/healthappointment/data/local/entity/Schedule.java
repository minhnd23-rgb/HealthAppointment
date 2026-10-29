package com.example.healthappointment.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "schedules",
        foreignKeys = @ForeignKey(
                entity = Doctor.class,
                parentColumns = "id",
                childColumns = "doctorId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("doctorId")}
)
public class Schedule {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int doctorId;
    public int dayOfWeek;                 // 1 = Thứ Hai ... 7 = Chủ Nhật
    @NonNull public String startTime;     // "08:00"
    @NonNull public String endTime;       // "11:30"
    public int slotMinutes;               // độ dài mỗi khung giờ, ví dụ 30

    public Schedule(int doctorId, int dayOfWeek, @NonNull String startTime,
                    @NonNull String endTime, int slotMinutes) {
        this.doctorId = doctorId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotMinutes = slotMinutes;
    }
}