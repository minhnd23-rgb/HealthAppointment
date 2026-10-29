package com.example.healthappointment.util;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.TextView;

public final class StatusUtil {

    private StatusUtil() {}

    public static String label(String status) {
        if (Constants.STATUS_PENDING.equals(status)) return "Chờ xác nhận";
        if (Constants.STATUS_CONFIRMED.equals(status)) return "Đã xác nhận";
        if (Constants.STATUS_COMPLETED.equals(status)) return "Hoàn thành";
        if (Constants.STATUS_CANCELLED.equals(status)) return "Đã hủy";
        return status == null ? "" : status;
    }

    public static int backgroundColor(String status) {
        if (Constants.STATUS_PENDING.equals(status)) return Color.parseColor("#FFF3E0");
        if (Constants.STATUS_CONFIRMED.equals(status)) return Color.parseColor("#E3F2FD");
        if (Constants.STATUS_COMPLETED.equals(status)) return Color.parseColor("#E8F5E9");
        if (Constants.STATUS_CANCELLED.equals(status)) return Color.parseColor("#FFEBEE");
        return Color.parseColor("#EEEEEE");
    }

    public static int textColor(String status) {
        if (Constants.STATUS_PENDING.equals(status)) return Color.parseColor("#E65100");
        if (Constants.STATUS_CONFIRMED.equals(status)) return Color.parseColor("#1565C0");
        if (Constants.STATUS_COMPLETED.equals(status)) return Color.parseColor("#2E7D32");
        if (Constants.STATUS_CANCELLED.equals(status)) return Color.parseColor("#C62828");
        return Color.parseColor("#424242");
    }

    /** Gán chữ + màu cho TextView dạng badge (nền bo tròn bg_status_badge). */
    public static void applyBadge(TextView view, String status) {
        view.setText(label(status));
        view.setTextColor(textColor(status));
        view.setBackgroundTintList(ColorStateList.valueOf(backgroundColor(status)));
    }

    /** Chỉ hủy được khi đang chờ hoặc đã xác nhận. */
    public static boolean canCancel(String status) {
        return Constants.STATUS_PENDING.equals(status)
                || Constants.STATUS_CONFIRMED.equals(status);
    }

    /** Chỉ sửa được khi đang chờ xác nhận. */
    public static boolean canEdit(String status) {
        return Constants.STATUS_PENDING.equals(status);
    }

    /** Chỉ đánh giá được khi đã hoàn thành (dùng ở Bước 7). */
    public static boolean canReview(String status) {
        return Constants.STATUS_COMPLETED.equals(status);
    }

    /** "2026-11-03" -> "03/11/2026" */
    public static String formatDate(String isoDate) {
        if (isoDate == null) return "";
        String[] p = isoDate.split("-");
        if (p.length != 3) return isoDate;
        return p[2] + "/" + p[1] + "/" + p[0];
    }
}
