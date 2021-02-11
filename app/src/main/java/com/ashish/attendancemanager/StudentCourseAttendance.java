package com.ashish.attendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ashish.attendancemanager.model.ClassDateInfo;
import com.ashish.attendancemanager.model.CourseInfo;
import com.ashish.attendancemanager.model.DateAttendanceInfo;
import com.ashish.attendancemanager.model.Student;
import com.ashish.attendancemanager.ui.AttendanceTableViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class StudentCourseAttendance extends AppCompatActivity {

    private TextView courseNameTextView, courseIdTextView;
    private List<DateAttendanceInfo> dateSheet;
    private DatabaseReference mDatabase;
    private Student student = null;
    private String courseID;
    private String courseName;
    private String  dateStringList;
    private RecyclerView recyclerView;
    private AttendanceTableViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course_attendance);

        student =  (Student) getIntent().getSerializableExtra("STUDENT_OBJECT");
        courseID = getIntent().getExtras().getString("COURSE_ID");
        courseName = getIntent().getExtras().getString("COURSE_NAME");

        courseNameTextView = findViewById(R.id.courseNameTextView);
        courseIdTextView = findViewById(R.id.courseid);
        courseNameTextView.setText(courseName);
        courseIdTextView.setText(courseID);

        recyclerView = findViewById(R.id.recyclerViewStudentAttendanceList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dateSheet = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("StudentAttendance").child(student.getUserId()).child("2021").
                child(courseID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    ClassDateInfo classDateInfo = snapshot.getValue(ClassDateInfo.class);

//                    Log.d("classDateInfo", classDateInfo.getCourseName());
//                    Log.d("classDateInfo", classDateInfo.getDateTimeListInfo());
                    String str = classDateInfo.getDateTimeListInfo();
                    for(int i=0;i<str.length();i++) {
                        if(str.charAt(i)>='0' && str.charAt(i)<='9'){
                            dateStringList = str.substring(i+1);
                            break;
                        }
                    }

                   // Log.d("DateStringList", dateStringList);
                    String []token = dateStringList.split(",");

                    for(int i=0;i<token.length;i++) {
                        int idx = 0;
                        String s = token[i];

                        if(s == null) {
                            continue;
                        }
                        for(int j=0;j<s.length();j++) {
                            if(s.charAt(j) == ' '){
                                idx = j;
                                break;
                            }

                        }

                        String date = s.substring(0,idx);
                        String timeDuration = s.substring(idx+1);
                        DateAttendanceInfo dateAttendanceInfo = new DateAttendanceInfo(date,timeDuration);
                        dateSheet.add(dateAttendanceInfo);

                    }
                    for(int i=0;i<dateSheet.size();i++) {
                        Log.d("dateSheet", dateSheet.get(i).getDate());
                        Log.d("dateSheet", dateSheet.get(i).getTimeInterval());
                    }

                    adapter = new AttendanceTableViewAdapter(dateSheet);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}