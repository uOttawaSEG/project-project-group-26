package com.example.eventmanagementsystemems.entities;

/**
 * Class Organizer that extends User
 */
public class Organizer extends User {

    private String organizationName;

    public Organizer(String firstName, String lastName, String emailAddress, String phoneNumber, String address, String organizationName){
        super(firstName, lastName, emailAddress, phoneNumber, address);
        setOrganizationName(organizationName);
    }

    public String getOrganizationName(){
        return organizationName;
    }

    public void setOrganizationName(String organizationName){
        if (organizationName == null || organizationName.isEmpty()){
            throw new IllegalArgumentException("Organization name does not exist.");
        }
        this.organizationName = organizationName;
    }

    @Override
    public String getUserType() {
        return "Organizer";
    }

    @Override
    public String toString() {
        return super.toString() +
               "Organization: " + organizationName + "\n" +
               "User Type: Organizer\n";
    }
}