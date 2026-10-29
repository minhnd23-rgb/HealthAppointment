package com.example.healthappointment.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.healthappointment.data.local.entity.Department;

import java.util.List;

@Dao
public interface DepartmentDao {
    @Insert
    List<Long> insertAll(List<Department> departments);

    @Query("SELECT * FROM departments ORDER BY name")
    LiveData<List<Department>> getAll();

    @Query("SELECT * FROM departments WHERE id = :id")
    Department findById(int id);
}