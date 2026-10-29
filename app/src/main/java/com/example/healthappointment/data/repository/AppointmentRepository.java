package com.example.healthappointment.data.repository;

import android.content.Context;

import com.example.healthappointment.data.local.AppDatabase;
import com.example.healthappointment.data.local.dao.AppointmentDao;
import com.example.healthappointment.data.local.dao.ScheduleDao;
import com.example.healthappointment.data.local.entity.Appointment;
import com.example.healthappointment.data.local.entity.Schedule;
import com.example.healthappointment.util.Constants;
import com.example.healthappointment.util.SlotGenerator;
import com.example.healthappointment.util.SlotItem;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppointmentRepository {

    private final AppointmentDao appointmentDao;
    private final ScheduleDao scheduleDao;

    public AppointmentRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.appointmentDao = db.appointmentDao();
        this.scheduleDao = db.scheduleDao();
    }

    /** Sinh danh sách khung giờ của bác sĩ trong ngày (yyyy-MM-dd). Callback chạy trên luồng nền. */
    public void loadSlots(int doctorId, String date, Callback<List<SlotItem>> callback) {
        AppDatabase.databaseExecutor.execute(() -> {
            try {
                LocalDate d = LocalDate.parse(date);
                int dayOfWeek = d.getDayOfWeek().getValue(); // 1 = Thứ 2 ... 7 = CN

                List<Schedule> shifts = scheduleDao.findByDoctorAndDay(doctorId, dayOfWeek);
                List<String> bookedList = appointmentDao.getBookedSlots(doctorId, date);
                Set<String> booked = new HashSet<>(bookedList);

                int nowMin = -1;
                if (d.equals(LocalDate.now())) {
                    LocalTime now = LocalTime.now();
                    nowMin = now.getHour() * 60 + now.getMinute();
                }
                callback.onSuccess(SlotGenerator.generate(shifts, booked, nowMin));
            } catch (Exception e) {
                callback.onError("Không tải được khung giờ: " + e.getMessage());
            }
        });
    }

    /** Đặt lịch. Trả về id lịch hẹn mới, hoặc lỗi nếu khung giờ vừa bị người khác đặt. */
    public void book(int patientId, int doctorId, String date, String timeSlot,
                     String symptoms, Callback<Long> callback) {
        AppDatabase.databaseExecutor.execute(() -> {
            try {
                long now = System.currentTimeMillis();
                Appointment a = new Appointment(patientId, doctorId, date, timeSlot,
                        symptoms.trim(), Constants.STATUS_PENDING, "", now, now);
                long id = appointmentDao.bookIfAvailable(a);
                if (id == -1) {
                    callback.onError("Khung giờ này vừa có người đặt, vui lòng chọn khung khác");
                } else {
                    callback.onSuccess(id);
                }
            } catch (Exception e) {
                callback.onError("Lỗi đặt lịch: " + e.getMessage());
            }
        });
    }
}
