package com.example.eventmanagementsystemems.entities;

public class Administrator extends User{

    /**
     * Parameterized Constructor that calls its parent constructor with given names
     * and initializes the user's phone number, email address, and password to the given fields.
     *
     * @param firstName
     * @param lastName
     * @param address
     * @param phoneNumber
     * @param emailAddress
     * @param password
     */
    public Administrator(String firstName, String lastName, String address, String phoneNumber, String emailAddress, String password){
        super(firstName, lastName, phoneNumber, emailAddress, password);
        this.address = address;
    }

    public void requestRegistration(){}
}
