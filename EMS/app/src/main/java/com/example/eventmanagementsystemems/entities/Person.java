package com.example.eventmanagementsystemems.entities;

import androidx.annotation.NonNull;

public class Person{
    private String firstName, lastName;
    private String phoneNumber;
    private String address;

    public Person(String firstName, String lastName, String address, String phoneNumber){
        this.firstName=firstName;
        this.lastName=lastName;
        this.phoneNumber=phoneNumber;
        this.address=address;
        
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName=firstName;
    }

    public String getLastNameString(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName=lastName;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address=address;
    }

    @NonNull
    public String toString() {
        return "Account created successfully!\n" +
                "Name: " + firstName + " " + lastName + "\n" +
                "Phone: " + phoneNumber + "\n" +
                "Address: " + address;
    }


}