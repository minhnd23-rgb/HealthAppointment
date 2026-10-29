package com.example.healthappointment.util;

import com.example.healthappointment.data.local.entity.Schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class SlotGenerator {

    private SlotGenerator() {}

    /**
     * @param shifts  các ca làm việc của bác sĩ trong đúng thứ đã chọn
     * @param booked  tập giờ đã có lịch ("HH:mm")
     * @param nowMin  số phút từ 00:00 của giờ hiện tại nếu ngày chọn là hôm nay, ngược lại -1
     */
    public static List<SlotItem> generate(List<Schedule> shifts, Set<String> booked, int nowMin) {
        List<SlotItem> result = new ArrayList<>();
        if (shifts == null || shifts.isEmpty()) return result;

        List<Schedule> sorted = new ArrayList<>(shifts);
        Collections.sort(sorted, (a, b) -> a.startTime.compareTo(b.startTime));

        for (Schedule s : sorted) {
            int startMin = toMinutes(s.startTime);
            int endMin = toMinutes(s.endTime);
            int step = s.slotMinutes > 0 ? s.slotMinutes : 30;

            for (int m = startMin; m + step <= endMin; m += step) {
                if (nowMin >= 0 && m <= nowMin) continue; // khung đã qua
                String label = String.format(Locale.US, "%02d:%02d", m / 60, m % 60);
                result.add(new SlotItem(label, !booked.contains(label)));
            }
        }
        return result;
    }

    private static int toMinutes(String hhmm) {
        String[] p = hhmm.split(":");
        return Integer.parseInt(p[0]) * 60 + Integer.parseInt(p[1]);
    }
}
