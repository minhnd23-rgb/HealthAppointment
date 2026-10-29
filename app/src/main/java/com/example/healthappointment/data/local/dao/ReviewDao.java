package com.example.healthappointment.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.healthappointment.data.local.entity.Review;
import com.example.healthappointment.data.local.model.ReviewDetail;

import java.util.List;

@Dao
public interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(Review review);

    @Query("SELECT * FROM reviews WHERE appointmentId = :appointmentId LIMIT 1")
    Review findByAppointment(int appointmentId);

    @Query("SELECT r.*, u.fullName AS patientName FROM reviews r " +
            "JOIN users u ON r.patientId = u.id " +
            "WHERE r.doctorId = :doctorId ORDER BY r.createdAt DESC")
    LiveData<List<ReviewDetail>> observeByDoctor(int doctorId);

    @Query("SELECT AVG(rating) FROM reviews WHERE doctorId = :doctorId")
    LiveData<Float> observeAverageRating(int doctorId);

    @Query("SELECT COUNT(*) FROM reviews WHERE doctorId = :doctorId")
    LiveData<Integer> observeReviewCount(int doctorId);
}
