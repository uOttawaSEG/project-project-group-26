// Attendee.java
package com.example.eventmanagementsystemems.entities;

/**
 * Class Attendee that extends User
 */
public class Attendee extends User {

    public Attendee(String firstName, String lastName, String emailAddress, String password,
                    String phoneNumber, String address) {
        super(firstName, lastName, emailAddress, password);
        setPhoneNumber(phoneNumber);
        setAddress(address);
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Phone number is required for Attendees");
        }
        if (phoneNumber.length() != 10) {
            throw new IllegalArgumentException("Phone number must be 10 digits");
        }
        try {
            Long.parseLong(phoneNumber);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Phone number must contain only numbers");
        }
        super.setPhoneNumber(phoneNumber);
    }

    @Override
    public void setAddress(String address) {
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address is required for Attendees");
        }
        if (address.length() < 5) {
            throw new IllegalArgumentException("Invalid address");
        }
        super.setAddress(address);
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
}