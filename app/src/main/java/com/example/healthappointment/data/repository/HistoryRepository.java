package com.example.healthappointment.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.healthappointment.data.local.AppDatabase;
import com.example.healthappointment.data.local.dao.AppointmentDao;
import com.example.healthappointment.data.local.model.AppointmentDetail;

import java.util.List;

public class HistoryRepository {

    private final AppointmentDao dao;

    public HistoryRepository(Context context) {
        this.dao = AppDatabase.getInstance(context.getApplicationContext()).appointmentDao();
    }

    public LiveData<List<AppointmentDetail>> observeByPatient(int patientId) {
        return dao.observeByPatient(patientId);
    }

    public LiveData<AppointmentDetail> observeDetail(int appointmentId) {
        return dao.observeDetailById(appointmentId);
    }
}
