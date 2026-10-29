package com.example.healthappointment.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.healthappointment.data.local.dao.AppointmentDao;
import com.example.healthappointment.data.local.dao.DepartmentDao;
import com.example.healthappointment.data.local.dao.DoctorDao;
import com.example.healthappointment.data.local.dao.ReviewDao;
import com.example.healthappointment.data.local.dao.ScheduleDao;
import com.example.healthappointment.data.local.dao.UserDao;
import com.example.healthappointment.data.local.entity.Appointment;
import com.example.healthappointment.data.local.entity.Department;
import com.example.healthappointment.data.local.entity.Doctor;
import com.example.healthappointment.data.local.entity.Review;
import com.example.healthappointment.data.local.entity.Schedule;
import com.example.healthappointment.data.local.entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {
                User.class, Department.class, Doctor.class,
                Schedule.class, Appointment.class, Review.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract DepartmentDao departmentDao();
    public abstract DoctorDao doctorDao();
    public abstract ScheduleDao scheduleDao();
    public abstract AppointmentDao appointmentDao();
    public abstract ReviewDao reviewDao();

    private static volatile AppDatabase INSTANCE;

    // Luồng nền cho mọi thao tác ghi/đọc đồng bộ (Room không cho chạy trên main thread)
    public static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(4);

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "health_appointment.db")
                            .addCallback(seedCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Chạy đúng 1 lần, khi file database được tạo lần đầu
    private static final RoomDatabase.Callback seedCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseExecutor.execute(() -> SeedData.populate(INSTANCE));
        }
    };
}
