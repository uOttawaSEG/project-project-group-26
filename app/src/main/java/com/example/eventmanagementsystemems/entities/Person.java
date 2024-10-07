package com.example.eventmanagementsystemems.entities;

import androidx.annotation.NonNull;

public class Person{
    protected String firstName, lastName;

    public Person(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastNameString(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }


    @NonNull
    public String toString() {
        return "Account created successfully!\n" +
                "Name: " + firstName + " " + lastName + "\n";
                //"Phone: " + phoneNumber + "\n";
    }
}