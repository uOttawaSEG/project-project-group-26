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

public class AttendeeViewEventsActivity extends AppCompatActivity {

    private ListView eventsListView;
    private TextView tvNoEvents;
    private Button btnAvailableEvents, btnRegisteredEvents;

    private DatabaseReference eventsRef;

    private ArrayList<Event> eventList = new ArrayList<>();
    private ArrayList<String> eventTitles = new ArrayList<>();

    private EventsAdapter eventsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_view_events);

        eventsListView = findViewById(R.id.eventsListView);
        tvNoEvents = findViewById(R.id.tvNoEvents);
        btnAvailableEvents = findViewById(R.id.btnAvailableEvents);
        btnRegisteredEvents = findViewById(R.id.btnRegisteredEvents);

        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        fetchEvents();

        btnAvailableEvents.setOnClickListener(view -> {
            // Already on Available Events
        });

        btnRegisteredEvents.setOnClickListener(view -> {
            Intent intent = new Intent(AttendeeViewEventsActivity.this, AttendeeRegisteredEventsActivity.class);
            startActivity(intent);
        });
    }

    private void fetchEvents() {
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                eventList.clear();
                eventTitles.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    if (event != null) {
                        eventList.add(event);
                    }
                }

                if (eventList.isEmpty()) {
                    tvNoEvents.setVisibility(View.VISIBLE);
                    eventsListView.setVisibility(View.GONE);
                } else {
                    tvNoEvents.setVisibility(View.GONE);
                    eventsListView.setVisibility(View.VISIBLE);
                    eventsAdapter = new EventsAdapter(AttendeeViewEventsActivity.this, eventList);
                    eventsListView.setAdapter(eventsAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AttendeeViewEventsActivity.this, "Failed to load events. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}