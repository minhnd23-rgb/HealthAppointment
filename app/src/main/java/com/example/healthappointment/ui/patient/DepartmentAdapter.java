package com.example.healthappointment.ui.patient;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappointment.data.local.entity.Department;
import com.example.healthappointment.databinding.ItemDepartmentBinding;

import java.util.Objects;

public class DepartmentAdapter extends ListAdapter<Department, DepartmentAdapter.VH> {

    public interface OnDepartmentClick {
        void onClick(Department department);
    }

    private static final DiffUtil.ItemCallback<Department> DIFF =
            new DiffUtil.ItemCallback<Department>() {
                @Override
                public boolean areItemsTheSame(@NonNull Department a, @NonNull Department b) {
                    return a.id == b.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Department a, @NonNull Department b) {
                    return Objects.equals(a.name, b.name)
                            && Objects.equals(a.description, b.description);
                }
            };

    private final OnDepartmentClick listener;

    public DepartmentAdapter(OnDepartmentClick listener) {
        super(DIFF);
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDepartmentBinding b = ItemDepartmentBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(b);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Department d = getItem(position);
        holder.binding.tvName.setText(d.name);
        holder.binding.tvDescription.setText(d.description);
        holder.binding.getRoot().setOnClickListener(v -> listener.onClick(d));
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemDepartmentBinding binding;

        VH(ItemDepartmentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}