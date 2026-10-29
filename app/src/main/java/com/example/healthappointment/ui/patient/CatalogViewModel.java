package com.example.healthappointment.ui.patient;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.healthappointment.data.local.entity.Department;
import com.example.healthappointment.data.local.entity.Doctor;
import com.example.healthappointment.data.repository.DepartmentRepository;
import com.example.healthappointment.data.repository.DoctorRepository;

import java.util.List;

/** ViewModel cho các màn hình xem danh mục (khoa, bác sĩ). */
public class CatalogViewModel extends AndroidViewModel {

    private final DoctorRepository doctorRepository;
    private final LiveData<List<Department>> departments;
    private final MutableLiveData<Integer> departmentId = new MutableLiveData<>();
    private final LiveData<List<Doctor>> doctors;

    public CatalogViewModel(@NonNull Application application) {
        super(application);
        DepartmentRepository departmentRepository = new DepartmentRepository(application);
        doctorRepository = new DoctorRepository(application);
        departments = departmentRepository.getDepartments();
        // Mỗi khi departmentId đổi, tự truy vấn lại danh sách bác sĩ
        doctors = Transformations.switchMap(departmentId, doctorRepository::getByDepartment);
    }

    public LiveData<List<Department>> getDepartments() {
        return departments;
    }

    public void setDepartmentId(int id) {
        if (departmentId.getValue() == null || departmentId.getValue() != id) {
            departmentId.setValue(id);
        }
    }

    public LiveData<List<Doctor>> getDoctors() {
        return doctors;
    }
}
