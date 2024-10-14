package com.example.eventmanagementsystemems.entities;

public class Administrator extends Person {

    /**
     * Parameterized Constructor that calls its parent constructor with given names
     * and initializes the user's phone number, email address, and password to the given fields.
     *
     * @param firstName
     * @param lastName

     */
    public Administrator(String firstName, String lastName){
        super(firstName, lastName);
    }

    public void requestRegistration(){
        // Implementation code
    }
}