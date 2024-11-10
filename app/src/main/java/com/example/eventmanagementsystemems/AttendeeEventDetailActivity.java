package com.example.eventmanagementsystemems;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.eventmanagementsystemems.entities.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class AttendeeEventDetailActivity extends AppCompatActivity {

    private TextView tvEventDetails;
    private TextView tvRegistrationStatus;
    private Button btnRegister;

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

        eventId = getIntent().getStringExtra("eventId");
        eventRef = FirebaseDatabase.getInstance().getReference("events").child(eventId);
        mAuth = FirebaseAuth.getInstance();
        attendeeId = mAuth.getCurrentUser().getUid();

        fetchEventDetails();

        btnRegister.setOnClickListener(view -> registerForEvent());
    }

    private void fetchEventDetails() {
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                event = snapshot.getValue(Event.class);

                if (event != null) {
                    StringBuilder details = new StringBuilder();
                    details.append("Title: ").append(event.getTitle()).append("\n");
                    details.append("Description: ").append(event.getDescription()).append("\n");
                    details.append("Date: ").append(event.getDate()).append("\n");
                    details.append("Start Time: ").append(event.getStartTime()).append("\n");
                    details.append("End Time: ").append(event.getEndTime()).append("\n");
                    details.append("Address: ").append(event.getEventAddress()).append("\n");

                    tvEventDetails.setText(details.toString());

                    String status = "Not Registered";
                    if (event.getAttendeeRegistrations() != null && event.getAttendeeRegistrations().containsKey(attendeeId)) {
                        status = "Registration Status: " + event.getAttendeeRegistrations().get(attendeeId);
                        btnRegister.setText("Registered (" + event.getAttendeeRegistrations().get(attendeeId) + ")");
                        btnRegister.setEnabled(false);
                    }
                    tvRegistrationStatus.setText(status);
                } else {
                    Toast.makeText(AttendeeEventDetailActivity.this, "Event not found.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AttendeeEventDetailActivity.this, "Failed to fetch event details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerForEvent() {
        // Check if already registered
        if (event.getAttendeeRegistrations() != null && event.getAttendeeRegistrations().containsKey(attendeeId)) {
            Toast.makeText(this, "You have already registered for this event!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add registration
        String initialStatus = event.isManualApproval() ? "pending" : "accepted";

        event.addAttendeeRegistration(attendeeId, initialStatus);

        eventRef.child("attendeeRegistrations").setValue(event.getAttendeeRegistrations())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Registration " + initialStatus, Toast.LENGTH_SHORT).show();
                        btnRegister.setText("Registered (" + initialStatus + ")");
                        btnRegister.setEnabled(false);
                        tvRegistrationStatus.setText("Registration Status: " + initialStatus);
                    } else {
                        Toast.makeText(this, "Failed to register for event.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}