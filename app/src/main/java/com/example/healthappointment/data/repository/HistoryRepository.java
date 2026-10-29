package com.example.healthappointment.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.healthappointment.data.local.AppDatabase;
import com.example.healthappointment.data.local.dao.AppointmentDao;
import com.example.healthappointment.data.local.entity.Appointment;
import com.example.healthappointment.data.local.model.AppointmentDetail;
import com.example.healthappointment.util.Constants;
import com.example.healthappointment.util.StatusUtil;

import java.util.List;

public class HistoryRepository {

    private final AppointmentDao dao;

    public HistoryRepository(Context context) {
        this.dao = AppDatabase.getInstance(context.getApplicationContext()).appointmentDao();
    }

    public LiveData<List<AppointmentDetail>> observeByPatient(int patientId) {
        return dao.observeByPatient(patientId);
    }

    public LiveData<AppointmentDetail> observeDetail(int appointmentId) {
        return dao.observeDetailById(appointmentId);
    }

    /**
     * Hủy lịch. PHẢI gọi trên luồng nền (AppDatabase.databaseExecutor).
     * @return null nếu thành công, ngược lại là thông báo lỗi.
     */
    public String cancel(int appointmentId, int patientId) {
        Appointment a = dao.findById(appointmentId);
        if (a == null) return "Không tìm thấy lịch hẹn";
        if (a.patientId != patientId) return "Bạn không có quyền hủy lịch hẹn này";
        if (!StatusUtil.canCancel(a.status)) return "Lịch hẹn này không thể hủy";
        dao.updateStatus(appointmentId, Constants.STATUS_CANCELLED, System.currentTimeMillis());
        return null;
    }
}
