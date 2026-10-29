package com.example.healthappointment.ui.patient;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.healthappointment.data.repository.AppointmentRepository;
import com.example.healthappointment.data.repository.Callback;
import com.example.healthappointment.util.SessionManager;
import com.example.healthappointment.util.SlotItem;

import java.util.List;

public class BookingViewModel extends AndroidViewModel {

    /** Kết quả một lần đặt lịch để Fragment quan sát. */
    public static class BookResult {
        public final boolean success;
        public final String message;

        BookResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }

    private final AppointmentRepository repository;
    private final SessionManager session;

    private final MutableLiveData<List<SlotItem>> slots = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<BookResult> bookResult = new MutableLiveData<>();

    private int doctorId = -1;
    private String selectedDate;
    private String selectedSlot;

    public BookingViewModel(@NonNull Application application) {
        super(application);
        repository = new AppointmentRepository(application);
        session = SessionManager.getInstance(application);
    }

    public LiveData<List<SlotItem>> getSlots() { return slots; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<BookResult> getBookResult() { return bookResult; }

    public String getSelectedDate() { return selectedDate; }
    public String getSelectedSlot() { return selectedSlot; }

    public void selectSlot(String slot) { selectedSlot = slot; }

    /** Gọi khi mở màn hình đặt lịch. Đổi bác sĩ thì xóa lựa chọn cũ. */
    public void start(int newDoctorId) {
        if (doctorId != newDoctorId) {
            doctorId = newDoctorId;
            selectedDate = null;
            selectedSlot = null;
            slots.setValue(null);
        }
    }

    /** Xóa toàn bộ lựa chọn (gọi sau khi đặt thành công). Chạy trên main thread. */
    public void resetSelection() {
        doctorId = -1;
        selectedDate = null;
        selectedSlot = null;
        slots.setValue(null);
    }

    /** Chọn ngày (yyyy-MM-dd) rồi tải khung giờ. */
    public void selectDate(String date) {
        selectedDate = date;
        selectedSlot = null;
        loadSlots();
    }

    public void loadSlots() {
        if (selectedDate == null) return;
        loading.postValue(true);
        repository.loadSlots(doctorId, selectedDate, new Callback<List<SlotItem>>() {
            @Override public void onSuccess(List<SlotItem> data) {
                slots.postValue(data);
                loading.postValue(false);
            }
            @Override public void onError(String message) {
                loading.postValue(false);
                bookResult.postValue(new BookResult(false, message));
            }
        });
    }

    public void book(String symptoms) {
        loading.setValue(true);
        repository.book(session.getUserId(), doctorId, selectedDate, selectedSlot, symptoms,
                new Callback<Long>() {
                    @Override public void onSuccess(Long id) {
                        loading.postValue(false);
                        bookResult.postValue(new BookResult(true, "Đặt lịch thành công"));
                    }
                    @Override public void onError(String message) {
                        loading.postValue(false);
                        bookResult.postValue(new BookResult(false, message));
                        loadSlots(); // làm mới để khung vừa bị đặt hiện khóa
                    }
                });
    }

    public void consumeBookResult() {
        bookResult.setValue(null);
    }
}