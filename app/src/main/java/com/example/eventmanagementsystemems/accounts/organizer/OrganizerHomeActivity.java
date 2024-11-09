package com.example.eventmanagementsystemems.accounts.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.CreateEventActivity;
import com.example.eventmanagementsystemems.R;
import com.example.eventmanagementsystemems.ViewEventsActivity;

public class OrganizerHomeActivity extends AppCompatActivity {

    private Button btnCreateEvent;
    private Button btnUpcomingEvents;
    private Button btnPastEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_home);

        btnCreateEvent = findViewById(R.id.btnCreateEvent);
        btnUpcomingEvents = findViewById(R.id.btnUpcomingEvents);
        btnPastEvents = findViewById(R.id.btnPastEvents);

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
    }
}