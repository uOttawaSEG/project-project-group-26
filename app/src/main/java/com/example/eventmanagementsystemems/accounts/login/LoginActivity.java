package com.example.eventmanagementsystemems.accounts.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.PendingApplicationActivity;
import com.example.eventmanagementsystemems.WelcomeScreen;
import com.example.eventmanagementsystemems.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;

    private TextView tvLoginAsAdmin;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize UI elements
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        tvLoginAsAdmin = findViewById(R.id.tvLoginAsAdmin);

        // Set onClick listener for login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogin();
            }
        });

        // Set onClick listener for admin login
        tvLoginAsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to AdminLoginActivity
                Intent intent = new Intent(LoginActivity.this, AdminLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleLogin() {
        String emailAddress = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        // Input validation
        if (emailAddress.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Authenticate user with Firebase
        mAuth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign-in success
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            String userId = currentUser.getUid();

                            // Check if user is in accepted attendees
                            usersRef.child("accepted").child("attendees").child(userId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot attendeeSnapshot) {
                                            if (attendeeSnapshot.exists()) {
                                                String firstName = attendeeSnapshot.child("firstName").getValue(String.class);
                                                String userType = "Attendee";
                                                proceedToWelcomeScreen(firstName, userType);
                                            } else {
                                                // Check if user is in accepted organizers
                                                usersRef.child("accepted").child("organizers").child(userId)
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot organizerSnapshot) {
                                                                if (organizerSnapshot.exists()) {
                                                                    String firstName = organizerSnapshot.child("firstName").getValue(String.class);
                                                                    String userType = "Organizer";
                                                                    proceedToWelcomeScreen(firstName, userType);
                                                                } else {
                                                                    // Check if user is in rejected or pending
                                                                    checkRejectedOrPending(userId);
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {
                                                                Toast.makeText(LoginActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(LoginActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            // If sign-in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication Failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkRejectedOrPending(String userId) {
        // Check rejected attendees
        usersRef.child("rejected").child("attendees").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot rejectedAttendeeSnapshot) {
                        if (rejectedAttendeeSnapshot.exists()) {
                            showRejectedMessage();
                        } else {
                            // Check rejected organizers
                            usersRef.child("rejected").child("organizers").child(userId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot rejectedOrganizerSnapshot) {
                                            if (rejectedOrganizerSnapshot.exists()) {
                                                showRejectedMessage();
                                            } else {
                                                // User is pending
                                                showPendingMessage();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(LoginActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showRejectedMessage() {
        Toast.makeText(LoginActivity.this, "Your registration was rejected. Contact 613-304-0180 for more info.", Toast.LENGTH_LONG).show();
        mAuth.signOut();
    }

    private void showPendingMessage() {
        // Redirect to Pending Application Screen
        Intent intent = new Intent(LoginActivity.this, PendingApplicationActivity.class);
        startActivity(intent);
        finish();
    }

    private void proceedToWelcomeScreen(String firstName, String userType) {
        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

        // Redirect to WelcomeScreen
        Intent intent = new Intent(LoginActivity.this, WelcomeScreen.class);
        intent.putExtra("FIRST_NAME", firstName != null ? firstName : "");
        intent.putExtra("userType", userType);
        startActivity(intent);
        finish();
    }
}
