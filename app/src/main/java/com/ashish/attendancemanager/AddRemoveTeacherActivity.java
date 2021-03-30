package com.ashish.attendancemanager;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ashish.attendancemanager.model.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddRemoveTeacherActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddRemoveTeacherActivity";
    private EditText teacherNameEditText, teacherPasswordEditText, teacherDesignationEditText,
            teacherDeptEditText, teacherMobileNoEditText, teacherEmailEditText;
    private Button addButton, removeButton;
    private String teacherName, teacherPassword, teacherDesignation, teacherDept, teacherMobileNo, teacherEmail;
    private String teacherId;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_teacher);

        teacherNameEditText = findViewById(R.id.addRemoveTeacher_nameEditView);
        teacherPasswordEditText = findViewById(R.id.addRemoveTeacher_passwordEditView);
        teacherDesignationEditText = findViewById(R.id.addRemoveTeacher_designationEditView);
        teacherDeptEditText = findViewById(R.id.addRemoveTeacher_deptEditView);
        teacherMobileNoEditText = findViewById(R.id.addRemoveTeacher_MobEditView);
        teacherEmailEditText = findViewById(R.id.addRemoveTeacher_EmailEditView);
        progressBar = findViewById(R.id.addRemoveTeacher_progressBar);

        addButton = findViewById(R.id.addRemoveTeacher_addButton);
        removeButton = findViewById(R.id.addRemoveTeacher_removeButton);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        addButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        teacherName = teacherNameEditText.getText().toString().trim();
        teacherPassword = teacherPasswordEditText.getText().toString().trim();
        teacherDesignation = teacherDesignationEditText.getText().toString().trim();
        teacherDept = teacherDeptEditText.getText().toString().trim();
        teacherMobileNo = teacherMobileNoEditText.getText().toString().trim();
        teacherEmail = teacherEmailEditText.getText().toString().trim();

        switch(view.getId()) {
            case R.id.addRemoveTeacher_addButton :
                if(!TextUtils.isEmpty(teacherName)&& !TextUtils.isEmpty(teacherPassword) &&
                        !TextUtils.isEmpty(teacherDesignation) && !TextUtils.isEmpty(teacherDept)&&
                        !TextUtils.isEmpty(teacherMobileNo)&& !TextUtils.isEmpty(teacherEmail)) {

                    final String teacherId = teacherMobileNo;
                    progressBar.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(teacherId+"@admin.com", teacherPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // stop loading screen
                                    String uid = task.getResult().getUser().getUid();
                                    mDatabase.child("UserId").child(uid)
                                            .setValue(teacherId);
                                    Log.d(TAG, uid);

                                    addTeacherToDatabase(teacherId, teacherName, teacherPassword,
                                            teacherDept, teacherDesignation, teacherMobileNo,
                                            teacherEmail);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddRemoveTeacherActivity.this,
                                            "Something went wrong.\nPlease Try again.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else{
                    Toast.makeText(AddRemoveTeacherActivity.this,
                            "Please Insert All Empty Fields",
                            Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
                break;

            case R.id.addRemoveTeacher_removeButton :
                break;
        }
    }

    private void addTeacherToDatabase(String teacherId, String teacherName, String teacherPassword, String teacherDept, String teacherDesignation, String teacherMobileNo, String teacherEmail) {
        Teacher teacher = new Teacher(teacherId,teacherName,teacherPassword,teacherEmail,teacherMobileNo,teacherDept,teacherDesignation);

        mDatabase.child("Teacher").child(teacherId).setValue(teacher).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddRemoveTeacherActivity.this,
                        "Teacher Added Successfully",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddRemoveTeacherActivity.this,
                        "Something went wrong.\nPlease Try again.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}