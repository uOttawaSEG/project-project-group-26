package com.example.eventmanagementsystemems.entities;

public class Administrator extends User {

    /**
     * Parameterized Constructor that calls its parent constructor with given names
     * and initializes the user's phone number, email address, and password to the given fields.
     *
     * @param firstName
     * @param lastName
     * @param phoneNumber
     * @param emailAddress
     * @param password
     * @param address
     */
    public Administrator(String firstName, String lastName, String phoneNumber, String emailAddress, String password, String address){
        super(firstName, lastName, phoneNumber, emailAddress, password, address);
    }

    public void requestRegistration(){
        // Implementation code
    }
}