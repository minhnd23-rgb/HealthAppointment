package com.example.healthappointment.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "reviews",
        foreignKeys = {
                @ForeignKey(entity = Appointment.class, parentColumns = "id",
                        childColumns = "appointmentId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class, parentColumns = "id",
                        childColumns = "patientId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Doctor.class, parentColumns = "id",
                        childColumns = "doctorId", onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = "appointmentId", unique = true),  // mỗi lịch hẹn chỉ đánh giá 1 lần
                @Index("patientId"),
                @Index("doctorId")
        }
)
public class Review {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int appointmentId;
    public int patientId;
    public int doctorId;
    public int rating;          // 1 đến 5
    public String comment;
    public long createdAt;

    public Review(int appointmentId, int patientId, int doctorId,
                  int rating, String comment, long createdAt) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }
}