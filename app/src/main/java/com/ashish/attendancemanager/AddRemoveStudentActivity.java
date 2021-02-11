package com.ashish.attendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ashish.attendancemanager.model.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AddRemoveStudentActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText studentNameEditText, studentPasswordEditText, studentIdEditText,
            studentDeptEditText, studentMobileNoEditText, studentEmailEditText;
    private Button addButton, removeButton;
    private String studentName, studentPassword, studentId, studentDept, studentMobileNo, studentEmail;
    private ProgressBar progressBar;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_student);

        studentNameEditText = findViewById(R.id.addRemoveStudent_nameEditView);
        studentPasswordEditText = findViewById(R.id.addRemoveStudent_passwordEditView);
        studentIdEditText = findViewById(R.id.addRemoveStudent_IDEditView);
        studentDeptEditText = findViewById(R.id.addRemoveStudent_deptEditView);
        studentMobileNoEditText = findViewById(R.id.addRemoveStudent_MobEditView);
        studentEmailEditText = findViewById(R.id.addRemoveStudent_EmailEditView);
        progressBar = findViewById(R.id.addRemoveStudent_progressBar);

        addButton = findViewById(R.id.addRemoveStudent_addButton);
        removeButton = findViewById(R.id.addRemoveStudent_removeButton);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        addButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        studentName = studentNameEditText.getText().toString().trim();
        studentPassword = studentPasswordEditText.getText().toString().trim();
        studentId = studentIdEditText.getText().toString().trim();
        studentDept = studentDeptEditText.getText().toString().trim();
        studentMobileNo = studentMobileNoEditText.getText().toString().trim();
        studentEmail = studentEmailEditText.getText().toString().trim();

        switch(view.getId()) {
            case R.id.addRemoveStudent_addButton :
                if (!TextUtils.isEmpty(studentName) && !TextUtils.isEmpty(studentPassword) && !TextUtils.isEmpty(studentId)
                        && !TextUtils.isEmpty(studentDept) && !TextUtils.isEmpty(studentMobileNo) && !TextUtils.isEmpty(studentEmail)) {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(studentId+"@admin.com", studentPassword)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    addStudentToDatabase(studentId, studentName, studentPassword, studentEmail, studentMobileNo, studentDept);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddRemoveStudentActivity.this,
                                            "Something went wrong.\nPlease try again.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else{
                    Toast.makeText(AddRemoveStudentActivity.this,
                            "Please Insert All Empty Fields",
                            Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
                break;

            case R.id.addRemoveStudent_removeButton :
                break;
        }
    }

    private void addStudentToDatabase(String studentId, String studentName, String studentPassword, String studentEmail, String studentMobileNo, String studentDept) {
        ArrayList<String> courseEnrolled = new ArrayList<>();

        Student student = new Student(studentId, studentName, studentPassword, studentEmail, studentMobileNo, studentDept, courseEnrolled);

        mDatabase.child("Student").child(studentId).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddRemoveStudentActivity.this,
                        "Student Added Successfully",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddRemoveStudentActivity.this,
                        "Something went wrong.\nPlease Try again.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}