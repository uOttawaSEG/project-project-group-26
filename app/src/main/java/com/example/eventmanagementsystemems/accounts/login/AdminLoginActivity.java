package com.example.eventmanagementsystemems.accounts.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.R;
import com.example.eventmanagementsystemems.RegistrationRequestsAdmin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText etAdminEmail, etAdminPassword;
    private Button btnAdminLogin;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        etAdminEmail = findViewById(R.id.etAdminEmail);
        etAdminPassword = findViewById(R.id.etAdminPassword);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);

        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAdminLogin();
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
                            String userEmail = mAuth.getCurrentUser().getEmail();
                            if (userEmail != null && userEmail.equals("admin@ems.com")) {
                                Toast.makeText(AdminLoginActivity.this, "Admin Login Successful", Toast.LENGTH_SHORT).show();

                                // Redirect to RegistrationRequestsAdmin
                                Intent intent = new Intent(AdminLoginActivity.this, RegistrationRequestsAdmin.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(AdminLoginActivity.this, "You are not an admin", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                            }

                        } else {
                            // If sign-in fails, display a message to the user.
                            Toast.makeText(AdminLoginActivity.this, "Authentication Failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}