package com.example.eventmanagementsystemems;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.eventmanagementsystemems.entities.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

// Activity that displays event details for an attendee
// and allows the attendee to register for the event if not already registered.
public class AttendeeEventDetailActivity extends AppCompatActivity {
    // UI elements responsible for displaying event details and registration status
    private TextView tvEventDetails;
    private TextView tvRegistrationStatus;
    private Button btnRegister;

    // Variables to store event information and references to Firebase
    private String eventId;
    private DatabaseReference eventRef;
    private FirebaseAuth mAuth;

    // Event data and attendee ID for checking registration status
    private Event event;
    private String attendeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_event_detail);

        tvEventDetails = findViewById(R.id.tvEventDetails);
        tvRegistrationStatus = findViewById(R.id.tvRegistrationStatus);
        btnRegister = findViewById(R.id.btnRegister);

        // Retrieve event ID from the intent and initialize Firebase references
        eventId = getIntent().getStringExtra("eventId");
        eventRef = FirebaseDatabase.getInstance().getReference("events").child(eventId);
        mAuth = FirebaseAuth.getInstance();
        attendeeId = mAuth.getCurrentUser().getUid();

        fetchEventDetails();

        // Set an onClickListener for the registration button
        btnRegister.setOnClickListener(view -> registerForEvent());
    }

    // Fetches the event details from the database and updates the UI
    private void fetchEventDetails() {
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Retrieve the Event object from the database snapshot
                event = snapshot.getValue(Event.class);

                if (event != null) {
                    // Build and display event details string
                    StringBuilder details = new StringBuilder();
                    details.append("Title: ").append(event.getTitle()).append("\n");
                    details.append("Description: ").append(event.getDescription()).append("\n");
                    details.append("Date: ").append(event.getDate()).append("\n");
                    details.append("Start Time: ").append(event.getStartTime()).append("\n");
                    details.append("End Time: ").append(event.getEndTime()).append("\n");
                    details.append("Address: ").append(event.getEventAddress()).append("\n");

                    tvEventDetails.setText(details.toString());

                    // Set registration status based on attendee's registration
                    String status = "Not Registered";
                    if (event.getAttendeeRegistrations() != null && event.getAttendeeRegistrations().containsKey(attendeeId)) {
                        status = "Registration Status: " + event.getAttendeeRegistrations().get(attendeeId);
                        btnRegister.setText("Registered (" + event.getAttendeeRegistrations().get(attendeeId) + ")");
                        btnRegister.setEnabled(false);
                    }
                    tvRegistrationStatus.setText(status);
                } else {
                    // Show an error message and close the activity if the event is not found
                    Toast.makeText(AttendeeEventDetailActivity.this, "Event not found.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle database access errors
                Toast.makeText(AttendeeEventDetailActivity.this, "Failed to fetch event details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Registers the attendee for the event, if not already registered
    private void registerForEvent() {
        // Check if the attendee is already registered
        if (event.getAttendeeRegistrations() != null && event.getAttendeeRegistrations().containsKey(attendeeId)) {
            Toast.makeText(this, "You have already registered for this event!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Determine initial registration status based on event approval requirements
        String initialStatus = event.isManualApproval() ? "pending" : "accepted";

        // Register the attendee in the event's registration map
        event.addAttendeeRegistration(attendeeId, initialStatus);

        // Update registration information in the Firebase database
        eventRef.child("attendeeRegistrations").setValue(event.getAttendeeRegistrations())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Notify the user and update the UI upon successful registration
                        Toast.makeText(this, "Registration " + initialStatus, Toast.LENGTH_SHORT).show();
                        btnRegister.setText("Registered (" + initialStatus + ")");
                        btnRegister.setEnabled(false);
                        tvRegistrationStatus.setText("Registration Status: " + initialStatus);
                    } else {
                        // Display an error if registration fails
                        Toast.makeText(this, "Failed to register for event.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}