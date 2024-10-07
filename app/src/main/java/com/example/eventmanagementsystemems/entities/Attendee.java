package com.example.eventmanagementsystemems.entities;


public class Attendee extends User{

    public Attendee(String firstName, String lastName, String address, String phoneNumber, String emailAddress){
        super(firstName, lastName, phoneNumber, emailAddress);
    }

    public void register(){}
}
