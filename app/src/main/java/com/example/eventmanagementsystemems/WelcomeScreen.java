package com.example.eventmanagementsystemems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.accounts.organizer.OrganizerHomeActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Welcome screen activity that displays a welcome message to attendees
 * or redirects organizers to their home screen.
 */
public class WelcomeScreen extends AppCompatActivity {

    private Button btnLogoff;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String userType = getIntent().getStringExtra("userType");
        if (userType != null && userType.equals("Organizer")) {
            // Redirect to OrganizerHomeActivity
            Intent intent = new Intent(WelcomeScreen.this, OrganizerHomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            // For Attendees, show welcome screen
            setContentView(R.layout.activity_welcome_screen);

            btnLogoff = findViewById(R.id.btnLogoff);
            btnLogoff.setOnClickListener(view -> {
                FirebaseAuth.getInstance().signOut();

                Toast.makeText(WelcomeScreen.this, "Logged off successfully!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(WelcomeScreen.this, MainActivity.class);
                startActivity(intent);
                finish(); // close the welcome screen
            });
        }
    }
}