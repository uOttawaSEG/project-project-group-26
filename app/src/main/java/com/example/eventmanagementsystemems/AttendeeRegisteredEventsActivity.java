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

public class AttendeeRegisteredEventsActivity extends AppCompatActivity {

    private ListView registeredEventsListView;
    private TextView tvNoRegisteredEvents;

    private DatabaseReference eventsRef;
    private String attendeeId;

    private ArrayList<Event> registeredEvents = new ArrayList<>();
    private ArrayList<String> eventTitles = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_registered_events);

        registeredEventsListView = findViewById(R.id.registeredEventsListView);
        tvNoRegisteredEvents = findViewById(R.id.tvNoRegisteredEvents);

        mAuth = FirebaseAuth.getInstance();
        attendeeId = mAuth.getCurrentUser().getUid();

        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        fetchRegisteredEvents();
    }

    private void fetchRegisteredEvents() {
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                registeredEvents.clear();
                eventTitles.clear();

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

                if (registeredEvents.isEmpty()) {
                    tvNoRegisteredEvents.setVisibility(View.VISIBLE);
                    registeredEventsListView.setVisibility(View.GONE);
                } else {
                    tvNoRegisteredEvents.setVisibility(View.GONE);
                    registeredEventsListView.setVisibility(View.VISIBLE);
                    adapter = new ArrayAdapter<>(AttendeeRegisteredEventsActivity.this, android.R.layout.simple_list_item_1, eventTitles);
                    registeredEventsListView.setAdapter(adapter);

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
                Toast.makeText(AttendeeRegisteredEventsActivity.this, "Failed to load registered events.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}