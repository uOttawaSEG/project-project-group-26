package com.example.eventmanagementsystemems.entities;

/**
 * Class Administrator that extends Person
 */
public class Administrator extends Person {

    public Administrator(String firstName, String lastName, String emailAddress){
        super(firstName, lastName, emailAddress);
    }

    public String getUserType() {
        return "Administrator";
    }

    @Override
    public String toString() {
        return super.toString() +
               "User Type: Administrator\n";
    }
}