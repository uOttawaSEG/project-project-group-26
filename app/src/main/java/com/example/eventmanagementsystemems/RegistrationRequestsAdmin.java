package com.example.eventmanagementsystemems;

import android.os.Bundle;
import android.util.Log;
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

        // Reference for the pending attendees and organizers nodes
        attendeesRef = usersRef.child("pending").child("attendees");
        organizersRef = usersRef.child("pending").child("organizers");

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
        attendeesRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    String userId = snapshot.getKey();

                    // Create an Attendee object
                    Attendee attendee = new Attendee(firstName, lastName, emailAddress, phoneNumber, address);
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
                        updateUserStatus("attendees", user.getUserId(), "accepted");
                    }

                    @Override
                    public void onReject(User user) {
                        // Handle reject action
                        updateUserStatus("attendees", user.getUserId(), "rejected");
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
        organizersRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    String userId = snapshot.getKey();

                    // Create an Organizer object
                    Organizer organizer = new Organizer(firstName, lastName, emailAddress, phoneNumber, address, organizationName);
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
                        updateUserStatus("organizers", user.getUserId(), "accepted");
                    }

                    @Override
                    public void onReject(User user) {
                        // Handle reject action
                        updateUserStatus("organizers", user.getUserId(), "rejected");
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

    // Method to update user status by moving them between database sections
    private void updateUserStatus(String userType, String userId, String newSection) {
        DatabaseReference fromRef = usersRef.child("pending").child(userType).child(userId);
        DatabaseReference toRef = usersRef.child(newSection).child(userType).child(userId);

        // Copy data
        fromRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                toRef.setValue(snapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            // Remove from original location
                            fromRef.removeValue();

                            Toast.makeText(RegistrationRequestsAdmin.this, "User moved to " + newSection, Toast.LENGTH_SHORT).show();
                            // Refresh the lists
                            retrieveAttendees();
                            retrieveOrganizers();
                        } else {
                            Toast.makeText(RegistrationRequestsAdmin.this, "Failed to move user", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RegistrationRequestsAdmin.this, "Operation cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}