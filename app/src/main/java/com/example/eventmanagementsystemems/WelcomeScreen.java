package com.example.eventmanagementsystemems;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeScreen extends AppCompatActivity {

    private TextView tvWelcomeMessage;
    private String firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        tvWelcomeMessage = findViewById(R.id.tvWelcomeMessage);
        Intent intent = getIntent();
        String userType = intent.getStringExtra("userType");
        if (userType != null) {
            tvWelcomeMessage.setText("Welcome, " + userType + "!");
        }
        // Get the first name passed from LoginActivity
//        firstName = getIntent().getStringExtra("FIRST_NAME");

        // Set the welcome message
//        if (firstName != null && !firstName.isEmpty()) {
//            tvWelcomeMessage.setText("Welcome, " + firstName + "!");
//        } else {
//            tvWelcomeMessage.setText("Welcome to EMS!");
//        }
    }
}