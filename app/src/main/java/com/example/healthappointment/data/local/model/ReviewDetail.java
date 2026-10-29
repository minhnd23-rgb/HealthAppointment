package com.example.healthappointment.data.local.model;

import androidx.room.Embedded;

import com.example.healthappointment.data.local.entity.Review;

public class ReviewDetail {
    @Embedded
    public Review review;

    public String patientName;
}