package com.example.healthappointment.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.healthappointment.data.local.entity.Appointment;
import com.example.healthappointment.data.local.model.AppointmentDetail;

import java.util.List;

@Dao
public interface AppointmentDao {
    @Insert
    long insert(Appointment appointment);

    @Update
    int update(Appointment appointment);

    @Query("SELECT * FROM appointments WHERE id = :id")
    Appointment findById(int id);

    // Các khung giờ đã có người đặt (không tính lịch đã hủy)
    @Query("SELECT timeSlot FROM appointments " +
            "WHERE doctorId = :doctorId AND appointmentDate = :date AND status != 'CANCELLED'")
    List<String> getBookedSlots(int doctorId, String date);

    // Đặt lịch an toàn: kiểm tra trùng khung giờ và thêm trong cùng 1 transaction.
    // Trả về -1 nếu khung giờ đã bị người khác đặt.
    @Transaction
    default long bookIfAvailable(Appointment appointment) {
        List<String> booked = getBookedSlots(appointment.doctorId, appointment.appointmentDate);
        if (booked.contains(appointment.timeSlot)) {
            return -1;
        }
        return insert(appointment);
    }

    @Query("UPDATE appointments SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    int updateStatus(int id, String status, long updatedAt);

    @Query("SELECT a.*, d.fullName AS doctorName, dep.name AS departmentName, u.fullName AS patientName " +
            "FROM appointments a " +
            "JOIN doctors d ON a.doctorId = d.id " +
            "JOIN departments dep ON d.departmentId = dep.id " +
            "JOIN users u ON a.patientId = u.id " +
            "WHERE a.patientId = :patientId " +
            "ORDER BY a.appointmentDate DESC, a.timeSlot DESC")
    LiveData<List<AppointmentDetail>> observeByPatient(int patientId);

    @Query("SELECT a.*, d.fullName AS doctorName, dep.name AS departmentName, u.fullName AS patientName " +
            "FROM appointments a " +
            "JOIN doctors d ON a.doctorId = d.id " +
            "JOIN departments dep ON d.departmentId = dep.id " +
            "JOIN users u ON a.patientId = u.id " +
            "ORDER BY a.appointmentDate DESC, a.timeSlot DESC")
    LiveData<List<AppointmentDetail>> observeAll();

    @Query("SELECT a.*, d.fullName AS doctorName, dep.name AS departmentName, u.fullName AS patientName " +
            "FROM appointments a " +
            "JOIN doctors d ON a.doctorId = d.id " +
            "JOIN departments dep ON d.departmentId = dep.id " +
            "JOIN users u ON a.patientId = u.id " +
            "WHERE a.status = :status " +
            "ORDER BY a.appointmentDate DESC, a.timeSlot DESC")
    LiveData<List<AppointmentDetail>> observeByStatus(String status);

    @Query("SELECT a.*, d.fullName AS doctorName, dep.name AS departmentName, u.fullName AS patientName " +
            "FROM appointments a " +
            "JOIN doctors d ON a.doctorId = d.id " +
            "JOIN departments dep ON d.departmentId = dep.id " +
            "JOIN users u ON a.patientId = u.id " +
            "WHERE a.id = :id")
    LiveData<AppointmentDetail> observeDetailById(int id);
}
