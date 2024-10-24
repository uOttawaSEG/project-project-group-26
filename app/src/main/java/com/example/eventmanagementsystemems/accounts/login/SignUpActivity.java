package com.example.eventmanagementsystemems.accounts.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.R;
import com.example.eventmanagementsystemems.WelcomeScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etPassword, etPhone, etHomeAddress;
    private RadioGroup rgUserType;
    private RadioButton rbAttendee, rbOrganizer;
    private Button btnSignup;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    private static final String TAG = "SignUpActivity"; // For logging


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize UI elements
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        etHomeAddress = findViewById(R.id.etAddress);
        rgUserType = findViewById(R.id.rgUserType);
        rbAttendee = findViewById(R.id.rbAttendee);
        rbOrganizer = findViewById(R.id.rbOrganizer);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignup();
            }
        });
    }

    private void handleSignup() {
        // Retrieve user input
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String emailAddress = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String phoneNumber = etPhone.getText().toString().trim();
        String address = etHomeAddress.getText().toString().trim();
        String userType = rbAttendee.isChecked() ? "Attendee" : "Organizer";

        // Input validation
        if (firstName.isEmpty() || lastName.isEmpty() || emailAddress.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign-up success
                            String userId = mAuth.getCurrentUser().getUid();

                            // Create user profile data
                            Map<String, Object> userProfile = new HashMap<>();
                            userProfile.put("firstName", firstName);
                            userProfile.put("lastName", lastName);
                            userProfile.put("email", emailAddress);
                            userProfile.put("phoneNumber", phoneNumber);
                            userProfile.put("address", address);
                            userProfile.put("userType", userType);
                            userProfile.put("status", 0);

                            // Save user profile data in Realtime Database under "attendees" or "organizers"
                            usersRef.child(userType.toLowerCase() + "s").child(userId).setValue(userProfile)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> dbTask) {
                                            if (dbTask.isSuccessful()) {
                                                Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                // Redirect to EnterOrganizationActivity if userType is Organizer
                                                if (userType.equals("Organizer")) {
                                                    Intent intent = new Intent(SignUpActivity.this, EnterOrganizationActivity.class);
                                                    intent.putExtra("userType", userType);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    // Redirect to WelcomeScreen
                                                    Intent intent = new Intent(SignUpActivity.this, WelcomeScreen.class);
                                                    intent.putExtra("userType", userType); // Pass the userType to WelcomeScreen
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            } else {
                                                Toast.makeText(SignUpActivity.this, "Failed to save user profile", Toast.LENGTH_SHORT).show();
                                                Log.w(TAG, "Error adding document", dbTask.getException());
                                            }
                                        }
                                    });

                        } else {
                            // If sign-up fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }
}
