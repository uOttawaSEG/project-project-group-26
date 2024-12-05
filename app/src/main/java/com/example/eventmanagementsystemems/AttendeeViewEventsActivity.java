package com.example.eventmanagementsystemems;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity; // Added import for Gravity
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.accounts.logoff.LogoffActivity;
import com.example.eventmanagementsystemems.entities.Event;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// AttendeeViewEventsActivity displays a list of all available events to the attendee

public class AttendeeViewEventsActivity extends AppCompatActivity {

    // UI elements for displaying events, navigation buttons, and search bar
    private ListView eventsListView;
    private TextView tvNoEvents;
    private Button btnAvailableEvents, btnRegisteredEvents;
    private TextInputEditText etSearchBar;
    private Button btnLogoff;

    // Firebase database reference to the events node
    private DatabaseReference eventsRef;

    // Lists to store events and their titles for display

    private ArrayList<Event> eventList = new ArrayList<>();
    private ArrayList<Event> filteredEventList = new ArrayList<>();

    // Adapter to manage and display events in the ListView
    private EventsAdapter eventsAdapter;

    // Firebase Authentication
    private FirebaseAuth mAuth;
    private String attendeeId;

    // Date formatter
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_view_events);

        // Initialize UI elements
        eventsListView = findViewById(R.id.eventsListView);
        tvNoEvents = findViewById(R.id.tvNoEvents);
        btnAvailableEvents = findViewById(R.id.btnAvailableEvents);
        btnRegisteredEvents = findViewById(R.id.btnRegisteredEvents);
        etSearchBar = findViewById(R.id.searchBar);
        btnLogoff = findViewById(R.id.btnLogoff);

        // Initialize Firebase database reference
        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        // Initialize Firebase Auth and get attendee ID
        mAuth = FirebaseAuth.getInstance();
        attendeeId = mAuth.getCurrentUser().getUid();

        // Fetch and display the list of all events
        fetchEvents();

        // Check for upcoming events and show reminder
        checkForUpcomingEvents();

        btnAvailableEvents.setOnClickListener(view -> {
            fetchEvents();
        });

        // Listener for the 'Registered Events' button to navigate to the registered events screen
        btnRegisteredEvents.setOnClickListener(view -> {
            Intent intent = new Intent(AttendeeViewEventsActivity.this, AttendeeRegisteredEventsActivity.class);
            startActivity(intent);
        });

        btnLogoff.setOnClickListener(view -> {
            // Open LogoffActivity to handle the logoff logic
            Intent intent = new Intent(AttendeeViewEventsActivity.this, LogoffActivity.class);
            startActivity(intent);
        });

        etSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterEvents(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable charSequence) {}
        });
    }

    // Fetches all events from Firebase and displays them in the ListView

    private void fetchEvents() {
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Clear previous data to avoid duplication
                eventList.clear();
                filteredEventList.clear();

                // Loop through each event in the database and add it to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    if (event != null) {
                        eventList.add(event);
                    }
                }
                filteredEventList.addAll(eventList); // Show all events by default

                // Update the UI based on whether there are events
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Display an error message if loading events fails
                Toast.makeText(AttendeeViewEventsActivity.this, "Failed to load events. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void filterEvents(String query) {
        filteredEventList.clear();
        if (query.isEmpty()) {
            filteredEventList.addAll(eventList);
        } else {
            for (Event event : eventList) {
                if (event.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        (event.getDescription() != null && event.getDescription().toLowerCase().contains(query.toLowerCase()))
                ) {
                    filteredEventList.add(event);
                }
            }
        }
        updateUI();
    }

    public void updateUI(){
        if(filteredEventList.isEmpty()){ //if there are no events that match the query, show "no events available"
            tvNoEvents.setVisibility(View.VISIBLE);
            eventsListView.setVisibility(View.GONE);
        }
        else{
            //Display the filtered events
            tvNoEvents.setVisibility(View.GONE);
            eventsListView.setVisibility(View.VISIBLE);
            if(eventsAdapter == null){
                //Initialize adapter if it's the first time
                eventsAdapter = new EventsAdapter(AttendeeViewEventsActivity.this, new ArrayList<>(filteredEventList));
                eventsListView.setAdapter(eventsAdapter);
            }
            else{
                //Update the adapter's data and refresh the ListView
                eventsAdapter.updateView(new ArrayList<>(filteredEventList));
            }
        }
    }

    private void checkForUpcomingEvents() {
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<String> upcomingEventTitles = new ArrayList<>();
                long currentTimeMillis = System.currentTimeMillis();
                long twentyFourHoursInMillis = 24 * 60 * 60 * 1000;

                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null && event.getAttendeeRegistrations() != null) {
                        String registrationStatus = event.getAttendeeRegistrations().get(attendeeId);
                        if ("accepted".equalsIgnoreCase(registrationStatus)) {
                            try {
                                Date eventDateTime = sdf.parse(event.getDate() + " " + event.getStartTime());
                                if (eventDateTime != null) {
                                    long timeUntilEvent = eventDateTime.getTime() - currentTimeMillis;
                                    if (timeUntilEvent > 0 && timeUntilEvent <= twentyFourHoursInMillis) {
                                        upcomingEventTitles.add(event.getTitle());
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                // Always display the toast, even if there are 0 upcoming events
                int eventCount = upcomingEventTitles.size();
                String reminderMessage = "You have " + eventCount + " event"
                        + (eventCount == 1 ? "" : "s") + " starting within 24 hours.";

                Toast toast = Toast.makeText(AttendeeViewEventsActivity.this, reminderMessage, Toast.LENGTH_LONG);
                // Set the gravity to display the toast at the top
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100); // Adjust the yOffset as needed
                toast.show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle database error
            }
        });
    }
}