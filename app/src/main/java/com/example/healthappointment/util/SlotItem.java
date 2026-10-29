package com.example.healthappointment.util;

/** Một khung giờ khám: giờ hiển thị và còn trống hay không. */
public class SlotItem {

    public final String time;       // "HH:mm"
    public final boolean available; // false = đã có người đặt

    public SlotItem(String time, boolean available) {
        this.time = time;
        this.available = available;
    }
}
