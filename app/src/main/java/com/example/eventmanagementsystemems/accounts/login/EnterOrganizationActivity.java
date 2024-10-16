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
import com.example.eventmanagementsystemems.WelcomeScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseApp;

import java.util.HashMap;
import java.util.Map;

public class EnterOrganizationActivity extends AppCompatActivity {

    private EditText etOrganizationName;
    private Button btnSubmitOrganization;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private String userId;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_organization);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        etOrganizationName = findViewById(R.id.etOrganizationName);
        btnSubmitOrganization = findViewById(R.id.btnSubmitOrganization);

        userId = mAuth.getCurrentUser().getUid();

        // Get userType from Intent
        Intent intent = getIntent();
        userType = intent.getStringExtra("userType");

        btnSubmitOrganization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOrganizationSubmit();
            }
        });
    }

    private void handleOrganizationSubmit() {
        String organizationName = etOrganizationName.getText().toString().trim();

        if (organizationName.isEmpty()) {
            Toast.makeText(this, "Please enter your organization name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save organization name to database under the user's profile
        Map<String, Object> updates = new HashMap<>();
        updates.put("organizationName", organizationName);

        // Save under "users/organizers/userId"
        usersRef.child("organizers").child(userId).updateChildren(updates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EnterOrganizationActivity.this, "Organization Name Saved", Toast.LENGTH_SHORT).show();

                        // Redirect to WelcomeScreen
                        Intent intent = new Intent(EnterOrganizationActivity.this, WelcomeScreen.class);
                        intent.putExtra("userType", userType);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(EnterOrganizationActivity.this, "Failed to save organization name", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
