package com.example.healthappointment.ui.patient;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.healthappointment.data.local.model.ReviewDetail;
import com.example.healthappointment.data.repository.ReviewRepository;

import java.util.List;

public class DoctorReviewViewModel extends AndroidViewModel {

    private final MutableLiveData<Integer> doctorId = new MutableLiveData<>();
    private final LiveData<List<ReviewDetail>> reviews;

    public DoctorReviewViewModel(@NonNull Application application) {
        super(application);
        ReviewRepository repository = new ReviewRepository(application);
        reviews = Transformations.switchMap(doctorId, repository::observeByDoctor);
    }

    public void start(int id) {
        if (doctorId.getValue() == null || doctorId.getValue() != id) {
            doctorId.setValue(id);
        }
    }

    public LiveData<List<ReviewDetail>> getReviews() {
        return reviews;
    }
}
