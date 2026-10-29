package com.example.healthappointment.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.healthappointment.data.local.entity.Doctor;

import java.util.List;

@Dao
public interface DoctorDao {
    @Insert
    List<Long> insertAll(List<Doctor> doctors);

    @Query("SELECT * FROM doctors ORDER BY fullName")
    LiveData<List<Doctor>> getAll();

    @Query("SELECT * FROM doctors WHERE departmentId = :departmentId ORDER BY fullName")
    LiveData<List<Doctor>> getByDepartment(int departmentId);

    @Query("SELECT * FROM doctors WHERE id = :id")
    LiveData<Doctor> observeById(int id);

    @Query("SELECT * FROM doctors WHERE id = :id")
    Doctor findById(int id);
}
