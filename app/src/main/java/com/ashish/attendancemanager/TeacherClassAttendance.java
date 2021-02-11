package com.ashish.attendancemanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class TeacherClassAttendance extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TeacherClassAttendance";
    private TextView courseNameTextView, courseIdTextView;
    private ImageView qrCodeImageView;
    private Button startClassButton;
    private String courseName, courseId, courseYear;
    private TableLayout stk;
    private Boolean firstTimeCalled = false;
    ArrayList<String> studentEnrolledList;
    HashMap<String, Boolean> attendance;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class_attendance);
        Intent intent = getIntent();
        courseName = intent.getStringExtra("classCourseName");
        courseId = intent.getStringExtra("classCourseId");
        courseYear = intent.getStringExtra("classCourseYear");
        courseNameTextView = findViewById(R.id.teacherClassAttendance_courseNametextView);
        courseIdTextView = findViewById(R.id.teacherClassAttendance_courseIdtextView);
        startClassButton = findViewById(R.id.teacherClassAttendance_startClassbutton);
        qrCodeImageView = findViewById(R.id.teacherClassAttendance_qrCodeimageView);
        stk = (TableLayout) findViewById(R.id.teacherClassAttendance_table_main);


        courseIdTextView.setText(courseId + " - " + courseYear);
        courseNameTextView.setText(courseName);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        startClassButton.setOnClickListener(this);

        studentEnrolledList = new ArrayList<>();
        attendance = new HashMap<>();
        init();
    }

    @Override
    public void onClick(View v) {

        generateQrCode();
        if(!firstTimeCalled) {
            mDatabase.child("CourseEnrolled").child(courseId)
                    .child(courseYear).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String studentEnrolled = snapshot.getValue(String.class);
                    String[] list = studentEnrolled.split(",");
                    for (String str : list) {
                        studentEnrolledList.add(str);
                        attendance.put(str, false);
                    }
                    fill();
                    firstTimeCalled = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void generateQrCode() {
        String text="CS16105,20/02/2021,1 - 1";
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("   Sl.No   ");
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("   Reg No.   ");
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("   Present   ");
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        stk.addView(tbrow0);
    }
    private void fill(){
        String currRegNo;
        for (int i = 0; i < studentEnrolledList.size(); i++) {
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText("" + i);
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);

            currRegNo = studentEnrolledList.get(i);

            TextView t2v = new TextView(this);
            t2v.setText( currRegNo);
            t2v.setTextColor(Color.WHITE);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);

            if(attendance.get(currRegNo)){
                t3v.setText(" P ");
            } else {
                t3v.setText(" -- " );
            }
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            stk.addView(tbrow);
        }
    }
}