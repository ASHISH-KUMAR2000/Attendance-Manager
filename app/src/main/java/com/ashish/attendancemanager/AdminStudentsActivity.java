package com.ashish.attendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ashish.attendancemanager.model.Student;
import com.ashish.attendancemanager.model.Teacher;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminStudentsActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private EditText studentIdEditText, courseIdEditText;
    private String studentId, courseId;
    private Button enrollButton, unenrollButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_students);

        studentIdEditText = findViewById(R.id.studentIdEditText);
        courseIdEditText = findViewById(R.id.courseIdEditText);
        enrollButton = findViewById(R.id.enrollButton);
        unenrollButton = findViewById(R.id.unenrollButton);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FloatingActionButton student_fab = findViewById(R.id.adminTeachers_fab);

        student_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminStudentsActivity.this,
                        AddRemoveStudentActivity.class));
            }
        });

        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEnrolledCourse();
            }
        });

    }

    private void AddEnrolledCourse() {
        studentId = studentIdEditText.getText().toString().trim();
        courseId = courseIdEditText.getText().toString().trim();

        mDatabase.child("Student").child(studentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Student student = snapshot.getValue(Student.class);

                if(student !=null) {
                    Log.d("Size", String.valueOf(student.getCourseEnrolled().size()));
                    student.addToCourseEnrolled(courseId);

                    mDatabase.child("Student").child(studentId).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AdminStudentsActivity.this,
                                    "Enrolled Successfully",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminStudentsActivity.this,
                                    "Something went wrong.\nPlease Try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}