package com.example.healthappointment.ui.patient;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.healthappointment.data.local.AppDatabase;
import com.example.healthappointment.data.local.model.AppointmentDetail;
import com.example.healthappointment.data.repository.HistoryRepository;
import com.example.healthappointment.data.repository.ReviewRepository;
import com.example.healthappointment.util.SessionManager;

public class AppointmentDetailViewModel extends AndroidViewModel {

    private final HistoryRepository repository;
    private final ReviewRepository reviewRepository;
    private final int userId;
    private final MutableLiveData<Integer> appointmentId = new MutableLiveData<>();
    private final LiveData<AppointmentDetail> detail;
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final MutableLiveData<Boolean> reviewed = new MutableLiveData<>(false);

    public AppointmentDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new HistoryRepository(application);
        reviewRepository = new ReviewRepository(application);
        userId = SessionManager.getInstance(application).getUserId();
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

    public LiveData<String> getMessage() {
        return message;
    }

    public void consumeMessage() {
        message.setValue(null);
    }

    /** true nếu lịch hẹn này đã có đánh giá. */
    public LiveData<Boolean> getReviewed() {
        return reviewed;
    }

    public void refreshReviewed(int id) {
        AppDatabase.databaseExecutor.execute(() ->
                reviewed.postValue(reviewRepository.hasReviewed(id)));
    }

    public void cancel(int id) {
        AppDatabase.databaseExecutor.execute(() -> {
            String error = repository.cancel(id, userId);
            message.postValue(error == null ? "Đã hủy lịch hẹn" : error);
        });
    }

    public void submitReview(int id, int rating, String comment) {
        AppDatabase.databaseExecutor.execute(() -> {
            String error = reviewRepository.submit(id, userId, rating, comment);
            if (error == null) {
                reviewed.postValue(true);
                message.postValue("Cảm ơn bạn đã đánh giá");
            } else {
                message.postValue(error);
            }
        });
    }
}