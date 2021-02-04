package com.ashish.attendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ashish.attendancemanager.model.CourseInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddRemoveCourseActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText courseIdEditView, courseNameEditView, courseDeptEditView;
    private Button addButton, removeButton;
    public String courseId, courseName, courseDept;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_course);

        courseIdEditView = findViewById(R.id.addRemoveCourse_idEditView);
        courseNameEditView = findViewById(R.id.addRemoveCourse_nameEditView);
        courseDeptEditView = findViewById(R.id.addRemoveCourse_deptEditView);

        addButton = findViewById(R.id.addRemoveCourse_addButton);
        removeButton = findViewById(R.id.addRemoveCourse_removeButton);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        addButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        courseId = courseIdEditView.getText().toString().trim();
        courseName = courseNameEditView.getText().toString().trim();
        courseDept = courseDeptEditView.getText().toString().trim();
        switch (v.getId()){
            case R.id.addRemoveCourse_addButton:
                if(!TextUtils.isEmpty(courseId)&&
                !TextUtils.isEmpty(courseName)&&
                !TextUtils.isEmpty(courseDept)) {
                    CourseInfo courseInfo = new CourseInfo(courseId, courseName, courseDept);
                    mDatabase.child("CourseInfo").child(courseId).setValue(courseInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddRemoveCourseActivity.this,
                                    "Course Added Successfully",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddRemoveCourseActivity.this,
                                    "Something went wrong.\nPlease Try again.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else{
                    Toast.makeText(AddRemoveCourseActivity.this,
                            "Empty Field Not Allowed",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.addRemoveCourse_removeButton:
                break;
        }
    }
}
