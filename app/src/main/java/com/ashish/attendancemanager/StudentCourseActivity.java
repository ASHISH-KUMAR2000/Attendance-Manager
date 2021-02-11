package com.ashish.attendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ashish.attendancemanager.model.CourseInfo;
import com.ashish.attendancemanager.model.Student;
import com.ashish.attendancemanager.ui.AdminCoursesRecyclerAdapter;
import com.ashish.attendancemanager.ui.RecyclerItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentCourseActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ArrayList<String> courseEnrolledList;

    private List<CourseInfo> courseInfoList;
    private  HashMap<String, Integer> map = new HashMap<>();
    private RecyclerView recyclerView;
    private AdminCoursesRecyclerAdapter studentCoursesRecyclerAdapter;
    private Student student = null;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course);

        recyclerView = findViewById(R.id.studentCourses_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        studentId =intent.getStringExtra("STUDENT_ID");
        Log.d("studentid", studentId);

        mDatabase.child("Student").child(studentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                   Student s = snapshot.getValue(Student.class);
                   if(s!= null) {
                       student = s;
                   }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FloatingActionButton fab = findViewById(R.id.studentCourses_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Goto QR SCANNER
                Intent intent = new Intent(StudentCourseActivity.this,StudentScannerActivity.class);
                intent.putExtra("STUDENT_OBJECT",student);

                startActivity(intent);

            }
        });

        courseEnrolledList = new ArrayList<>();
        courseInfoList = new ArrayList<>();
        getCourseEnrolledOfStudent();



        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(StudentCourseActivity.this,
                new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CourseInfo courseinfo = studentCoursesRecyclerAdapter.getAdapterPositionCourseInfo(position);
                Intent intent = new Intent(StudentCourseActivity.this,StudentCourseAttendance.class);
                intent.putExtra("STUDENT_OBJECT",student);
                intent.putExtra("COURSE_ID",courseinfo.getCourseId());
                intent.putExtra("COURSE_NAME",courseinfo.getCourseName());
                startActivity(intent);

            }
        }));

    }

    private void getCourseEnrolledOfStudent() {

        mDatabase.child("Student").child(studentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Student student = snapshot.getValue(Student.class);

                if(student != null) {
                    courseEnrolledList = student.getCourseEnrolled();

                    for(int i=0; i<courseEnrolledList.size();i++) {
                        map.put(courseEnrolledList.get(i),1);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDatabase.child("CourseInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseInfoList.clear();

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    CourseInfo courseInfo = postSnapshot.getValue(CourseInfo.class);
                    if(map.containsKey(courseInfo.getCourseId())) {
                        courseInfoList.add(courseInfo);
                    }
                }
                studentCoursesRecyclerAdapter = new AdminCoursesRecyclerAdapter(StudentCourseActivity.this,
                        courseInfoList);
                recyclerView.setAdapter(studentCoursesRecyclerAdapter);
                studentCoursesRecyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}