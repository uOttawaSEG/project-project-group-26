package com.example.eventmanagementsystemems;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventmanagementsystemems.entities.User;
import com.example.eventmanagementsystemems.entities.Organizer;

import java.util.ArrayList;

public class RegistrationRequestAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<User> userList; // User is the base class
    private final OnRequestActionListener listener;

    public RegistrationRequestAdapter(Context context, ArrayList<User> userList, OnRequestActionListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
    }

    public interface OnRequestActionListener {
        void onApprove(User user);
        void onReject(User user);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // You can use the position as ID or any unique ID
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            // Inflate the layout
            convertView = LayoutInflater.from(context).inflate(R.layout.item_registration_request, parent, false);
            holder = new ViewHolder();
            holder.tvUserName = convertView.findViewById(R.id.tvUserName);
            holder.tvUserDetails = convertView.findViewById(R.id.tvUserDetails);
            holder.btnApprove = convertView.findViewById(R.id.btnApprove);
            holder.btnReject = convertView.findViewById(R.id.btnReject);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User user = userList.get(position);

        holder.tvUserName.setText(user.getFirstName() + " " + user.getLastName());

        StringBuilder details = new StringBuilder();
        details.append("Email: ").append(user.getEmailAddress()).append("\n");
        details.append("Phone: ").append(user.getPhoneNumber()).append("\n");
        details.append("Address: ").append(user.getAddress()).append("\n");
        if (user instanceof Organizer) {
            details.append("Organization: ").append(((Organizer) user).getOrganizationName()).append("\n");
        }

        holder.tvUserDetails.setText(details.toString());

        holder.btnApprove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onApprove(user);
            }
        });

        holder.btnReject.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReject(user);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tvUserName;
        TextView tvUserDetails;
        Button btnApprove;
        Button btnReject;
    }
}
