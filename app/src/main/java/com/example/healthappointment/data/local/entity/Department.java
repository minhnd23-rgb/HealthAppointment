package com.example.healthappointment.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "departments")
public class Department {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull public String name;
    public String description;

    public Department(@NonNull String name, String description) {
        this.name = name;
        this.description = description;
    }
}
