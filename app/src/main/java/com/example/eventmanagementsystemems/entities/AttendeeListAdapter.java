package com.example.eventmanagementsystemems.entities;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventmanagementsystemems.EventDetailActivity;
import com.example.eventmanagementsystemems.R;
import com.example.eventmanagementsystemems.entities.Attendee;

import java.util.ArrayList;

public class AttendeeListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Attendee> attendees;

    public AttendeeListAdapter(Context context, ArrayList<Attendee> attendees) {
        this.context = context;
        this.attendees = attendees;
    }

    @Override
    public int getCount() {
        return attendees.size();
    }

    @Override
    public Object getItem(int position) {
        return attendees.get(position);
    }

    @Override
    public long getItemId(int position) {
        // For simplicity, return position
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Attendee attendee = attendees.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_attendee, parent, false);
        }

        TextView tvAttendeeName = convertView.findViewById(R.id.tvAttendeeName);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);

        tvAttendeeName.setText(attendee.getFirstName() + " " + attendee.getLastName());
        tvStatus.setText("Status: " + attendee.getRegistrationStatus());

        convertView.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(attendee.getFirstName() + " " + attendee.getLastName());

            StringBuilder message = new StringBuilder();
            message.append("Email: ").append(attendee.getEmailAddress()).append("\n");
            message.append("Phone: ").append(attendee.getPhoneNumber()).append("\n");
            message.append("Address: ").append(attendee.getAddress()).append("\n");
            message.append("Registration Status: ").append(attendee.getRegistrationStatus()).append("\n");

            builder.setMessage(message.toString());

            if (attendee.getRegistrationStatus().equals("pending")) {
                builder.setPositiveButton("Approve", (dialog, id) -> {
                    // Approve the registration
                    if (context instanceof EventDetailActivity) {
                        ((EventDetailActivity) context).approveRegistration(attendee.getUserId());
                    }
                });
                builder.setNegativeButton("Reject", (dialog, id) -> {
                    // Reject the registration
                    if (context instanceof EventDetailActivity) {
                        ((EventDetailActivity) context).rejectRegistration(attendee.getUserId());
                    }
                });
            } else {
                builder.setNeutralButton("OK", (dialog, id) -> dialog.dismiss());
            }

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        return convertView;
    }
}