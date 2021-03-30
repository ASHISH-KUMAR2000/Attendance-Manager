package com.ashish.attendancemanager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.ashish.attendancemanager.model.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.Arrays;

public class AdminStudentsActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private EditText studentIdEditText, courseIdEditText;
    private String studentId, courseId, currYear;
    private Button enrollButton, unenrollButton;


    @RequiresApi(api = Build.VERSION_CODES.O)
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

        LocalDate currentDate = LocalDate.now();
        int y = currentDate.getYear();
        currYear = Integer.toString(y);

    }

    private void AddEnrolledCourse() {

        studentId = studentIdEditText.getText().toString().trim();
        courseId = courseIdEditText.getText().toString().trim();

        if(!TextUtils.isEmpty(studentId)&&
        !TextUtils.isEmpty(courseId)) {
            addToStudentCourseEnrolled(studentId, courseId);
            addToCourseEnrolledDatabase(studentId, courseId);
        } else{
            Toast.makeText(AdminStudentsActivity.this,
                    "Empty fields not allowed.",
                    Toast.LENGTH_SHORT).show();
        }


    }

    private void addToCourseEnrolledDatabase(final String studentId, final String courseId) {
        mDatabase.child("CourseEnrolled").child(courseId).child(currYear)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            String courseEnrolled = snapshot.getValue(String.class);
                            if (!TextUtils.isEmpty(courseEnrolled)) {
                                courseEnrolled += ",";
                            }
                            courseEnrolled += studentId;
                            courseEnrolled = sortStudents(courseEnrolled);
                            mDatabase.child("CourseEnrolled").child(courseId).child(currYear)
                                    .setValue(courseEnrolled);
                        } else{
                            mDatabase.child("CourseEnrolled").child(courseId).child(currYear)
                                    .setValue(studentId);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private String sortStudents(String courseEnrolled) {

        String[] students = courseEnrolled.split(",");
        Arrays.sort(students);

        String res="";
        for(String str: students){
            res+=str;
            res+=',';
        }

        res = res.substring(0, res.length()-1);
        return res;
    }

    private void addToStudentCourseEnrolled(final String studentId, final String courseId) {
        mDatabase.child("Student").child(studentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Student student = snapshot.getValue(Student.class);

                    if (student != null) {
                        //Log.d("Size", String.valueOf(student.getCourseEnrolled().size()));
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}