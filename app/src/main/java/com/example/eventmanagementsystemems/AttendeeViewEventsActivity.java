package com.example.eventmanagementsystemems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.entities.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// AttendeeViewEventsActivity displays a list of all available events to the attendee

public class AttendeeViewEventsActivity extends AppCompatActivity {

    // UI elements for displaying events and navigation buttons
    private ListView eventsListView;
    private TextView tvNoEvents;
    private Button btnAvailableEvents, btnRegisteredEvents;

    // Firebase database reference to the events node
    private DatabaseReference eventsRef;

    // Lists to store events and their titles for display

    private ArrayList<Event> eventList = new ArrayList<>();
    private ArrayList<String> eventTitles = new ArrayList<>();

    // Adapter to manage and display events in the ListView
    private EventsAdapter eventsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_view_events);

        // Initialize UI elements
        eventsListView = findViewById(R.id.eventsListView);
        tvNoEvents = findViewById(R.id.tvNoEvents);
        btnAvailableEvents = findViewById(R.id.btnAvailableEvents);
        btnRegisteredEvents = findViewById(R.id.btnRegisteredEvents);

        // Initialize Firebase database reference

        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        // Fetch and display the list of all events

        fetchEvents();

        // Listener for the 'Available Events' button (currently on this screen, so no action needed)

        btnAvailableEvents.setOnClickListener(view -> {
            // Already on Available Events
        });

        // Listener for the 'Registered Events' button to navigate to the registered events screen

        btnRegisteredEvents.setOnClickListener(view -> {
            Intent intent = new Intent(AttendeeViewEventsActivity.this, AttendeeRegisteredEventsActivity.class);
            startActivity(intent);
        });
    }

    // Fetches all events from Firebase and displays them in the ListView

    private void fetchEvents() {
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Clear previous data to avoid duplication
                eventList.clear();
                eventTitles.clear();

                // Loop through each event in the database and add it to the list

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    if (event != null) {
                        eventList.add(event);
                    }
                }

                // Update the UI based on whether there are events

                if (eventList.isEmpty()) {
                    // Show message if no events are available
                    tvNoEvents.setVisibility(View.VISIBLE);
                    eventsListView.setVisibility(View.GONE);
                } else {
                    // Show events list if events are available
                    tvNoEvents.setVisibility(View.GONE);
                    eventsListView.setVisibility(View.VISIBLE);
                    // Set up the adapter to display events in the ListView
                    eventsAdapter = new EventsAdapter(AttendeeViewEventsActivity.this, eventList);
                    eventsListView.setAdapter(eventsAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Display an error message if loading events fails
                Toast.makeText(AttendeeViewEventsActivity.this, "Failed to load events. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}