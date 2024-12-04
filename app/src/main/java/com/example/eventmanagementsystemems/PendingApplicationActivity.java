package com.example.eventmanagementsystemems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.accounts.logoff.LogoffActivity;

public class PendingApplicationActivity extends AppCompatActivity {

    // TextView for displaying the pending application message
    private TextView tvPendingMessage;
    private Button btnLogoff;

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
        btnLogoff = findViewById(R.id.btnLogoff);

        // Set the pending message
        tvPendingMessage.setText("Your application is pending.\nYou will receive an email once a decision has been made.");

        btnLogoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open LogoffActivity to handle the logoff logic
                Intent intent = new Intent(PendingApplicationActivity.this, LogoffActivity.class);
                startActivity(intent);
            }
        });

    }
}