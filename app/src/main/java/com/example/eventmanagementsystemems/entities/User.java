package com.example.eventmanagementsystemems.entities;

public abstract class User extends Person {

    protected String phoneNumber;
    protected String emailAddress;

    public User (String firstName, String lastName, String phoneNumber, String emailAddress){
        super(firstName, lastName);
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public String getAddress(){
        return emailAddress;
    }

    public void setAddress(String address){
        this.emailAddress = address;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public abstract void register();

    public void logOff(){}
}
