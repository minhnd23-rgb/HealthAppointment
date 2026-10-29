package com.example.healthappointment.ui.patient;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.healthappointment.data.local.model.AppointmentDetail;
import com.example.healthappointment.data.repository.HistoryRepository;

public class AppointmentDetailViewModel extends AndroidViewModel {

    private final HistoryRepository repository;
    private final MutableLiveData<Integer> appointmentId = new MutableLiveData<>();
    private final LiveData<AppointmentDetail> detail;

    public AppointmentDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new HistoryRepository(application);
        detail = Transformations.switchMap(appointmentId, repository::observeDetail);
    }

    public void start(int id) {
        if (appointmentId.getValue() == null || appointmentId.getValue() != id) {
            appointmentId.setValue(id);
        }
    }

    public LiveData<AppointmentDetail> getDetail() {
        return detail;
    }
}