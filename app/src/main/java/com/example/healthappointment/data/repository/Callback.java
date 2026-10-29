package com.example.healthappointment.data.repository;

/** Callback kết quả từ luồng nền. Lưu ý: được gọi trên luồng NỀN, không phải main thread. */
public interface Callback<T> {
    void onSuccess(T data);
    void onError(String message);
}
