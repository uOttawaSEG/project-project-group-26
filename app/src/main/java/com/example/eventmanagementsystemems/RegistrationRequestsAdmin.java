package com.example.eventmanagementsystemems;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.entities.Attendee;
import com.example.eventmanagementsystemems.entities.Organizer;
import com.example.eventmanagementsystemems.entities.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistrationRequestsAdmin extends AppCompatActivity {
    // Firebase database reference
    DatabaseReference usersRef;
    DatabaseReference attendeesRef;
    DatabaseReference organizersRef;

    // List to store the attendee and organizer objects
    ArrayList<User> attendeeRequests = new ArrayList<>();
    ArrayList<User> organizerRequests = new ArrayList<>();

    // UI Components
    ListView attendeeListView;
    ListView organizerListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_requests_admin);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Get Firebase database reference
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Reference for the attendees node
        attendeesRef = usersRef.child("attendees");

        // Reference for the organizers node
        organizersRef = usersRef.child("organizers");

        // Bind the ListView components from the UI
        attendeeListView = findViewById(R.id.attendeeListView);
        organizerListView = findViewById(R.id.organizerListView);

        // Fetch attendees
        retrieveAttendees();

        // Fetch organizers
        retrieveOrganizers();
    }

    // Method to fetch attendees
    private void retrieveAttendees() {
        attendeesRef.orderByChild("status").equalTo(0).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot attendeeData) {
                attendeeRequests.clear(); // clear the list
                for (DataSnapshot snapshot : attendeeData.getChildren()) {
                    // Retrieve values from Firebase snapshot
                    String firstName = snapshot.child("firstName").getValue(String.class);
                    String lastName = snapshot.child("lastName").getValue(String.class);
                    String emailAddress = snapshot.child("email").getValue(String.class);
                    String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    int status = snapshot.child("status").getValue(Integer.class);
                    String userId = snapshot.getKey();

                    // Create an Attendee object
                    Attendee attendee = new Attendee(firstName, lastName, emailAddress, "", phoneNumber, address);
                    attendee.setStatus(status);
                    attendee.setUserId(userId);

                    // Add to the attendeeRequests list
                    attendeeRequests.add(attendee);
                }

                // Create the adapter and set it to the ListView
                RegistrationRequestAdapter adapter = new RegistrationRequestAdapter(RegistrationRequestsAdmin.this,
                        attendeeRequests, new RegistrationRequestAdapter.OnRequestActionListener() {
                    @Override
                    public void onApprove(User user) {
                        // Handle approve action
                        updateUserStatus("attendees", user.getUserId(), 1);
                    }

                    @Override
                    public void onReject(User user) {
                        // Handle reject action
                        updateUserStatus("attendees", user.getUserId(), 2);
                    }
                });

                attendeeListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "Failed to read attendees.", databaseError.toException());
            }
        });
    }

    // Method to fetch organizers
    private void retrieveOrganizers() {
        organizersRef.orderByChild("status").equalTo(0).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot organizerData) {
                organizerRequests.clear(); // clear the list
                for (DataSnapshot snapshot : organizerData.getChildren()) {
                    // Retrieve values from Firebase snapshot
                    String firstName = snapshot.child("firstName").getValue(String.class);
                    String lastName = snapshot.child("lastName").getValue(String.class);
                    String emailAddress = snapshot.child("email").getValue(String.class);
                    String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String organizationName = snapshot.child("organizationName").getValue(String.class);
                    int status = snapshot.child("status").getValue(Integer.class);
                    String userId = snapshot.getKey();

                    // Create an Organizer object
                    Organizer organizer = new Organizer(firstName, lastName, emailAddress, "", phoneNumber, address, organizationName);
                    organizer.setStatus(status);
                    organizer.setUserId(userId);

                    // Add to the organizerRequests list
                    organizerRequests.add(organizer);
                }

                // Create the adapter and set it to the ListView
                RegistrationRequestAdapter adapter = new RegistrationRequestAdapter(RegistrationRequestsAdmin.this,
                        organizerRequests, new RegistrationRequestAdapter.OnRequestActionListener() {
                    @Override
                    public void onApprove(User user) {
                        // Handle approve action
                        updateUserStatus("organizers", user.getUserId(), 1);
                    }

                    @Override
                    public void onReject(User user) {
                        // Handle reject action
                        updateUserStatus("organizers", user.getUserId(), 2);
                    }
                });

                organizerListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "Failed to read organizers.", databaseError.toException());
            }
        });
    }

    // Method to update user status
    private void updateUserStatus(String userType, String userId, int newStatus) {
        DatabaseReference userRef = usersRef.child(userType).child(userId);
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", newStatus);
        userRef.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RegistrationRequestsAdmin.this, "User status updated", Toast.LENGTH_SHORT).show();
                // Refresh the lists
                retrieveAttendees();
                retrieveOrganizers();
            } else {
                Toast.makeText(RegistrationRequestsAdmin.this, "Failed to update user status", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
