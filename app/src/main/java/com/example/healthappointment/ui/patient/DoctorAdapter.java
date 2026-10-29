package com.example.healthappointment.ui.patient;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappointment.data.local.entity.Doctor;
import com.example.healthappointment.databinding.ItemDoctorBinding;

import java.util.Objects;

public class DoctorAdapter extends ListAdapter<Doctor, DoctorAdapter.VH> {

    public interface OnDoctorClick {
        void onClick(Doctor doctor);
    }

    private static final DiffUtil.ItemCallback<Doctor> DIFF =
            new DiffUtil.ItemCallback<Doctor>() {
                @Override
                public boolean areItemsTheSame(@NonNull Doctor a, @NonNull Doctor b) {
                    return a.id == b.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Doctor a, @NonNull Doctor b) {
                    return Objects.equals(a.fullName, b.fullName)
                            && Objects.equals(a.degree, b.degree)
                            && Objects.equals(a.specialization, b.specialization)
                            && a.experienceYears == b.experienceYears;
                }
            };

    private final OnDoctorClick listener;

    public DoctorAdapter(OnDoctorClick listener) {
        super(DIFF);
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemDoctorBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Doctor d = getItem(position);
        holder.binding.tvName.setText(d.degree + " " + d.fullName);
        holder.binding.tvSpecialization.setText(d.specialization);
        holder.binding.tvExperience.setText(d.experienceYears + " năm kinh nghiệm");
        holder.binding.getRoot().setOnClickListener(v -> listener.onClick(d));
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemDoctorBinding binding;

        VH(ItemDoctorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}