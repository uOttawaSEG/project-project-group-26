package com.example.eventmanagementsystemems;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PendingApplicationActivity extends AppCompatActivity {

    // TextView for displaying the pending application message
    private TextView tvPendingMessage;

    /**
     * Initializes the PendingApplicationActivity and sets up the UI to display
     * a message informing users about the pending status of their application.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_application);

        tvPendingMessage = findViewById(R.id.tvPendingMessage);

        // Set the pending message
        tvPendingMessage.setText("Your application is pending.\nYou will receive an email once a decision has been made.");
    }
}