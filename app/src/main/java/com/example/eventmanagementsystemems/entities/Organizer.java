package com.example.eventmanagementsystemems.entities;

/**
 * Class Organizer that extends User
 */
public class Organizer extends User {

    private String organizationName;


    public Organizer(String firstName, String lastName, String emailAddress, String password,
                     String phoneNumber, String address, String organizationName) {
        super(firstName, lastName, emailAddress, password);
        setPhoneNumber(phoneNumber);
        setAddress(address);
        setOrganizationName(organizationName);
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Phone number is required for Organizers");
        }
        if (phoneNumber.length() != 10) {
            throw new IllegalArgumentException("Phone number must be 10 digits");
        }
        try {
            Long.parseLong(phoneNumber);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Phone number must contain only numbers");
        }
        super.setPhoneNumber(phoneNumber);
    }

    @Override
    public void setAddress(String address) {
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address is required for Organizers");
        }
        if (address.length() < 5) {
            throw new IllegalArgumentException("Invalid address");
        }
        super.setAddress(address);
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        if (organizationName == null || organizationName.isEmpty()) {
            throw new IllegalArgumentException("Organization name is required for Organizers");
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