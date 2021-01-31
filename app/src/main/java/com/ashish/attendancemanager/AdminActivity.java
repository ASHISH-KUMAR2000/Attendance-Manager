package com.ashish.attendancemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private Button teacherViewButton, courseViewButton, studentViewButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        teacherViewButton = findViewById(R.id.adminActivity_teacherActivityButton);
        courseViewButton = findViewById(R.id.adminActivity_courseActivity);
        studentViewButton = findViewById(R.id.adminActivity_studentActivityButton);

        teacherViewButton.setOnClickListener(this);
        courseViewButton.setOnClickListener(this);
        studentViewButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.adminActivity_courseActivity:
                //Goto Admin Courses Activity
                startActivity(new Intent(AdminActivity.this, AdminCoursesActivity.class));
                break;
            case R.id.adminActivity_teacherActivityButton:
                //Goto Admin Teacher Activity
                break;
            case R.id.adminActivity_studentActivityButton:
                //Goto Admin Student Activity
                break;
        }
    }
}
