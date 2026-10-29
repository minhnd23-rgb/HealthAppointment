package com.example.healthappointment.ui.patient;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.healthappointment.data.local.model.AppointmentDetail;
import com.example.healthappointment.data.repository.HistoryRepository;
import com.example.healthappointment.util.SessionManager;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private final LiveData<List<AppointmentDetail>> appointments;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        HistoryRepository repository = new HistoryRepository(application);
        int userId = SessionManager.getInstance(application).getUserId();
        appointments = repository.observeByPatient(userId);
    }

    public LiveData<List<AppointmentDetail>> getAppointments() {
        return appointments;
    }
}