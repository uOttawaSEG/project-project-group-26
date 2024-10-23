package com.example.eventmanagementsystemems;//package com.example.eventmanagementsystemems;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ArrayAdapter;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.eventmanagementsystemems.entities.Attendee;
//import com.example.eventmanagementsystemems.entities.Organizer;
//import com.example.eventmanagementsystemems.entities.User;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//public class RegistrationRequestsAdmin extends AppCompatActivity {
//    // Firebase database reference
//    DatabaseReference usersRef;
//    DatabaseReference attendeesRef;
//    DatabaseReference organizersRef;
//
//    // List to store the attendee and organizer objects
//    ArrayList<Attendee> attendeeRequests = new ArrayList<>();
//    ArrayList<Organizer> organizerRequests = new ArrayList<>();
//
//    User[] attendeeList = new User[50];
//    User[] organizerList = new User[50];
//
//    ArrayAdapter ad;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main); // Replace with your activity layout if needed
//
//        // Initialize Firebase
//        FirebaseApp.initializeApp(this);
//
//        // Get Firebase database reference
//        usersRef = FirebaseDatabase.getInstance().getReference("users");
//
//        // Reference for the attendees node
//        attendeesRef = usersRef.child("attendees");
//
//        // Reference for the organizers node
//        organizersRef = usersRef.child("organizers");
//
//        // Fetch attendees
//        retrieveAttendees();
//
//        // Fetch organizers
//        retrieveOrganizers();
//
//
//
//    }
//
//    // Method to fetch attendees
//    private void retrieveAttendees() {
//        attendeesRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot attendeeData) {
//                    for (DataSnapshot snapshot : attendeeData.getChildren()) {
//                        // Retrieve values from Firebase snapshot
//                        String firstName = snapshot.child("firstName").getValue(String.class);
//                        String lastName = snapshot.child("lastName").getValue(String.class);
//                        String emailAddress = snapshot.child("emailAddress").getValue(String.class);
//                        String password = snapshot.child("password").getValue(String.class);
//                        String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
//                        String address = snapshot.child("address").getValue(String.class);
//
//                        // Ensure no null values are passed to the constructor
////                        if (firstName != null && lastName != null && emailAddress != null &&
////                                password != null && phoneNumber != null && address != null) {
//
//                            // Create an Attendee object
//                            Attendee attendee = new Attendee(firstName, lastName, emailAddress, password, phoneNumber, address);
//
//                            // Add to the attendeeRequests list
//                            attendeeRequests.add(attendee);
////                        } else {
////                            Log.w("TAG", "Missing required fields for Attendee");
//                        }
//                    }
//             //       Log.d("TAG", "Attendee List: " + attendeeRequests);
//            //    } else {
////                    Log.d("TAG", "No attendees found.");
////                }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w("TAG", "Failed to read attendees.", databaseError.toException());
//            }
//        });
//
//    }
//
//    // Method to fetch organizers
//    private void retrieveOrganizers() {
//        organizersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot organizerData) {
//                    for (DataSnapshot snapshot : organizerData.getChildren()) {
//                        // Retrieve values from Firebase snapshot
//                        String firstName = snapshot.child("firstName").getValue(String.class);
//                        String lastName = snapshot.child("lastName").getValue(String.class);
//                        String emailAddress = snapshot.child("emailAddress").getValue(String.class);
//                        String password = snapshot.child("password").getValue(String.class);
//                        String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
//                        String address = snapshot.child("address").getValue(String.class);
//                        String organizationName = snapshot.child("organizationName").getValue(String.class);
//
//                        // Ensure no null values are passed to the constructor
//
//                            // Create an Organizer object
//                            Organizer organizer = new Organizer(firstName, lastName, emailAddress, password, phoneNumber, address, organizationName);
//
//                            // Add to the organizerRequests list
//                            organizerRequests.add(organizer);
//
//                            //Log.w("TAG", "Missing required fields for Organizer");
//                        }
//                    }
//                    //Log.d("TAG", "Organizer List: " + organizerRequests);
//                //} else {
//                   // Log.d("TAG", "No organizers found.");
//                //}
//            //}
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w("TAG", "Failed to read organizers.", databaseError.toException());
//            }
//        });
//    }
//}

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.entities.Attendee;
import com.example.eventmanagementsystemems.entities.Organizer;
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
    ArrayList<Attendee> attendeeRequests = new ArrayList<>();
    ArrayList<Organizer> organizerRequests = new ArrayList<>();

    // UI Components
    ListView attendeeListView;
    ListView organizerListView;

    // ArrayAdapters for attendees and organizers
    ArrayAdapter<String> attendeeAdapter;
    ArrayAdapter<String> organizerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_requests_admin); // Replace with your activity layout if needed

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
        attendeesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot attendeeData) {
                for (DataSnapshot snapshot : attendeeData.getChildren()) {
                    // Retrieve values from Firebase snapshot
                    String firstName = snapshot.child("firstName").getValue(String.class);
                    String lastName = snapshot.child("lastName").getValue(String.class);
                    String emailAddress = snapshot.child("emailAddress").getValue(String.class);
                    String password = snapshot.child("password").getValue(String.class);
                    String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);

                    // Create an Attendee object
                    Attendee attendee = new Attendee(firstName, lastName, emailAddress, password, phoneNumber, address);

                    // Add to the attendeeRequests list
                    attendeeRequests.add(attendee);
                }

                // Update the ListView with attendee data
                ArrayList<String> attendeeNames = new ArrayList<>();
                for (Attendee attendee : attendeeRequests) {
                    attendeeNames.add(attendee.getFirstName() + " " + attendee.getLastName());
                }

                // Create the adapter and set it to the ListView
                attendeeAdapter = new ArrayAdapter<>(RegistrationRequestsAdmin.this,
                        android.R.layout.simple_list_item_1, attendeeNames);
                attendeeListView.setAdapter(attendeeAdapter);
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
                for (DataSnapshot snapshot : organizerData.getChildren()) {
                    // Retrieve values from Firebase snapshot
                    String firstName = snapshot.child("firstName").getValue(String.class);
                    String lastName = snapshot.child("lastName").getValue(String.class);
                    String emailAddress = snapshot.child("emailAddress").getValue(String.class);
                    String password = snapshot.child("password").getValue(String.class);
                    String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String organizationName = snapshot.child("organizationName").getValue(String.class);

                    // Create an Organizer object
                    Organizer organizer = new Organizer(firstName, lastName, emailAddress, password, phoneNumber, address, organizationName);

                    // Add to the organizerRequests list
                    organizerRequests.add(organizer);
                }

                // Update the ListView with organizer data
                ArrayList<String> organizerNames = new ArrayList<>();
                for (Organizer organizer : organizerRequests) {
                    organizerNames.add(organizer.getFirstName() + " " + organizer.getLastName() + " (" + organizer.getOrganizationName() + ")");
                }

                // Create the adapter and set it to the ListView
                organizerAdapter = new ArrayAdapter<>(RegistrationRequestsAdmin.this,
                        android.R.layout.simple_list_item_1, organizerNames);
                organizerListView.setAdapter(organizerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "Failed to read organizers.", databaseError.toException());
            }
        });
    }
}


