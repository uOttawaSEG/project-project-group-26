package com.example.eventmanagementsystemems;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.entities.Attendee;
import com.example.eventmanagementsystemems.entities.AttendeeListAdapter;
import com.example.eventmanagementsystemems.entities.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;

// EventDetailActivity displays the details of a specific event for an organizer and allows managing attendee registrations
public class EventDetailActivity extends AppCompatActivity {

    // UI elements for displaying event details, list of attendees, and control buttons
    private TextView tvEventDetails;
    private ListView lvAttendees;
    private Button btnApproveAll, btnDeleteEvent;

    // Variables to store event ID, event type (e.g., "upcoming"), and Firebase references
    private String eventId, eventType;

    private DatabaseReference eventsRef;
    private DatabaseReference usersRef;

    // Event data and list of attendees
    private Event event;

    private ArrayList<Attendee> attendeeList = new ArrayList<>();
    private AttendeeListAdapter adapter;

    // Organizer ID to verify ownership and manage event actions

    private String organizerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        // Initialize UI elements
        tvEventDetails = findViewById(R.id.tvEventDetails);
        lvAttendees = findViewById(R.id.lvAttendees);
        btnApproveAll = findViewById(R.id.btnApproveAll);
        btnDeleteEvent = findViewById(R.id.btnDeleteEvent);

        // Retrieve event ID and type from the intent
        eventId = getIntent().getStringExtra("eventId");
        eventType = getIntent().getStringExtra("eventType");

        organizerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        eventsRef = FirebaseDatabase.getInstance().getReference("events").child(eventId);
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Fetch and display event details

        fetchEventDetails();

        // Set listener for approving all attendee registrations

        btnApproveAll.setOnClickListener(view -> approveAllRegistrations());

        // Set listener for deleting the event

        btnDeleteEvent.setOnClickListener(view -> deleteEvent());
    }

    // Fetches event details from Firebase and updates the UI

    private void fetchEventDetails() {
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                event = snapshot.getValue(Event.class);

                // If the event is upcoming, fetch attendee registrations

                if (event != null) {
                    // Build event details and display them
                    StringBuilder details = new StringBuilder();
                    details.append("Title: ").append(event.getTitle()).append("\n");
                    details.append("Description: ").append(event.getDescription()).append("\n");
                    details.append("Date: ").append(event.getDate()).append("\n");
                    details.append("Start Time: ").append(event.getStartTime()).append("\n");
                    details.append("End Time: ").append(event.getEndTime()).append("\n");
                    details.append("Address: ").append(event.getEventAddress()).append("\n");

                    tvEventDetails.setText(details.toString());

                    if (eventType.equals("upcoming")) {
                        fetchAttendeeRegistrations();
                    } else {
                        // Hide attendees list and approval button for past events
                        lvAttendees.setVisibility(View.GONE);
                        btnApproveAll.setVisibility(View.GONE);
                    }
                } else {
                    // Show error if event not found and close the activity
                    Toast.makeText(EventDetailActivity.this, "Event not found.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Show error message if fetching event details fails
                Toast.makeText(EventDetailActivity.this, "Failed to fetch event details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fetches the list of attendee registrations for the event
    private void fetchAttendeeRegistrations() {
        HashMap<String, String> registrations = event.getAttendeeRegistrations();

        if (registrations == null || registrations.isEmpty()) {
            // No registrations
            Toast.makeText(this, "No attendee registrations yet.", Toast.LENGTH_SHORT).show();
            attendeeList.clear();
            updateAttendeeListView();
            return;
        }

        attendeeList.clear();
        final int[] remaining = {registrations.size()};

        // Fetch each attendee's details based on registration status
        for (String attendeeId : registrations.keySet()) {
            String status = registrations.get(attendeeId);
            // Include all statuses: pending, accepted, rejected
            // If you want only pending, use: if (status.equals("pending")) { ... }
            usersRef.child("accepted").child("attendees").child(attendeeId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Attendee attendee = snapshot.getValue(Attendee.class);
                            if (attendee != null) {
                                attendee.setUserId(attendeeId);
                                attendee.setRegistrationStatus(status); // Add this method or field in Attendee class
                                attendeeList.add(attendee);
                            }
                            remaining[0]--;
                            if (remaining[0] == 0) {
                                updateAttendeeListView();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            remaining[0]--;
                            if (remaining[0] == 0) {
                                updateAttendeeListView();
                            }
                        }
                    });
        }
    }

    // Updates the ListView with the list of attendees
    private void updateAttendeeListView() {
        adapter = new AttendeeListAdapter(this, attendeeList);
        lvAttendees.setAdapter(adapter);
    }

    // Approves an individual registration for a specified attendee

    public void approveRegistration(String attendeeId) {
        event.addAttendeeRegistration(attendeeId, "accepted");
        eventsRef.child("attendeeRegistrations").setValue(event.getAttendeeRegistrations())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Registration approved.", Toast.LENGTH_SHORT).show();
                        fetchAttendeeRegistrations();
                    } else {
                        Toast.makeText(this, "Failed to approve registration.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Rejects an individual registration for a specified attendee
    public void rejectRegistration(String attendeeId) {
        event.addAttendeeRegistration(attendeeId, "rejected");
        eventsRef.child("attendeeRegistrations").setValue(event.getAttendeeRegistrations())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Registration rejected.", Toast.LENGTH_SHORT).show();
                        fetchAttendeeRegistrations();
                    } else {
                        Toast.makeText(this, "Failed to reject registration.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Approves all pending registrations for the event
    private void approveAllRegistrations() {
        if (attendeeList.isEmpty()) {
            Toast.makeText(this, "No pending registrations to approve.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Approve each attendee's registration
        for (Attendee attendee : attendeeList) {
            event.addAttendeeRegistration(attendee.getUserId(), "accepted");
        }

        eventsRef.child("attendeeRegistrations").setValue(event.getAttendeeRegistrations())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "All registrations approved.", Toast.LENGTH_SHORT).show();
                        fetchAttendeeRegistrations();
                    } else {
                        Toast.makeText(this, "Failed to approve registrations.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Deletes the event from the database
//    private void deleteEvent() {
//        // For now, allow deletion
//        eventsRef.removeValue().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(this, "Event deleted.", Toast.LENGTH_SHORT).show();
//                finish();
//            } else {
//                Toast.makeText(this, "Failed to delete event.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    // Deletes the event from the database
    private void deleteEvent() {
        // Check if there are any approved attendees
        DatabaseReference attendeeRegistrationsRef = eventsRef.child("attendeeRegistrations");

        attendeeRegistrationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean hasApprovedAttendees = false;

                // Iterate through attendee registrations
                for (DataSnapshot attendeeSnapshot : snapshot.getChildren()) {
                    String status = attendeeSnapshot.getValue(String.class);
                    if ("accepted".equalsIgnoreCase(status)) {
                        hasApprovedAttendees = true;
                        break;
                    }
                }

                // If there are approved attendees, show a message and prevent deletion
                if (hasApprovedAttendees) {
                    Toast.makeText(EventDetailActivity.this, "Cannot delete event with approved attendees.", Toast.LENGTH_SHORT).show();
                } else {
                    // No approved attendees, proceed with deletion
                    eventsRef.removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(EventDetailActivity.this, "Event deleted.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EventDetailActivity.this, "Failed to delete event.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(EventDetailActivity.this, "Failed to check attendee registrations.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}