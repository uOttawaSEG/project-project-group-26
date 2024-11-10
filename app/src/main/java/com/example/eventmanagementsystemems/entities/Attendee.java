package com.example.eventmanagementsystemems.entities;

/**
 * Class Attendee that extends User
 */
public class Attendee extends User {

    private String registrationStatus;

    public Attendee(String firstName, String lastName, String emailAddress, String phoneNumber, String address){
        super(firstName, lastName, emailAddress, phoneNumber, address);
    }

    // Default constructor required for calls to DataSnapshot.getValue(Attendee.class)
    public Attendee() {
        super();
    }

    @Override
    public String getUserType() {
        return "Attendee";
    }

    @Override
    public String toString() {
        return super.toString() +
               "User Type: Attendee\n";
    }

    // Getter and Setter for registrationStatus

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }
}