package com.example.eventmanagementsystemems.entities;

/**
 * Class Attendee that extends User
 */

public class Attendee extends User {

    public Attendee(String firstName, String lastName, String phoneNumber, String emailAddress, String password, String address){
        super(firstName, lastName, phoneNumber, emailAddress, password, address);
    }

}