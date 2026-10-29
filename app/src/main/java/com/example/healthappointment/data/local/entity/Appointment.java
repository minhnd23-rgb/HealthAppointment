package com.example.healthappointment.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "appointments",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "id",
                        childColumns = "patientId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Doctor.class, parentColumns = "id",
                        childColumns = "doctorId", onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index("patientId"),
                @Index(value = {"doctorId", "appointmentDate"})
        }
)
public class Appointment {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int patientId;
    public int doctorId;
    @NonNull public String appointmentDate;   // yyyy-MM-dd
    @NonNull public String timeSlot;          // HH:mm
    public String symptoms;
    @NonNull public String status;            // PENDING, CONFIRMED, COMPLETED, CANCELLED
    public String staffNote;
    public long createdAt;
    public long updatedAt;

    public Appointment(int patientId, int doctorId, @NonNull String appointmentDate,
                       @NonNull String timeSlot, String symptoms, @NonNull String status,
                       String staffNote, long createdAt, long updatedAt) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.timeSlot = timeSlot;
        this.symptoms = symptoms;
        this.status = status;
        this.staffNote = staffNote;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
