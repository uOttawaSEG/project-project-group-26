package com.example.eventmanagementsystemems;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.example.eventmanagementsystemems.notifications.ReminderManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.entities.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

// CreateEventActivity allows organizers to create a new event by entering event details and saving them to Firebase

public class CreateEventActivity extends AppCompatActivity {

    // UI elements for entering event details and creating the event

    private EditText etTitle, etDescription, etDate, etStartTime, etEndTime, etAddress;
    private CheckBox cbManualApproval;
    private Button btnCreateEvent;

    // Calendar object to hold selected date information

    private Calendar selectedDate;
    // Organizer ID for associating the event with the organizer creating it

    private String organizerId;

    // Firebase database reference for storing events
    private DatabaseReference eventsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Initialize Firebase and get the current organizer's ID
        FirebaseApp.initializeApp(this);
        organizerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        // Create notification channel for reminders
        ReminderManager.createNotificationChannel(this);

        // Initialize UI elements
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etStartTime = findViewById(R.id.etStartTime);
        etEndTime = findViewById(R.id.etEndTime);
        etAddress = findViewById(R.id.etAddress);
        cbManualApproval = findViewById(R.id.cbManualApproval);
        btnCreateEvent = findViewById(R.id.btnCreateEvent);

        selectedDate = Calendar.getInstance(); // Initialize the calendar for date selection

        // Set click listeners for date and time fields
        etDate.setOnClickListener(view -> showDatePicker());
        etStartTime.setOnClickListener(view -> showTimePicker(etStartTime));
        etEndTime.setOnClickListener(view -> showTimePicker(etEndTime));

        // Set click listener for the create event button

        btnCreateEvent.setOnClickListener(view -> handleCreateEvent());
    }

    // Opens a date picker dialog to select the event date
    private void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Set selected date
                    selectedDate.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    etDate.setText(sdf.format(selectedDate.getTime()));// Display selected date
                },
                now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(now.getTimeInMillis()); // Disable past dates
        datePickerDialog.show();
    }

    // Opens a time picker dialog to select the start or end time, rounding to the nearest 30 minutes
    private void showTimePicker(EditText timeField) {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        // Round to nearest 30 minutes
        minute = minute >= 30 ? 30 : 0;

        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEventActivity.this,
                (view, selectedHour, selectedMinute) -> {
                    // Only allow 30-minute increments
                    if (selectedMinute != 0 && selectedMinute != 30) {
                        selectedMinute = selectedMinute < 15 ? 0 : 30;
                    }
                    String time = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                    timeField.setText(time);// Display selected time
                }, hour, minute, true);
        timePickerDialog.show();
    }

    // Handles the event creation process, validating input and saving to Firebase
    private void handleCreateEvent() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String startTime = etStartTime.getText().toString().trim();
        String endTime = etEndTime.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        boolean manualApproval = cbManualApproval.isChecked();

        // Validate that all fields are filled
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(date)
                || TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ensure that start time is before end time
        if (!isValidTimeRange(startTime, endTime)) {
            Toast.makeText(this, "Start time must be before end time", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create event ID
        String eventId = eventsRef.push().getKey();

        // Create Event object
        Event event = new Event(eventId, title, description, date, startTime, endTime, address, organizerId, manualApproval);

        // Log event creation
        Log.d("CreateEventActivity", "Event created: " + event);

        // Save to database
        eventsRef.child(eventId).setValue(event)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CreateEventActivity.this, "Event created successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close activity

                    } else {
                        Toast.makeText(CreateEventActivity.this, "Failed to create event", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // Checks if the start time is before the end time
    private boolean isValidTimeRange(String startTime, String endTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            long start = sdf.parse(startTime).getTime();
            long end = sdf.parse(endTime).getTime();
            return start < end; // Valid if start time is before end time
        } catch (Exception e) {
            return false; // Return false if parsing fails
        }
    }
}