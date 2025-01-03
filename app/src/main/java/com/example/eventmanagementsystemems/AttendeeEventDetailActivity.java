package com.example.eventmanagementsystemems;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.entities.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AttendeeEventDetailActivity extends AppCompatActivity {
    private TextView tvEventDetails;
    private TextView tvRegistrationStatus;
    private Button btnRegister;
    private Button btnCancelRegistration;

    private String eventId;
    private DatabaseReference eventRef;
    private FirebaseAuth mAuth;
    private Event event;
    private String attendeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_event_detail);

        tvEventDetails = findViewById(R.id.tvEventDetails);
        tvRegistrationStatus = findViewById(R.id.tvRegistrationStatus);
        btnRegister = findViewById(R.id.btnRegister);
        btnCancelRegistration = findViewById(R.id.btnCancelRegistration);

        eventId = getIntent().getStringExtra("eventId");
        eventRef = FirebaseDatabase.getInstance().getReference("events").child(eventId);
        mAuth = FirebaseAuth.getInstance();
        attendeeId = mAuth.getCurrentUser().getUid();

        fetchEventDetails();

        btnRegister.setOnClickListener(view -> registerForEvent());
        btnCancelRegistration.setOnClickListener(view -> cancelRegistration());
    }

    private void fetchEventDetails() {
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                event = snapshot.getValue(Event.class);

                if (event != null) {
                    StringBuilder details = new StringBuilder();
                    details.append("Title: ").append(event.getTitle()).append("\n")
                            .append("Description: ").append(event.getDescription()).append("\n")
                            .append("Date: ").append(event.getDate()).append("\n")
                            .append("Start Time: ").append(event.getStartTime()).append("\n")
                            .append("End Time: ").append(event.getEndTime()).append("\n")
                            .append("Address: ").append(event.getEventAddress()).append("\n");

                    tvEventDetails.setText(details.toString());

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
                    Toast.makeText(AttendeeEventDetailActivity.this, "Event not found.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AttendeeEventDetailActivity.this, "Failed to fetch event details.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerForEvent() {
        if (event.getAttendeeRegistrations() != null && event.getAttendeeRegistrations().containsKey(attendeeId)) {
            Toast.makeText(this, "You have already registered for this event!", Toast.LENGTH_SHORT).show();
            return;
        }

        hasConflict(event, new ConflictCheckCallback() {
            @Override
            public void onConflictCheckResult(boolean hasConflict) {
                if (hasConflict) {
                    Toast.makeText(AttendeeEventDetailActivity.this, "This event conflicts with another event you are registered for.", Toast.LENGTH_SHORT).show();
                } else {
                    // Proceed with registration
                    String initialStatus = event.isManualApproval() ? "pending" : "accepted";
                    event.addAttendeeRegistration(attendeeId, initialStatus);

                    eventRef.child("attendeeRegistrations").setValue(event.getAttendeeRegistrations())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AttendeeEventDetailActivity.this, "Registration " + initialStatus, Toast.LENGTH_SHORT).show();
                                    btnRegister.setText("Registered (" + initialStatus + ")");
                                    btnRegister.setEnabled(false);
                                    tvRegistrationStatus.setText("Registration Status: " + initialStatus);
                                } else {
                                    Toast.makeText(AttendeeEventDetailActivity.this, "Failed to register for event.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private void cancelRegistration() {
        try {
            // Get the start date and time as a Date object
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

    // Callback interface
    interface ConflictCheckCallback {
        void onConflictCheckResult(boolean hasConflict);
    }

    private void hasConflict(Event newEvent, ConflictCheckCallback callback) {
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");

        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean conflictFound = false;

                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event existingEvent = eventSnapshot.getValue(Event.class);

                    if (existingEvent != null && existingEvent.getAttendeeRegistrations() != null && existingEvent.getAttendeeRegistrations().containsKey(attendeeId)) {
                        String status = existingEvent.getAttendeeRegistrations().get(attendeeId);
                        if (status.equals("accepted") || status.equals("pending")) {
                            if (isOverlapping(newEvent, existingEvent)) {
                                conflictFound = true;
                                break;
                            }
                        }
                    }
                }

                callback.onConflictCheckResult(conflictFound);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AttendeeEventDetailActivity.this, "Failed to fetch registered events.", Toast.LENGTH_SHORT).show();
                callback.onConflictCheckResult(false);
            }
        });
    }

    // Utility method to check time overlap, ensuring events are on the same day
    private boolean isOverlapping(Event event1, Event event2) {
        try {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            Date start1 = dateTimeFormat.parse(event1.getDate() + " " + event1.getStartTime());
            Date end1 = dateTimeFormat.parse(event1.getDate() + " " + event1.getEndTime());
            Date start2 = dateTimeFormat.parse(event2.getDate() + " " + event2.getStartTime());
            Date end2 = dateTimeFormat.parse(event2.getDate() + " " + event2.getEndTime());

            // Check for overlap in time
            return start1.before(end2) && start2.before(end1);
        } catch (ParseException e) {
            e.printStackTrace();
            return false; // If parsing fails, assume no overlap
        }
    }
}