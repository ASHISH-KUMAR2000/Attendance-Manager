package com.ashish.attendancemanager.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.TextView;

import com.ashish.attendancemanager.R;
import com.ashish.attendancemanager.model.Teacher;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminTeachersRecycleAdapter extends RecyclerView.Adapter<AdminTeachersRecycleAdapter.ViewHolder> {
    private Context context;
    private List<Teacher> teacherList;

    public AdminTeachersRecycleAdapter(Context context, List<Teacher> teacherList) {
        this.context = context;
        this.teacherList = teacherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_teachers, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull  AdminTeachersRecycleAdapter.ViewHolder holder, int position) {
        Teacher teacher = teacherList.get(position);

        holder.teacherNameTextView.setText(teacher.getUserName());
        holder.teacherDesignationTextView.setText(teacher.getDesignation());
        holder.teacherDepartmentTextView.setText(teacher.getDeptName());
        holder.teacherMobileNoTextView.setText(teacher.getUserPhoneNumber());
        holder.teacherEmailTextView.setText(teacher.getUserEmail());
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView teacherNameTextView, teacherDesignationTextView, teacherDepartmentTextView,
                teacherMobileNoTextView,teacherEmailTextView;
        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            teacherNameTextView = itemView.findViewById(R.id.row_teacherNameTextView);
            teacherDesignationTextView = itemView.findViewById(R.id.row_teacherDesignationTextView);
            teacherDepartmentTextView = itemView.findViewById(R.id.row_teacherDeptTextView);
            teacherMobileNoTextView = itemView.findViewById(R.id.row_teacherMobileTextView);
            teacherEmailTextView = itemView.findViewById(R.id.row_teacherEmailTextView);
        }
    }
}
