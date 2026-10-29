package com.example.healthappointment.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.healthappointment.data.local.entity.Schedule;

import java.util.List;

@Dao
public interface ScheduleDao {
    @Insert
    void insertAll(List<Schedule> schedules);

    @Insert
    long insert(Schedule schedule);

    @Update
    int update(Schedule schedule);

    @Delete
    int delete(Schedule schedule);

    @Query("SELECT * FROM schedules WHERE doctorId = :doctorId ORDER BY dayOfWeek, startTime")
    LiveData<List<Schedule>> getByDoctor(int doctorId);

    @Query("SELECT * FROM schedules WHERE doctorId = :doctorId AND dayOfWeek = :dayOfWeek ORDER BY startTime")
    List<Schedule> findByDoctorAndDay(int doctorId, int dayOfWeek);
}
