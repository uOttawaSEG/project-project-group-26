package com.example.eventmanagementsystemems.accounts.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.MainActivity;
import com.example.eventmanagementsystemems.R;
import com.example.eventmanagementsystemems.RegistrationRequestsAdmin;
import com.example.eventmanagementsystemems.WelcomeScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText etAdminEmail, etAdminPassword;
    private Button btnAdminLogin, btnViewList;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        etAdminEmail = findViewById(R.id.etAdminEmail);
        etAdminPassword = findViewById(R.id.etAdminPassword);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);
        btnViewList = findViewById(R.id.viewList);

        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAdminLogin();
            }
        });

        btnViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminLoginActivity.this, RegistrationRequestsAdmin.class);
                startActivity(intent);
            }
        });
    }

    private void handleAdminLogin() {
        String emailAddress = etAdminEmail.getText().toString().trim();
        String password = etAdminPassword.getText().toString();

        // Input validation
        if (emailAddress.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter admin email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Authenticate admin with Firebase
        mAuth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign-in success
                            String userId = mAuth.getCurrentUser().getUid();

                            // Retrieve admin data from database
                            usersRef.child("admins").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String firstName = snapshot.child("firstName").getValue(String.class);
                                        Toast.makeText(AdminLoginActivity.this, "Admin Login Successful", Toast.LENGTH_SHORT).show();

                                        // Redirect to WelcomeScreen
                                        Intent intent = new Intent(AdminLoginActivity.this, WelcomeScreen.class);
                                        intent.putExtra("FIRST_NAME", firstName != null ? firstName : "");
                                        intent.putExtra("userType", "Administrator");
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(AdminLoginActivity.this, "No admin data found", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(AdminLoginActivity.this, "Failed to retrieve admin data", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            // If sign-in fails, display a message to the user.
                            Toast.makeText(AdminLoginActivity.this, "Authentication Failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}