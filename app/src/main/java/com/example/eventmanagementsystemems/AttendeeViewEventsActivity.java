package com.example.eventmanagementsystemems;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.entities.Event;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// AttendeeViewEventsActivity displays a list of all available events to the attendee

public class AttendeeViewEventsActivity extends AppCompatActivity {

    // UI elements for displaying events, navigation buttons, and search bar
    private ListView eventsListView;
    private TextView tvNoEvents;
    private Button btnAvailableEvents, btnRegisteredEvents;
    private TextInputEditText etSearchBar;

    // Firebase database reference to the events node
    private DatabaseReference eventsRef;

    // Lists to store events and their titles for display

    private ArrayList<Event> eventList = new ArrayList<>();
    private ArrayList<Event> filteredEventList = new ArrayList<>();

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
        etSearchBar = findViewById(R.id.searchBar);

        // Initialize Firebase database reference

        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        // Fetch and display the list of all events

        fetchEvents();


        btnAvailableEvents.setOnClickListener(view -> {
            fetchEvents();
        });

        // Listener for the 'Registered Events' button to navigate to the registered events screen
 //tests
        btnRegisteredEvents.setOnClickListener(view -> {
            Intent intent = new Intent(AttendeeViewEventsActivity.this, AttendeeRegisteredEventsActivity.class);
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
                        //Log.d("EventFetched", "Fetched event: " + event.toString());

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
       // Log.d("FilterEvents", "filtering with query: " + query);
        if (query.isEmpty()) {
            filteredEventList.addAll(eventList);
        } else {
            for (Event event : eventList) {
                if (event.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        (event.getDescription() != null && event.getDescription().toLowerCase().contains(query.toLowerCase()))
                ) {
                    filteredEventList.add(event);
                    Log.d("FilterEvents", "Matched event: " + event.toString());
                }
            }
        }
        updateUI();

    }

    public void updateUI(){
        //Log.d("UpdateUI", "Updating UI. Filtered events count: " + filteredEventList.size());
        if(filteredEventList.isEmpty()){ //if there are no events that match the query, show "no events available"
            tvNoEvents.setVisibility(View.VISIBLE);
            eventsListView.setVisibility(View.GONE);
        }
        else{
            //Display the filtered events
            tvNoEvents.setVisibility(View.GONE);
            eventsListView.setVisibility(View.VISIBLE);
            if(eventsAdapter == null){
                //Initialize adapter if its the first time
                eventsAdapter = new EventsAdapter(AttendeeViewEventsActivity.this, new ArrayList<>(filteredEventList));
                eventsListView.setAdapter(eventsAdapter);
            }
            else{
                //Update the adapter's data and refresh the ListView
                eventsAdapter.updateView(new ArrayList<>(filteredEventList));
            }
        }
    }
}