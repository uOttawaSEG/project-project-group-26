package com.example.eventmanagementsystemems.entities;

public class Organizer extends User{

    protected String organizationName;

    public Organizer(String firstName, String lastName, String address, String phoneNumber, String emailAddress, String password, String organizationName){
        super(firstName, lastName, phoneNumber, emailAddress, password);
        this.organizationName = organizationName;
    }

    public String getOrganizationName(){ 
        return organizationName;
    }

    public void setOrganizationName(String organizationName){
        this.organizationName = organizationName;
    }

    public void requestRegistration(){}
}
