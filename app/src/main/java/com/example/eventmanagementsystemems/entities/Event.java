package com.example.eventmanagementsystemems.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Event {
    private String eventId;
    private String title;
    private String description;
    private String date;        // Format: "yyyy-MM-dd"
    private String startTime;    // Format: "HH:mm"
    private String endTime;      // Format: "HH:mm"
    private String eventAddress;
    private String organizerId;
    private boolean manualApproval;
    private HashMap<String, String> attendeeRegistrations;

    // Default constructor required for calls to DataSnapshot.getValue(Event.class)
    public Event() {
        this.attendeeRegistrations = new HashMap<>();
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

    // Getters and Setters for all fields

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Event title cannot be empty.");
        }
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description; // Description can be optional
    }

    public Date getStartDateTime() throws ParseException {
        if (date == null || startTime == null) {
            throw new IllegalStateException("Event date or start time is null.");
        }

        // Define the format for date and time
        String dateTimeString = date + " " + startTime; // Example: "2024-12-01 14:00"
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return dateTimeFormat.parse(dateTimeString);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        // Add validation for date format if necessary
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        // Add validation for time format if necessary
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        // Add validation for time format if necessary
        this.endTime = endTime;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        if (eventAddress == null || eventAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Event address cannot be empty.");
        }
        this.eventAddress = eventAddress;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        if (organizerId == null || organizerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Organizer ID cannot be empty.");
        }
        this.organizerId = organizerId;
    }

    public boolean isManualApproval() {
        return manualApproval;
    }

    public void setManualApproval(boolean manualApproval) {
        this.manualApproval = manualApproval;
    }

    public HashMap<String, String> getAttendeeRegistrations() {
        return attendeeRegistrations;
    }

    public void setAttendeeRegistrations(HashMap<String, String> attendeeRegistrations) {
        this.attendeeRegistrations = attendeeRegistrations;
    }

    // Methods to manage attendee registrations

    /**
     * Adds an attendee registration with the given status.
     *
     * @param attendeeId The ID of the attendee.
     * @param status     The registration status ("pending", "accepted", "rejected").
     */
    public void addAttendeeRegistration(String attendeeId, String status) {
        attendeeRegistrations.put(attendeeId, status);
    }

    /**
     * Removes an attendee registration.
     *
     * @param attendeeId The ID of the attendee.
     */
    public void removeAttendeeRegistration(String attendeeId) {
        attendeeRegistrations.remove(attendeeId);
    }

    /**
     * Gets the registration status of a specific attendee.
     *
     * @param attendeeId The ID of the attendee.
     * @return The registration status, or null if the attendee is not registered.
     */
    public String getRegistrationStatus(String attendeeId) {
        return attendeeRegistrations.get(attendeeId);
    }

    // Optionally, you can override toString() for debugging purposes
    @Override
    public String toString() {
        return "Event{" +
               "eventId='" + eventId + '\'' +
               ", title='" + title + '\'' +
               ", description='" + description + '\'' +
               ", date='" + date + '\'' +
               ", startTime='" + startTime + '\'' +
               ", endTime='" + endTime + '\'' +
               ", eventAddress='" + eventAddress + '\'' +
               ", organizerId='" + organizerId + '\'' +
               ", manualApproval=" + manualApproval +
               ", attendeeRegistrations=" + attendeeRegistrations +
               '}';
    }
}