// Event.java
package com.example.eventmanagementsystemems.entities;

import java.util.HashMap;

public class Event {
    private String eventId;
    private String title;
    private String description;
    private String date;
    private String startTime;
    private String endTime;
    private String eventAddress;
    private String organizerId;
    private boolean manualApproval;
    private HashMap<String, String> attendeeRegistrations;

    // Default constructor required for calls to DataSnapshot.getValue(Event.class)
    public Event() {
    }

    public Event(String eventId, String title, String description, String date, String startTime,
                 String endTime, String eventAddress, String organizerId, boolean manualApproval) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventAddress = eventAddress;
        this.organizerId = organizerId;
        this.manualApproval = manualApproval;
        this.attendeeRegistrations = new HashMap<>();
    }

    // Getters and setters for all fields
    // ... (Include getters and setters for all attributes)

    public void addAttendeeRegistration(String attendeeId, String status) {
        attendeeRegistrations.put(attendeeId, status);
    }

    public HashMap<String, String> getAttendeeRegistrations() {
        return attendeeRegistrations;
    }
}
