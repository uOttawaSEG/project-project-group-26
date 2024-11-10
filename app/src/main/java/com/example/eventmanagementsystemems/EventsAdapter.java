package com.example.eventmanagementsystemems;

import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.*;

import com.example.eventmanagementsystemems.entities.Event;

import java.util.List;

public class EventsAdapter extends ArrayAdapter<Event> {

    private Context context;
    private List<Event> eventsList;

    public EventsAdapter(Context context, List<Event> eventsList) {
        super(context, R.layout.event_list_item, eventsList);
        this.context = context;
        this.eventsList = eventsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Event event = eventsList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.event_list_item, parent, false);
        }

        TextView title = convertView.findViewById(R.id.eventTitle);
        TextView description = convertView.findViewById(R.id.eventDescription);
        Button btnViewDetails = convertView.findViewById(R.id.btnViewDetails);

        title.setText(event.getTitle());
        description.setText(event.getDescription());

        btnViewDetails.setOnClickListener(view -> {
            Intent intent = new Intent(context, AttendeeEventDetailActivity.class);
            intent.putExtra("eventId", event.getEventId());
            context.startActivity(intent);
        });

        return convertView;
    }
}