package com.example.eventmanagementsystemems.accounts.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.R;
import com.example.eventmanagementsystemems.PendingRequestsActivity;
import com.example.eventmanagementsystemems.RejectedRequestsActivity;

public class AdminHomeActivity extends AppCompatActivity {

    private Button btnPendingRequests;
    private Button btnRejectedRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        btnPendingRequests = findViewById(R.id.btnPendingRequests);
        btnRejectedRequests = findViewById(R.id.btnRejectedRequests);

        btnPendingRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, PendingRequestsActivity.class);
                startActivity(intent);
            }
        });

        btnRejectedRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, RejectedRequestsActivity.class);
                startActivity(intent);
            }
        });
    }
}