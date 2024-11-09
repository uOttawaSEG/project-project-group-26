package com.example.eventmanagementsystemems;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.entities.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {

    private EditText etTitle, etDescription, etDate, etStartTime, etEndTime, etAddress;
    private CheckBox cbManualApproval;
    private Button btnCreateEvent;

    private Calendar selectedDate;
    private String organizerId;

    private DatabaseReference eventsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        FirebaseApp.initializeApp(this);
        organizerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etStartTime = findViewById(R.id.etStartTime);
        etEndTime = findViewById(R.id.etEndTime);
        etAddress = findViewById(R.id.etAddress);
        cbManualApproval = findViewById(R.id.cbManualApproval);
        btnCreateEvent = findViewById(R.id.btnCreateEvent);

        selectedDate = Calendar.getInstance();

        etDate.setOnClickListener(view -> showDatePicker());
        etStartTime.setOnClickListener(view -> showTimePicker(etStartTime));
        etEndTime.setOnClickListener(view -> showTimePicker(etEndTime));

        btnCreateEvent.setOnClickListener(view -> handleCreateEvent());
    }

    private void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    selectedDate.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    etDate.setText(sdf.format(selectedDate.getTime()));
                },
                now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(now.getTimeInMillis()); // Disable past dates
        datePickerDialog.show();
    }

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
                    timeField.setText(time);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void handleCreateEvent() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String startTime = etStartTime.getText().toString().trim();
        String endTime = etEndTime.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        boolean manualApproval = cbManualApproval.isChecked();

        // Validation
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(date)
                || TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate that start time is before end time
        if (!isValidTimeRange(startTime, endTime)) {
            Toast.makeText(this, "Start time must be before end time", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create event ID
        String eventId = eventsRef.push().getKey();

        // Create Event object
        Event event = new Event(eventId, title, description, date, startTime, endTime, address, organizerId, manualApproval);

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

    private boolean isValidTimeRange(String startTime, String endTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            long start = sdf.parse(startTime).getTime();
            long end = sdf.parse(endTime).getTime();
            return start < end;
        } catch (Exception e) {
            return false;
        }
    }
}