package com.example.eventmanagementsystemems.entities;

/**
 * Class Administrator that extends User
 */
public class Administrator extends User {

    public Administrator(String firstName, String lastName, String emailAddress, String password) {
        super(firstName, lastName, emailAddress, password);
    }

    @Override
    public String getUserType() {
        return "Administrator";
    }

    @Override
    public String toString() {
        return super.toString() +
               "User Type: Administrator\n";
    }
}