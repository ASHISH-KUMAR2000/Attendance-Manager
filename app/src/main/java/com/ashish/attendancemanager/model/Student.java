package com.ashish.attendancemanager.model;


import java.util.ArrayList;


public class Student extends User{
    //private String userId, userName, userPassword, userEmail, userPhoneNumber;
    private String deptName;
    private ArrayList<String> courseEnrolled;

    public Student() {

    }
    public Student(String userId, String userName, String userPassword,
                   String userEmail, String userPhoneNumber, String deptName, ArrayList<String> courseEnrolled) {
//        this.userId = userId;
//        this.userName = userName;
//        this.userPassword = userPassword;
//        this.userEmail = userEmail;
//        this.userPhoneNumber = userPhoneNumber;
        super(userId,userName,userPassword,userEmail,userPhoneNumber);
        this.deptName = deptName;
        this.courseEnrolled =courseEnrolled;

    }
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getUserPassword() {
//        return userPassword;
//    }
//
//    public void setUserPassword(String userPassword) {
//        this.userPassword = userPassword;
//    }
//
//    public String getUserEmail() {
//        return userEmail;
//    }
//
//    public void setUserEmail(String userEmail) {
//        this.userEmail = userEmail;
//    }
//
//    public String getUserPhoneNumber() {
//        return userPhoneNumber;
//    }
//
//    public void setUserPhoneNumber(String userPhoneNumber) {
//        this.userPhoneNumber = userPhoneNumber;
//    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public ArrayList<String> getCourseEnrolled() {
        return courseEnrolled;
    }

    public void setCourseEnrolled(ArrayList<String> courseEnrolled) {

        this.courseEnrolled = courseEnrolled;
    }
    public void addToCourseEnrolled(String course) {

        this.courseEnrolled.add(course);
    }

}
