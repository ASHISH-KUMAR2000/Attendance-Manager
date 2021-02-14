package com.ashish.attendancemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashish.attendancemanager.model.ClassInfo;
import com.ashish.attendancemanager.ui.RecyclerItemClickListener;
import com.ashish.attendancemanager.ui.TeacherRecycleAdapter;
import com.ashish.attendancemanager.ui.UserApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeacherActivity extends AppCompatActivity {

    private static final String TAG = "TeacherActivity";
    ArrayList<ClassInfo> classInfoList;
    private DatabaseReference mDatabase;

    private RecyclerView recyclerView;
    private TeacherRecycleAdapter teacherRecycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);


        recyclerView = findViewById(R.id.teacherActivity_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FloatingActionButton fab = findViewById(R.id.teacherActivity_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherActivity.this, TeacherAddClass.class));
            }
        });

        classInfoList = new ArrayList<>();
        getAllClassesFromDatabase();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(TeacherActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ClassInfo classInfo =teacherRecycleAdapter.getAdapterPositionClassInfo(position);
                Intent intent = new Intent(TeacherActivity.this,
                        TeacherClassAttendance.class);
                intent.putExtra("classCourseName", classInfo.getCourseName());
                intent.putExtra("classCourseId", classInfo.getCourseId());
                intent.putExtra("classCourseYear", classInfo.getCourseYear());
                startActivity(intent);

            }
        }));
    }

    private void getAllClassesFromDatabase() {
        Log.d(TAG, UserApi.getInstance().getUserId());
        Query query = mDatabase.child("TeacherCourse").child(UserApi.getInstance().getUserId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    classInfoList.clear();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot ps : postSnapshot.getChildren()) {
                            ClassInfo classInfo = ps.getValue(ClassInfo.class);
                            classInfoList.add(classInfo);
                        }
                    }
                    teacherRecycleAdapter = new TeacherRecycleAdapter(TeacherActivity.this,
                            classInfoList);
                    recyclerView.setAdapter(teacherRecycleAdapter);
                    teacherRecycleAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}