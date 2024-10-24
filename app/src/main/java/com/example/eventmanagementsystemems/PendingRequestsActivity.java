package com.example.eventmanagementsystemems;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class PendingRequestsActivity extends AppCompatActivity {
    // Firebase database reference
    DatabaseReference usersRef;
    DatabaseReference attendeesRef;
    DatabaseReference organizersRef;

    // List to store the attendee and organizer objects
    ArrayList<User> pendingRequests = new ArrayList<>();

    // UI Components
    ListView pendingListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_requests);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Get Firebase database reference
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // References for the pending attendees and organizers nodes
        attendeesRef = usersRef.child("pending").child("attendees");
        organizersRef = usersRef.child("pending").child("organizers");

        // Bind the ListView component from the UI
        pendingListView = findViewById(R.id.pendingListView);

        // Fetch pending requests
        retrievePendingRequests();
    }

    private void retrievePendingRequests() {
        pendingRequests.clear();
        // Fetch attendees
        attendeesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot attendeeData) {
                for (DataSnapshot snapshot : attendeeData.getChildren()) {
                    // Retrieve values from Firebase snapshot
                    String firstName = snapshot.child("firstName").getValue(String.class);
                    String lastName = snapshot.child("lastName").getValue(String.class);
                    String emailAddress = snapshot.child("email").getValue(String.class);
                    String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String userId = snapshot.getKey();

                    // Create an Attendee object
                    Attendee attendee = new Attendee(firstName, lastName, emailAddress, "", phoneNumber, address);
                    attendee.setUserId(userId);

                    // Add to the pendingRequests list
                    pendingRequests.add(attendee);
                }

                // Fetch organizers after attendees are fetched
                fetchOrganizers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "Failed to read attendees.", databaseError.toException());
            }
        });
    }

    private void fetchOrganizers() {
        organizersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot organizerData) {
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
                    Organizer organizer = new Organizer(firstName, lastName, emailAddress, "", phoneNumber, address, organizationName);
                    organizer.setUserId(userId);

                    // Add to the pendingRequests list
                    pendingRequests.add(organizer);
                }

                // Update the ListView with pending requests
                updateListView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "Failed to read organizers.", databaseError.toException());
            }
        });
    }

    private void updateListView() {
        ArrayList<String> userNames = new ArrayList<>();
        for (User user : pendingRequests) {
            if (user instanceof Organizer) {
                Organizer organizer = (Organizer) user;
                userNames.add(organizer.getFirstName() + " " + organizer.getLastName() + " (Organizer)");
            } else if (user instanceof Attendee) {
                Attendee attendee = (Attendee) user;
                userNames.add(attendee.getFirstName() + " " + attendee.getLastName() + " (Attendee)");
            }
        }

        // Create the adapter and set it to the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, userNames);
        pendingListView.setAdapter(adapter);

        // Set item click listener
        pendingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                User selectedUser = pendingRequests.get(position);
                showUserDetailsPopup(selectedUser, "pending");
            }
        });
    }

    private void showUserDetailsPopup(User user, String currentSection) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(user.getFirstName() + " " + user.getLastName());

        StringBuilder message = new StringBuilder();
        message.append("Email: ").append(user.getEmailAddress()).append("\n");
        message.append("Phone: ").append(user.getPhoneNumber()).append("\n");
        message.append("Address: ").append(user.getAddress()).append("\n");

        if (user instanceof Organizer) {
            message.append("Organization: ").append(((Organizer) user).getOrganizationName()).append("\n");
        }

        builder.setMessage(message.toString());

        builder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Move user to accepted
                moveUserBetweenSections(user, currentSection, "accepted");
            }
        });
        builder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Move user to rejected
                moveUserBetweenSections(user, currentSection, "rejected");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void moveUserBetweenSections(User user, String fromSection, String toSection) {
        String userTypePath = user instanceof Organizer ? "organizers" : "attendees";
        DatabaseReference fromRef = usersRef.child(fromSection).child(userTypePath).child(user.getUserId());
        DatabaseReference toRef = usersRef.child(toSection).child(userTypePath).child(user.getUserId());

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

                            Toast.makeText(PendingRequestsActivity.this, "User moved to " + toSection, Toast.LENGTH_SHORT).show();
                            // Refresh the list
                            retrievePendingRequests();
                        } else {
                            Toast.makeText(PendingRequestsActivity.this, "Failed to move user", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PendingRequestsActivity.this, "Operation cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}