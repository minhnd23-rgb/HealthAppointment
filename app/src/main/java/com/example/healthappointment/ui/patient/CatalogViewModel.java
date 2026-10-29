package com.example.healthappointment.ui.patient;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.healthappointment.data.local.entity.Department;
import com.example.healthappointment.data.repository.DepartmentRepository;

import java.util.List;

/** ViewModel cho các màn hình xem danh mục (khoa, bác sĩ). Ở 4.2 sẽ thêm hàm cho bác sĩ. */
public class CatalogViewModel extends AndroidViewModel {

    private final DepartmentRepository departmentRepository;
    private final LiveData<List<Department>> departments;

    public CatalogViewModel(@NonNull Application application) {
        super(application);
        departmentRepository = new DepartmentRepository(application);
        departments = departmentRepository.getDepartments();
    }

    public LiveData<List<Department>> getDepartments() {
        return departments;
    }
}
