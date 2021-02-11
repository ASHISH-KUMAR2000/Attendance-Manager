package com.ashish.attendancemanager.ui;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashish.attendancemanager.R;
import com.ashish.attendancemanager.model.ClassInfo;
import com.ashish.attendancemanager.model.CourseInfo;

import java.util.List;

public class AdminCoursesRecyclerAdapter extends RecyclerView.Adapter<AdminCoursesRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<CourseInfo> courseList;

    public AdminCoursesRecyclerAdapter(Context context, List<CourseInfo> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public AdminCoursesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_course, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCoursesRecyclerAdapter.ViewHolder viewHolder, int position) {
        CourseInfo courseInfo = courseList.get(position);

        viewHolder.courseIdTextView.setText(courseInfo.getCourseId());
        viewHolder.courseNameTextView.setText(courseInfo.getCourseName());
        viewHolder.courseDepartmentTextView.setText(courseInfo.getCourseDepartment());

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView courseIdTextView, courseNameTextView, courseDepartmentTextView;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context = ctx;

            courseIdTextView = itemView.findViewById(R.id.row_courseIdTextView);
            courseNameTextView = itemView.findViewById(R.id.row_courseNameTextView);
            courseDepartmentTextView = itemView.findViewById(R.id.row_courseDepartmentTextView);
        }
    }
    public CourseInfo getAdapterPositionCourseInfo(int position) {
        if (position >= 0 && position<courseList.size()){
            return courseList.get(position);
        }
        return null;
    }
}
