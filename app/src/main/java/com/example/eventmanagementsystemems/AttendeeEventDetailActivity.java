package com.example.eventmanagementsystemems;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.eventmanagementsystemems.entities.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.text.ParseException;
import java.util.Date;


// Activity that displays event details for an attendee
// and allows the attendee to register for the event if not already registered.
public class AttendeeEventDetailActivity extends AppCompatActivity {
    // UI elements responsible for displaying event details and registration status
    private TextView tvEventDetails;
    private TextView tvRegistrationStatus;
    private Button btnRegister;

    private Button btnCancelRegistration;

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
        btnCancelRegistration = findViewById(R.id.btnCancelRegistration); // Ensure this exists in your layout


        // Retrieve event ID from the intent and initialize Firebase references
        eventId = getIntent().getStringExtra("eventId");
        eventRef = FirebaseDatabase.getInstance().getReference("events").child(eventId);
        mAuth = FirebaseAuth.getInstance();
        attendeeId = mAuth.getCurrentUser().getUid();

        fetchEventDetails();

        // Set an onClickListener for the registration button
        btnRegister.setOnClickListener(view -> registerForEvent());
        btnCancelRegistration.setOnClickListener(view -> cancelRegistration());

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
                        btnCancelRegistration.setEnabled(true);

                    } else {
                        btnCancelRegistration.setEnabled(false);
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

//    private void cancelRegistration() {
//        long timeUntilStart = event.getStartTime().getTime() - System.currentTimeMillis();
//
//        if (timeUntilStart < 24 * 60 * 60 * 1000) {
//            Toast.makeText(this, "Cannot cancel registrations within 24 hours of the event.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (!event.getAttendeeRegistrations().get(attendeeId).equalsIgnoreCase("pending") &&
//                !event.getAttendeeRegistrations().get(attendeeId).equalsIgnoreCase("accepted")) {
//            Toast.makeText(this, "Only pending or accepted registrations can be canceled.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        event.getAttendeeRegistrations().remove(attendeeId);
//
//        eventRef.child("attendeeRegistrations").setValue(event.getAttendeeRegistrations())
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(this, "Registration canceled successfully.", Toast.LENGTH_SHORT).show();
//                        btnRegister.setEnabled(true);
//                        btnRegister.setText("Register");
//                        btnCancelRegistration.setEnabled(false);
//                        tvRegistrationStatus.setText("Not Registered");
//                    } else {
//                        Toast.makeText(this, "Failed to cancel registration.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    private void cancelRegistration() {
        try {
            // Get the start date and time as a `Date` object
            Date eventStartDateTime = event.getStartDateTime();
            long timeUntilStart = eventStartDateTime.getTime() - System.currentTimeMillis();

            // Check if the event starts in less than 24 hours
            if (timeUntilStart < 24 * 60 * 60 * 1000) {
                Toast.makeText(this, "Cannot cancel registrations within 24 hours of the event.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the registration status is valid for cancellation
            String registrationStatus = event.getRegistrationStatus(attendeeId);
            if (!"pending".equalsIgnoreCase(registrationStatus) && !"accepted".equalsIgnoreCase(registrationStatus)) {
                Toast.makeText(this, "Only pending or accepted registrations can be canceled.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Remove the attendee's registration from the event
            event.removeAttendeeRegistration(attendeeId);

            // Update the Firebase database
            eventRef.child("attendeeRegistrations").setValue(event.getAttendeeRegistrations())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Registration canceled successfully.", Toast.LENGTH_SHORT).show();
                            btnRegister.setEnabled(true);
                            btnRegister.setText("Register");
                            btnCancelRegistration.setEnabled(false);
                            tvRegistrationStatus.setText("Not Registered");
                        } else {
                            Toast.makeText(this, "Failed to cancel registration.", Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (ParseException e) {
            Toast.makeText(this, "Invalid event date or time format.", Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}