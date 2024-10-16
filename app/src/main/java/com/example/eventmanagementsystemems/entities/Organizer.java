package com.example.eventmanagementsystemems.entities;

/**
 * Class Organizer that extends User
 */

public class Organizer extends User {

    protected String organizationName;

    /**
     * Parameterized Constructor that calls parent constructor with given fields and
     * initializes the organization name field to the given name.
     */
    public Organizer(String firstName, String lastName, String phoneNumber, String emailAddress, String password, String address, String organizationName){
        super(firstName, lastName, phoneNumber, emailAddress, password, address);
        setOrganizationName(organizationName);
    }

    /**
     * Returns the organization name that the user belongs to
     * @return String
     */
    public String getOrganizationName(){
        return organizationName;
    }

    /**
     * Sets the organizer's organization to the given name
     * @param organizationName
     */
    public void setOrganizationName(String organizationName){
        if (organizationName == null || organizationName.isEmpty()){
            throw new IllegalArgumentException("Organization name does not exist.");
        }
        this.organizationName = organizationName;
    }

}