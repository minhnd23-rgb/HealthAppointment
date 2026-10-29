package com.example.healthappointment.data.local;

import com.example.healthappointment.data.local.entity.Department;
import com.example.healthappointment.data.local.entity.Doctor;
import com.example.healthappointment.data.local.entity.Schedule;
import com.example.healthappointment.data.local.entity.User;
import com.example.healthappointment.util.Constants;
import com.example.healthappointment.util.HashUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SeedData {
    private SeedData() {}

    public static void populate(AppDatabase db) {
        db.runInTransaction(() -> {
            long now = System.currentTimeMillis();

            // ---- Tài khoản mẫu ----
            String saltStaff = HashUtil.generateSalt();
            db.userDao().insert(new User(
                    "Nhân viên Y tế", "staff@clinic.com", "0900000001",
                    HashUtil.hash("Staff@123", saltStaff), saltStaff,
                    Constants.ROLE_STAFF, now));

            String saltPatient = HashUtil.generateSalt();
            db.userDao().insert(new User(
                    "Nguyễn Văn An", "patient@demo.com", "0900000002",
                    HashUtil.hash("Patient@123", saltPatient), saltPatient,
                    Constants.ROLE_PATIENT, now));

            // ---- Khoa ----
            List<Department> departments = Arrays.asList(
                    new Department("Nội tổng quát", "Khám và điều trị các bệnh nội khoa thường gặp"),
                    new Department("Tim mạch", "Chẩn đoán và điều trị bệnh lý tim mạch, huyết áp"),
                    new Department("Nhi khoa", "Chăm sóc sức khỏe trẻ em từ sơ sinh đến 15 tuổi"),
                    new Department("Da liễu", "Điều trị các bệnh về da, tóc, móng"),
                    new Department("Tai Mũi Họng", "Khám và điều trị bệnh tai, mũi, họng")
            );
            List<Long> depIds = db.departmentDao().insertAll(departments);

            // ---- Bác sĩ (mỗi khoa 2 người) ----
            List<Doctor> doctors = new ArrayList<>();
            doctors.add(new Doctor(depIds.get(0).intValue(), "BS. Trần Minh Khoa", "Bác sĩ CKI",
                    "Nội tổng quát", 12, "Hơn 12 năm kinh nghiệm khám nội tổng quát.", "0901000001"));
            doctors.add(new Doctor(depIds.get(0).intValue(), "BS. Lê Thu Hà", "Thạc sĩ, Bác sĩ",
                    "Nội tiết", 9, "Chuyên điều trị tiểu đường và rối loạn nội tiết.", "0901000002"));

            doctors.add(new Doctor(depIds.get(1).intValue(), "PGS.TS. Nguyễn Hoàng Long", "Phó giáo sư, Tiến sĩ",
                    "Tim mạch can thiệp", 20, "Nhiều năm điều trị bệnh mạch vành và suy tim.", "0901000003"));
            doctors.add(new Doctor(depIds.get(1).intValue(), "BS. Phạm Quỳnh Anh", "Bác sĩ CKII",
                    "Tăng huyết áp", 15, "Chuyên theo dõi và điều trị huyết áp cao.", "0901000004"));

            doctors.add(new Doctor(depIds.get(2).intValue(), "BS. Võ Thị Mai", "Bác sĩ CKI",
                    "Nhi hô hấp", 10, "Điều trị viêm phế quản, hen suyễn ở trẻ em.", "0901000005"));
            doctors.add(new Doctor(depIds.get(2).intValue(), "BS. Đặng Quốc Bảo", "Thạc sĩ, Bác sĩ",
                    "Nhi tiêu hóa", 8, "Chuyên các bệnh tiêu hóa và dinh dưỡng trẻ em.", "0901000006"));

            doctors.add(new Doctor(depIds.get(3).intValue(), "BS. Huỳnh Ngọc Trâm", "Bác sĩ CKI",
                    "Da liễu thẩm mỹ", 11, "Điều trị mụn, nám, viêm da cơ địa.", "0901000007"));
            doctors.add(new Doctor(depIds.get(3).intValue(), "BS. Bùi Anh Tuấn", "Thạc sĩ, Bác sĩ",
                    "Da liễu", 7, "Chuyên các bệnh da do nấm, dị ứng.", "0901000008"));

            doctors.add(new Doctor(depIds.get(4).intValue(), "BS. Ngô Thanh Sơn", "Bác sĩ CKII",
                    "Tai Mũi Họng", 16, "Điều trị viêm xoang, viêm họng mạn tính.", "0901000009"));
            doctors.add(new Doctor(depIds.get(4).intValue(), "BS. Đỗ Mỹ Linh", "Bác sĩ CKI",
                    "Tai Mũi Họng nhi", 9, "Khám tai mũi họng cho trẻ em.", "0901000010"));

            List<Long> doctorIds = db.doctorDao().insertAll(doctors);

            // ---- Lịch làm việc ----
            // Bác sĩ thứ tự chẵn: Thứ 2, 4, 6. Bác sĩ thứ tự lẻ: Thứ 3, 5, 7.
            // Mỗi ngày 2 ca: 08:00-11:30 và 13:30-16:30, mỗi khung giờ 30 phút.
            List<Schedule> schedules = new ArrayList<>();
            for (int i = 0; i < doctorIds.size(); i++) {
                int doctorId = doctorIds.get(i).intValue();
                int[] days = (i % 2 == 0) ? new int[]{1, 3, 5} : new int[]{2, 4, 6};
                for (int day : days) {
                    schedules.add(new Schedule(doctorId, day, "08:00", "11:30", 30));
                    schedules.add(new Schedule(doctorId, day, "13:30", "16:30", 30));
                }
            }
            db.scheduleDao().insertAll(schedules);
        });
    }
}
