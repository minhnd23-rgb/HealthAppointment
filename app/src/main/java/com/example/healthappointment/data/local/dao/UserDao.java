package com.example.healthappointment.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.healthappointment.data.local.entity.User;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(User user);

    @Update
    int update(User user);

    @Query("SELECT * FROM users WHERE email = :email COLLATE NOCASE LIMIT 1")
    User findByEmail(String email);

    @Query("SELECT * FROM users WHERE id = :id")
    User findById(int id);

    @Query("SELECT * FROM users WHERE id = :id")
    LiveData<User> observeById(int id);

    @Query("SELECT COUNT(*) FROM users")
    int count();
}
