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

import com.example.eventmanagementsystemems.PendingApplicationActivity;
import com.example.eventmanagementsystemems.R;
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

        // Email format validation
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Password strength validation (minimum 6 characters)
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Phone number validation
        if (!phoneNumber.matches("\\d{10}")) {
            Toast.makeText(this, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
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

                            // Modify the database path to include "pending"
                            String userTypePath = "pending/" + userType.toLowerCase() + "s";

                            // Save user profile data in Realtime Database under "pending/attendees" or "pending/organizers"
                            usersRef.child(userTypePath).child(userId).setValue(userProfile)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> dbTask) {
                                            if (dbTask.isSuccessful()) {
                                                Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                                if (userType.equals("Organizer")) {
                                                    // Redirect to EnterOrganizationActivity to collect organization name
                                                    Intent intent = new Intent(SignUpActivity.this, EnterOrganizationActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    // For Attendees, proceed to pending screen
                                                    // Sign out the user
                                                    mAuth.signOut();

                                                    // Redirect to Pending Application Screen
                                                    Intent intent = new Intent(SignUpActivity.this, PendingApplicationActivity.class);
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