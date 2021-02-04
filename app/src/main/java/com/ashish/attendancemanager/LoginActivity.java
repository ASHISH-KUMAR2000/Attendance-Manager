package com.ashish.attendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ashish.attendancemanager.model.User;
import com.ashish.attendancemanager.ui.UserApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Spinner dropDown ;
    private EditText userIdEditText, passwordEditText;
    private Button loginButton;
    private String userId, password, loginType;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userIdEditText = findViewById(R.id.loginActivity_userId);
        passwordEditText = findViewById(R.id.loginActivity_userPassword);
        loginButton = findViewById(R.id.loginActivity_loginButton);

        dropDown = findViewById(R.id.loginActivity_spinner);
        //create a list of items for the spinner.
        String[] items = new String[]{"Admin", "Teacher", "Student"};
        //create an adapter to describe how the items are displayed.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropDown.setAdapter(adapter);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = userIdEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();
                loginType = dropDown.getSelectedItem().toString();

                if(!TextUtils.isEmpty(userId)&&!TextUtils.isEmpty(password)&&
                        !TextUtils.isEmpty(loginType)){

                    verifyUser(userId, password, loginType);

                } else{
                    Toast.makeText(LoginActivity.this,
                            "Empty Text Field Not Allowed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verifyUser(final String userId, final String password, String loginType) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(loginType).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    User currentUser = dataSnapshot.getValue(User.class);
                    UserApi userApi = UserApi.getInstance();
                    userApi.setValue(currentUser);
                    if(currentUser.getUserId().equals(userId)&&
                            currentUser.getUserPassword().equals(password)){

                        loginWithIdPassword(userId+"@admin.com", password);

                    }
                    else{
                        Toast.makeText(LoginActivity.this,
                                "Wrong Credentials",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void loginWithIdPassword(String userId, String password) {
        mAuth.signInWithEmailAndPassword(userId, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            switchUser(loginType);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void switchUser(String loginType) {
        if(loginType.equals("Admin")){
            // Goto Admin View
            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
        } else if(loginType.equals("Teacher")){
            // Goto Teacher View
        } else{
            // Goto Student View
        }
        finish();
    }
}
