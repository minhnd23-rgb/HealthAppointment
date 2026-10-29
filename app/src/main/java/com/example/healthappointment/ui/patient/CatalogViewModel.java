package com.example.healthappointment.ui.patient;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.healthappointment.data.local.entity.Department;
import com.example.healthappointment.data.local.entity.Doctor;
import com.example.healthappointment.data.local.entity.Schedule;
import com.example.healthappointment.data.repository.DepartmentRepository;
import com.example.healthappointment.data.repository.DoctorRepository;
import com.example.healthappointment.data.repository.ScheduleRepository;

import java.util.List;

/** ViewModel cho các màn hình xem danh mục: khoa, bác sĩ, chi tiết bác sĩ, lịch làm việc. */
public class CatalogViewModel extends AndroidViewModel {

    private final LiveData<List<Department>> departments;

    private final MutableLiveData<Integer> departmentId = new MutableLiveData<>();
    private final LiveData<List<Doctor>> doctors;

    private final MutableLiveData<Integer> doctorId = new MutableLiveData<>();
    private final LiveData<Doctor> doctor;
    private final LiveData<List<Schedule>> schedules;

    public CatalogViewModel(@NonNull Application application) {
        super(application);
        DepartmentRepository departmentRepository = new DepartmentRepository(application);
        DoctorRepository doctorRepository = new DoctorRepository(application);
        ScheduleRepository scheduleRepository = new ScheduleRepository(application);

        departments = departmentRepository.getDepartments();
        doctors = Transformations.switchMap(departmentId, doctorRepository::getByDepartment);
        doctor = Transformations.switchMap(doctorId, doctorRepository::observeById);
        schedules = Transformations.switchMap(doctorId, scheduleRepository::getByDoctor);
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

    public void setDoctorId(int id) {
        if (doctorId.getValue() == null || doctorId.getValue() != id) {
            doctorId.setValue(id);
        }
    }

    public LiveData<Doctor> getDoctor() {
        return doctor;
    }

    public LiveData<List<Schedule>> getSchedules() {
        return schedules;
    }
}
