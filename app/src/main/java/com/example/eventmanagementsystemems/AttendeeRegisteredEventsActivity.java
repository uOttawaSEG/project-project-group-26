package com.example.eventmanagementsystemems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.eventmanagementsystemems.entities.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

// AttendeeRegisteredEventsActivity displays a list of events that the attendee has registered for
public class AttendeeRegisteredEventsActivity extends AppCompatActivity {

    // UI elements for displaying registered events and a message when there are no events
    private ListView registeredEventsListView;
    private TextView tvNoRegisteredEvents;

    // Firebase references and the ID of the current attendee
    private DatabaseReference eventsRef;
    private String attendeeId;

    // Lists to store registered events and their titles for display
    private ArrayList<Event> registeredEvents = new ArrayList<>();
    private ArrayList<String> eventTitles = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    // Firebase Authentication instance
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_registered_events);

        // Initialize UI elements
        registeredEventsListView = findViewById(R.id.registeredEventsListView);
        tvNoRegisteredEvents = findViewById(R.id.tvNoRegisteredEvents);

        // Initialize Firebase Authentication and retrieve the current user's ID
        mAuth = FirebaseAuth.getInstance();
        attendeeId = mAuth.getCurrentUser().getUid();

        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        // Fetch the list of events that the attendee has registered for
        fetchRegisteredEvents();
    }

    // Fetches the list of events the attendee has registered for from Firebase
    private void fetchRegisteredEvents() {
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Clear previous data to avoid duplication
                registeredEvents.clear();
                eventTitles.clear();

                // Loop through each event and check if the attendee is registered
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null && event.getAttendeeRegistrations() != null) {
                        String status = event.getAttendeeRegistrations().get(attendeeId);
                        if (status != null) {
                            registeredEvents.add(event);
                            eventTitles.add(event.getTitle() + " (" + status + ")");
                        }
                    }
                }

                // Update UI based on whether there are registered events
                if (registeredEvents.isEmpty()) {
                    // Show message if no registered events
                    tvNoRegisteredEvents.setVisibility(View.VISIBLE);
                    registeredEventsListView.setVisibility(View.GONE);
                } else {
                    // Show registered events list if there are events

                    tvNoRegisteredEvents.setVisibility(View.GONE);
                    registeredEventsListView.setVisibility(View.VISIBLE);
                    // Set up the adapter to display event titles in the list view
                    adapter = new ArrayAdapter<>(AttendeeRegisteredEventsActivity.this, android.R.layout.simple_list_item_1, eventTitles);
                    registeredEventsListView.setAdapter(adapter);

                    // Set up a click listener to view details of the selected event
                    registeredEventsListView.setOnItemClickListener((parent, view, position, id) -> {
                        Event selectedEvent = registeredEvents.get(position);
                        Intent intent = new Intent(AttendeeRegisteredEventsActivity.this, AttendeeEventDetailActivity.class);
                        intent.putExtra("eventId", selectedEvent.getEventId());
                        startActivity(intent);
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Display an error message if loading the events fails
                Toast.makeText(AttendeeRegisteredEventsActivity.this, "Failed to load registered events.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}