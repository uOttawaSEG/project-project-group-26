package com.example.eventmanagementsystemems.accounts.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventmanagementsystemems.R;
import com.example.eventmanagementsystemems.PendingRequestsActivity;
import com.example.eventmanagementsystemems.RejectedRequestsActivity;
import com.example.eventmanagementsystemems.accounts.logoff.LogoffActivity;

public class AdminHomeActivity extends AppCompatActivity {

    private Button btnPendingRequests;
    private Button btnRejectedRequests;
    private Button btnLogoff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        btnPendingRequests = findViewById(R.id.btnPendingRequests);
        btnRejectedRequests = findViewById(R.id.btnRejectedRequests);
        //btnLogoff = findViewById(R.id.btnLogoff)
        btnLogoff = findViewById(R.id.btnLogoff);

        btnLogoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open LogoffActivity
                Intent intent = new Intent(AdminHomeActivity.this, LogoffActivity.class);
                startActivity(intent);
            }
        });
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

        /*btnLogoff.setOnClickListener(new View.OnClickListener() {
          /  @Override
            public void onClick(View view) {
                LogoffActivity.handleLogoff();
            }
        });
*/
    }
}