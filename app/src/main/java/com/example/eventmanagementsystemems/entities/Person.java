package com.example.eventmanagementsystemems.entities;

/**
 * Class Person (parent of User)
 */

import androidx.annotation.NonNull;

public class Person{
    protected String firstName, lastName;

    public Person(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Returns the person's first name
     * @return String
     */
    public String getFirstName(){
        return firstName;
    }

    /**
     * Sets the person's first name to the given name
     * @param firstName
     */
    public void setFirstName(String firstName){

        if (firstName == null){
            throw new IllegalArgumentException("Name is invalid");
        }

        this.firstName = firstName;
    }

    /**
     * Returns the person's last name
     * @return String
     */
    public String getLastNameString(){
        return lastName;
    }

    /**
     * Sets the person's last name to the given name
     * @param lastName
     */
    public void setLastName(String lastName){
        if (lastName == null){
            throw new IllegalArgumentException("Last name is invalid");
        }
        this.lastName = lastName;
    }


    @NonNull
    public String toString() {
        return "Account created successfully!\n" +
                "Name: " + firstName + " " + lastName + "\n";
                //"Phone: " + phoneNumber + "\n";
    }
}