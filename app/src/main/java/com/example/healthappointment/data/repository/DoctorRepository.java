package com.example.healthappointment.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.healthappointment.data.local.AppDatabase;
import com.example.healthappointment.data.local.dao.DoctorDao;
import com.example.healthappointment.data.local.entity.Doctor;

import java.util.List;

public class DoctorRepository {

    private final DoctorDao doctorDao;

    public DoctorRepository(Context context) {
        this.doctorDao = AppDatabase.getInstance(context).doctorDao();
    }

    public LiveData<List<Doctor>> getByDepartment(int departmentId) {
        return doctorDao.getByDepartment(departmentId);
    }

    public LiveData<Doctor> observeById(int doctorId) {
        return doctorDao.observeById(doctorId);
    }
}
