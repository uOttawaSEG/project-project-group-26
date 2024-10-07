package com.example.eventmanagementsystemems.entities;


public class Attendee extends User{

    public Attendee(String firstName, String lastName, String address, String phoneNumber, String emailAddress, String password){
        super(firstName, lastName, phoneNumber, emailAddress, password);
    }

    public void requestRegistration(){}
}
