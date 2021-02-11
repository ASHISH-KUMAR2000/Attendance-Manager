package com.ashish.attendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ashish.attendancemanager.model.ClassDateInfo;
import com.ashish.attendancemanager.model.CourseInfo;
import com.ashish.attendancemanager.model.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class StudentScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    private Student student= null;
    DatabaseReference mDataBase;
    CourseInfo course;
    ClassDateInfo classDateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        //setContentView(R.layout.activity_student_scanner);
        student =  (Student) getIntent().getSerializableExtra("STUDENT_OBJECT");
        mDataBase = FirebaseDatabase.getInstance().getReference();

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void handleResult(Result rawResult) {
//        Toast.makeText(StudentScannerActivity.this,rawResult.getText().toString().trim(),
//                Toast.LENGTH_SHORT).show();
        String scannedData = rawResult.getText().toString().trim();

        addStudentAttendanceInfo(scannedData);
        Log.d("scannedData", scannedData);
    }



    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    private void addStudentAttendanceInfo(String scannedData) {

        String[] token= scannedData.split(",");
        final String courseid = token[0];
        final String date = token[1];
        final String time = token[2];
        int n = token[1].length();
        final String year = token[1].substring(n-4);



        mDataBase.child("CourseInfo").child(courseid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                   course = snapshot.getValue(CourseInfo.class);
                    Log.d("Data Mining", course.getCourseId());
                   final String dateTime =  date+ " "+time;
                   if(course != null) {
                       mDataBase.child("StudentAttendance").child(student.getUserId()).child(year)
                               .child(course.getCourseId()).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               if(snapshot.exists()) {
                                   ClassDateInfo classDateInfo1 = snapshot.getValue(ClassDateInfo.class);
                                   Log.d("DateList", classDateInfo1.getDateTimeListInfo());
                                   classDateInfo1.setDateTimeListInfo(classDateInfo1.getDateTimeListInfo()+","+dateTime);
                                   classDateInfo = classDateInfo1;
                                   Log.d("DateList2", classDateInfo.getDateTimeListInfo());
                                   addClassAttendanceToFirebase(year,dateTime);
                               }
                               else{
                                   classDateInfo = new ClassDateInfo(course,dateTime);
                                   addClassAttendanceToFirebase(year,dateTime);
                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

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

    private void addClassAttendanceToFirebase(String year, final String dateTime) {

        mDataBase.child("StudentAttendance").child(student.getUserId()).child(year)
                .child(course.getCourseId()).setValue(classDateInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(StudentScannerActivity.this, "Student Attendance Added Successfully",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudentScannerActivity.this, "Something went wrong.\nPlease check " +
                                "your internet connection.",
                        Toast.LENGTH_LONG).show();
            }
        });


    }
}