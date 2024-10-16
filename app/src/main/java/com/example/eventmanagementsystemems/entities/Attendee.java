// Attendee.java
package com.example.eventmanagementsystemems.entities;

/**
 * Class Attendee that extends User
 */
public class Attendee extends User {

    public Attendee(String firstName, String lastName, String emailAddress, String password, String phoneNumber, String address){
        super(firstName, lastName, emailAddress, password, phoneNumber, address);
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