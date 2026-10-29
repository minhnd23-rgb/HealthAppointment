package com.example.healthappointment.data.local.model;

import androidx.room.Embedded;

import com.example.healthappointment.data.local.entity.Appointment;

public class AppointmentDetail {
    @Embedded
    public Appointment appointment;

    public String doctorName;
    public String departmentName;
    public String patientName;
}
