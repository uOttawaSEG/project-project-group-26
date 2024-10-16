package com.example.eventmanagementsystemems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.eventmanagementsystemems.accounts.logoff.LogoffActivity;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeScreen extends AppCompatActivity {

    private TextView tvWelcomeMessage;
    private String firstName;
    private Button btnLogoff;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        tvWelcomeMessage = findViewById(R.id.tvWelcomeMessage);
        Intent intent = getIntent();
        String userType = intent.getStringExtra("userType");
        if (userType != null) {
            tvWelcomeMessage.setText("Welcome! You are logged in as" + userType + ".");
        }
        btnLogoff = findViewById(R.id.btnLogoff);
        btnLogoff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FirebaseAuth.getInstance().signOut();

                Toast.makeText(WelcomeScreen.this, "Logged off succesfully!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(WelcomeScreen.this, MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

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