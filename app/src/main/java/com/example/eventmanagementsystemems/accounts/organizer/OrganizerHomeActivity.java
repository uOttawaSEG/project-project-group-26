package com.example.eventmanagementsystemems.accounts.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.CreateEventActivity;
import com.example.eventmanagementsystemems.R;
import com.example.eventmanagementsystemems.ViewEventsActivity;
import com.example.eventmanagementsystemems.accounts.logoff.LogoffActivity;

public class OrganizerHomeActivity extends AppCompatActivity {

    private Button btnCreateEvent;
    private Button btnUpcomingEvents;
    private Button btnPastEvents;
    private Button btnLogoff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_home);

        btnCreateEvent = findViewById(R.id.btnCreateEvent);
        btnUpcomingEvents = findViewById(R.id.btnUpcomingEvents);
        btnPastEvents = findViewById(R.id.btnPastEvents);
        btnLogoff = findViewById(R.id.btnLogoff);

        btnCreateEvent.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerHomeActivity.this, CreateEventActivity.class);
            startActivity(intent);
        });

        btnUpcomingEvents.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerHomeActivity.this, ViewEventsActivity.class);
            intent.putExtra("eventType", "upcoming");
            startActivity(intent);
        });

        btnPastEvents.setOnClickListener(view -> {
            Intent intent = new Intent(OrganizerHomeActivity.this, ViewEventsActivity.class);
            intent.putExtra("eventType", "past");
            startActivity(intent);
        });

        btnLogoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open LogoffActivity to handle the logoff logic
                Intent intent = new Intent(OrganizerHomeActivity.this, LogoffActivity.class);
                startActivity(intent);
            }
        });

    }
}