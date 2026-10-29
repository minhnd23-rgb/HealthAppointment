package com.example.healthappointment.util;

public final class DayUtils {

    private DayUtils() {}

    /** dayOfWeek: 1 = Thứ 2 ... 6 = Thứ 7, 7 = Chủ nhật. */
    public static String dayName(int dayOfWeek) {
        if (dayOfWeek == 7) return "Chủ nhật";
        if (dayOfWeek >= 1 && dayOfWeek <= 6) return "Thứ " + (dayOfWeek + 1);
        return "?";
    }
}
