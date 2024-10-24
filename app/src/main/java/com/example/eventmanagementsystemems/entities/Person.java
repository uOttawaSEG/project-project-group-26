package com.example.eventmanagementsystemems.entities;

public abstract class Person {
    protected String firstName;
    protected String lastName;
    protected String emailAddress;

    // Constructor without password
    public Person(String firstName, String lastName, String emailAddress) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmailAddress(emailAddress);
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        // Add validation if needed
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        // Add validation if needed
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        // Add validation if needed
        this.emailAddress = emailAddress;
    }

    // Removed password field and related methods because Firebase handles all things passwords. Realistically, we would not store passwords in the app anyways.
}