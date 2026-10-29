package com.example.healthappointment.ui.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappointment.data.local.model.AppointmentDetail;
import com.example.healthappointment.databinding.ItemAppointmentBinding;
import com.example.healthappointment.util.StatusUtil;

import java.util.Objects;

/** Dùng chung cho lịch sử bệnh nhân (Bước 6) và danh sách của nhân viên (Bước 8). */
public class AppointmentAdapter
        extends ListAdapter<AppointmentDetail, AppointmentAdapter.VH> {

    public interface OnItemClick {
        void onClick(AppointmentDetail item);
    }

    private final OnItemClick listener;
    private final boolean showPatientName;

    public AppointmentAdapter(boolean showPatientName, OnItemClick listener) {
        super(DIFF);
        this.showPatientName = showPatientName;
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<AppointmentDetail> DIFF =
            new DiffUtil.ItemCallback<AppointmentDetail>() {
                @Override
                public boolean areItemsTheSame(@NonNull AppointmentDetail a,
                                               @NonNull AppointmentDetail b) {
                    return a.appointment.id == b.appointment.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull AppointmentDetail a,
                                                  @NonNull AppointmentDetail b) {
                    return Objects.equals(a.appointment.status, b.appointment.status)
                            && Objects.equals(a.appointment.appointmentDate, b.appointment.appointmentDate)
                            && Objects.equals(a.appointment.timeSlot, b.appointment.timeSlot)
                            && Objects.equals(a.doctorName, b.doctorName)
                            && Objects.equals(a.departmentName, b.departmentName)
                            && Objects.equals(a.patientName, b.patientName);
                }
            };

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppointmentBinding b = ItemAppointmentBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(b);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(getItem(position));
    }

    class VH extends RecyclerView.ViewHolder {
        private final ItemAppointmentBinding b;

        VH(ItemAppointmentBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        void bind(AppointmentDetail item) {
            b.tvDoctor.setText(item.doctorName);
            b.tvDepartment.setText(item.departmentName);
            b.tvDateTime.setText(item.appointment.timeSlot + "  •  "
                    + StatusUtil.formatDate(item.appointment.appointmentDate));
            StatusUtil.applyBadge(b.tvStatus, item.appointment.status);

            if (showPatientName) {
                b.tvPatient.setVisibility(View.VISIBLE);
                b.tvPatient.setText("Bệnh nhân: " + item.patientName);
            } else {
                b.tvPatient.setVisibility(View.GONE);
            }

            b.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onClick(item);
            });
        }
    }
}
