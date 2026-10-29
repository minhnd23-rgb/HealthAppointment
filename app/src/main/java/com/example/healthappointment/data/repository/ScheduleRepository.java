package com.example.healthappointment.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.healthappointment.data.local.AppDatabase;
import com.example.healthappointment.data.local.dao.ScheduleDao;
import com.example.healthappointment.data.local.entity.Schedule;

import java.util.List;

public class ScheduleRepository {

    private final ScheduleDao scheduleDao;

    public ScheduleRepository(Context context) {
        this.scheduleDao = AppDatabase.getInstance(context).scheduleDao();
    }

    public LiveData<List<Schedule>> getByDoctor(int doctorId) {
        return scheduleDao.getByDoctor(doctorId);
    }
}
