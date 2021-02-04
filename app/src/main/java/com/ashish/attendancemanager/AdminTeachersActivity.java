package com.ashish.attendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ashish.attendancemanager.model.CourseInfo;
import com.ashish.attendancemanager.model.Teacher;
import com.ashish.attendancemanager.ui.AdminTeachersRecycleAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminTeachersActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private List<Teacher> teacherList;

    private RecyclerView recyclerView;
    private AdminTeachersRecycleAdapter adminTeachersRecycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_teachers);

        recyclerView = findViewById(R.id.adminTeachers_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference();

        FloatingActionButton teacher_fab = findViewById(R.id.adminTeachers_fab);

        teacher_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminTeachersActivity.this,
                        AddRemoveTeacherActivity.class));
            }
        });
        teacherList = new ArrayList<>();

        getTeacherInfoFromFireStore();
    }

    private void getTeacherInfoFromFireStore() {
        mDatabase.child("Teacher").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teacherList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Teacher teacher = postSnapshot.getValue(Teacher.class);
                    teacherList.add(teacher);
                }
                adminTeachersRecycleAdapter = new AdminTeachersRecycleAdapter(AdminTeachersActivity.this,
                        teacherList);
                recyclerView.setAdapter(adminTeachersRecycleAdapter);
                adminTeachersRecycleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}