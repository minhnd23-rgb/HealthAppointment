package com.example.healthappointment.data.repository;

import android.content.Context;

import com.example.healthappointment.data.local.AppDatabase;
import com.example.healthappointment.data.local.dao.AppointmentDao;
import com.example.healthappointment.data.local.dao.ReviewDao;
import com.example.healthappointment.data.local.entity.Appointment;
import com.example.healthappointment.data.local.entity.Review;
import com.example.healthappointment.util.StatusUtil;

public class ReviewRepository {

    private final AppointmentDao appointmentDao;
    private final ReviewDao reviewDao;

    public ReviewRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
        this.appointmentDao = db.appointmentDao();
        this.reviewDao = db.reviewDao();
    }

    /** PHẢI gọi trên luồng nền. */
    public boolean hasReviewed(int appointmentId) {
        return reviewDao.findByAppointment(appointmentId) != null;
    }

    /**
     * Gửi đánh giá. PHẢI gọi trên luồng nền.
     * @return null nếu thành công, ngược lại là thông báo lỗi.
     */
    public String submit(int appointmentId, int patientId, int rating, String comment) {
        if (rating < 1 || rating > 5) return "Vui lòng chọn số sao từ 1 đến 5";

        Appointment a = appointmentDao.findById(appointmentId);
        if (a == null) return "Không tìm thấy lịch hẹn";
        if (a.patientId != patientId) return "Bạn không có quyền đánh giá lịch hẹn này";
        if (!StatusUtil.canReview(a.status)) return "Chỉ đánh giá được lịch hẹn đã hoàn thành";
        if (hasReviewed(appointmentId)) return "Bạn đã đánh giá lịch hẹn này rồi";

        String cleanComment = comment == null ? "" : comment.trim();
        try {
            reviewDao.insert(new Review(appointmentId, patientId, a.doctorId,
                    rating, cleanComment, System.currentTimeMillis()));
        } catch (Exception e) {
            return "Không thể lưu đánh giá, vui lòng thử lại";
        }
        return null;
    }
}
