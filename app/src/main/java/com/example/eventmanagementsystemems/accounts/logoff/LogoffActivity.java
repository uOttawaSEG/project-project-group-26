//Since we literally no longer have the welcome screen, im going to move the logoff button to the actual regular screen

package com.example.eventmanagementsystemems.accounts.logoff;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LogoffActivity extends AppCompatActivity  {
//Similar to LoginActivity class
    //private Button btnLogoff;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Link btnLogoff to the button in your layout
        //btnLogoff = findViewById(R.id.btnLogoff);

        // Set an OnClickListener to handle the logoff action
        //btnLogoff.setOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View view) {
                handleLogoff();  // Call the handleLogoff method when the button is clicked
            //}
        //});
    }
    private void handleLogoff(){
        mAuth.signOut();
        
        Toast.makeText(this, "Logged off succesfully!", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}