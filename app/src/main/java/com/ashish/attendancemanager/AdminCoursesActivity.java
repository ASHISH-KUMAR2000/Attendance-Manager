package com.ashish.attendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ashish.attendancemanager.model.CourseInfo;
import com.ashish.attendancemanager.ui.AdminCoursesRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminCoursesActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private List<CourseInfo> courseInfoList;


    private RecyclerView recyclerView;
    private AdminCoursesRecyclerAdapter adminCoursesRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_courses);

        recyclerView = findViewById(R.id.adminCourses_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference();


        FloatingActionButton fab = findViewById(R.id.adminCourses_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Goto Add Course Activity
                startActivity(new Intent(AdminCoursesActivity.this,
                        AddRemoveCourseActivity.class));
            }
        });

        courseInfoList = new ArrayList<>();

        getCourseInfoFromeFireStore();
    }

    private void getCourseInfoFromeFireStore() {
        mDatabase.child("CourseInfo")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        courseInfoList.clear();
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            CourseInfo courseInfo = postSnapshot.getValue(CourseInfo.class);
                            courseInfoList.add(courseInfo);
                        }

                        adminCoursesRecyclerAdapter = new AdminCoursesRecyclerAdapter(AdminCoursesActivity.this,
                                courseInfoList);
                        recyclerView.setAdapter(adminCoursesRecyclerAdapter);
                        adminCoursesRecyclerAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
