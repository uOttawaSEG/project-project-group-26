package com.example.eventmanagementsystemems.accounts.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.database.DatabaseHelper;
import com.example.eventmanagementsystemems.entities.User;
import com.example.eventmanagementsystemems.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etPassword, etPhone;
    private RadioGroup rgUserType;
    private RadioButton rbAttendee, rbOrganizer;
    private Button btnSignup;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI elements
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        rgUserType = findViewById(R.id.rgUserType);
        rbAttendee = findViewById(R.id.rbAttendee);
        rbOrganizer = findViewById(R.id.rbOrganizer);
        btnSignup = findViewById(R.id.btnSignup);

        dbHelper = new DatabaseHelper(this);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignup();
            }
        });
    }

    private void handleSignup() {
        // Retrieve user input
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String emailAddress = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String phoneNumber = etPhone.getText().toString().trim();
        String userType = rbAttendee.isChecked() ? "Attendee" : "Organizer";

        // Input validation
        if (firstName.isEmpty() || lastName.isEmpty() || emailAddress.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.isEmailExists(emailAddress)) {
            Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new User object
        User user = new User(firstName, lastName, emailAddress, password, phoneNumber, userType);

        // Add user to database
        boolean isInserted = dbHelper.addUser(user);
        if (isInserted) {
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
            // Redirect to login or main activity
            finish();
        } else {
            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
        }
    }
}