package com.example.eventmanagementsystemems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.entities.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * Activity for displaying either upcoming or past events organized by the current user.
 * Fetches events from Firebase and displays them in a ListView with the option to view event details.
 */
public class ViewEventsActivity extends AppCompatActivity {

    // UI components for displaying events and a message when there are no events
    private ListView eventsListView;
    private TextView tvNoEvents;

    // Firebase reference for events and user information
    private DatabaseReference eventsRef;
    private String organizerId;
    private String eventType; // "upcoming" or "past"

    // Lists to hold event objects and titles for display in the ListView
    private ArrayList<Event> eventList = new ArrayList<>();
    private ArrayList<String> eventTitles = new ArrayList<>();

    // Formatter for parsing and comparing event dates
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private Date today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        eventsListView = findViewById(R.id.eventsListView);
        tvNoEvents = findViewById(R.id.tvNoEvents);

        organizerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        eventType = getIntent().getStringExtra("eventType");

        today = new Date();

        fetchEvents();
    }

    /**
     * Retrieves events organized by the current user and filters them based on event type
     * ("upcoming" or "past") compared to the current date.
     */
    private void fetchEvents() {
        eventsRef.orderByChild("organizerId").equalTo(organizerId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        eventList.clear();
                        eventTitles.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Event event = snapshot.getValue(Event.class);
                            try {
                                Date eventDate = sdf.parse(event.getDate());
                                if (eventType.equals("upcoming") && !eventDate.before(today)) {
                                    eventList.add(event);
                                    eventTitles.add(event.getTitle());
                                } else if (eventType.equals("past") && eventDate.before(today)) {
                                    eventList.add(event);
                                    eventTitles.add(event.getTitle());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (eventList.isEmpty()) {
                            tvNoEvents.setVisibility(View.VISIBLE);
                            eventsListView.setVisibility(View.GONE);
                        } else {
                            tvNoEvents.setVisibility(View.GONE);
                            eventsListView.setVisibility(View.VISIBLE);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewEventsActivity.this,
                                    android.R.layout.simple_list_item_1, eventTitles);
                            eventsListView.setAdapter(adapter);

                            eventsListView.setOnItemClickListener((adapterView, view, position, l) -> {
                                Event selectedEvent = eventList.get(position);
                                Intent intent = new Intent(ViewEventsActivity.this, EventDetailActivity.class);
                                intent.putExtra("eventId", selectedEvent.getEventId());
                                intent.putExtra("eventType", eventType);
                                startActivity(intent);
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }
}