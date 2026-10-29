package com.example.healthappointment.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.healthappointment.data.local.AppDatabase;
import com.example.healthappointment.data.local.dao.DepartmentDao;
import com.example.healthappointment.data.local.entity.Department;

import java.util.List;

public class DepartmentRepository {

    private final DepartmentDao departmentDao;

    public DepartmentRepository(Context context) {
        this.departmentDao = AppDatabase.getInstance(context).departmentDao();
    }

    /** Room tự chạy truy vấn LiveData trên luồng nền, không cần executor. */
    public LiveData<List<Department>> getDepartments() {
        return departmentDao.getAll();
    }
}
