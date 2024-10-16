package com.example.eventmanagementsystemems.accounts.logoff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.R;
import com.example.eventmanagementsystemems.accounts.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LogoffActivity extends AppCompatActivity  {
//Similar to LoginActivity class
    private Button btnLogoff;
    private FirebaseAuth mAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        mAuth = FirebaseAuth.getInstance();
        
        btnLogoff = findViewById(R.id.btnLogoff);
        btnLogoff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                handleLogoff();
            }
        });
    }

    private void handleLogoff(){
        mAuth.signOut();
        
        Toast.makeText(LogoffActivity.this, "Logged off succesfully!", Toast.LENGTH_LONG).show();
        
        Intent intent = new Intent(LogoffActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}
