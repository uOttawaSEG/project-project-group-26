// Person.java
package com.example.eventmanagementsystemems.entities;

import androidx.annotation.NonNull;

/**
 * Parent Class Person
 */
public class Person {
    protected String firstName;
    protected String lastName;
    protected String emailAddress;
    protected String password;

    public Person(String firstName, String lastName, String emailAddress, String password) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmailAddress(emailAddress);
        setPassword(password);
    }

    // getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isEmpty()){
            throw new IllegalArgumentException("First name is invalid");
        }
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        if (lastName == null || lastName.isEmpty()){
            throw new IllegalArgumentException("Last name is invalid");
        }
        this.lastName = lastName;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress){
        if (emailAddress == null || emailAddress.isEmpty()){
            throw new IllegalArgumentException("Email address is invalid");
        }
        this.emailAddress = emailAddress;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        if (password == null || password.isEmpty()){
            throw new IllegalArgumentException("Invalid password");
        } else if (password.length() < 8 || password.length() > 20) {
            throw new IllegalArgumentException("Password must be between 8 and 20 characters.");
        }
        this.password = password;
    }

    @NonNull
    @Override
    public String toString() {
        return "Name: " + firstName + " " + lastName + "\n" +
               "Email: " + emailAddress + "\n";
    }
}