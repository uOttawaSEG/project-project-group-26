package com.example.eventmanagementsystemems.entities;

/**
 * Abstract Class User that extends Person
 */
public abstract class User extends Person {

    protected String phoneNumber;
    protected String address;
    protected String userId;
/*
* I removed the int status field because we're relying on the database structure to track the user's status.
* Specifically, users are categorized under different sections in the database:
* users/pending/attendees and users/pending/organizers for pending users.
* users/accepted/attendees and users/accepted/organizers for accepted users.
* users/rejected/attendees and users/rejected/organizers for rejected users.
* By organizing users this way in the database, we can determine a user's status 
* based on where their data is stored, making the status field redundant.
*
* Let me know if you don't want this, but I really feel like this is much easier than keeping track of status in the app.
*/
    /**
     * Constructor initializes the user's name, email, password, phone number, and address
     */
    public User(String firstName, String lastName, String emailAddress, String phoneNumber, String address){
        super(firstName, lastName, emailAddress);
        setPhoneNumber(phoneNumber);
        setAddress(address);
    }

    /**
     * Returns the user's phone number
     */
    public String getPhoneNumber(){
        return phoneNumber;
    }

    /**
     * Sets the user's phone number
     */
    public void setPhoneNumber(String phoneNumber){
        if (phoneNumber == null || phoneNumber.isEmpty()){
            throw new IllegalArgumentException("Phone number is invalid");
        }
        if (phoneNumber.length() != 10){
            throw new IllegalArgumentException("Phone number must be 10 digits");
        }
        try{
            Long.parseLong(phoneNumber);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Phone number must contain only numbers");
        }
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the user's address
     */
    public String getAddress(){ return address; }

    /**
     * Sets the user's address
     */
    public void setAddress(String address){
        if (address == null || address.isEmpty()){
            throw new IllegalArgumentException("No address entered");
        } else if (address.length() < 5){
            throw new IllegalArgumentException("Invalid address");
        }
        this.address = address;
    }

    /**
     * Abstract method to get user type
     */
    public abstract String getUserType();

    /**
     * Overriding the toString() method to display additional User information
     */
    @Override
    public String toString() {
        return super.toString() +
               "Phone Number: " + phoneNumber + "\n" +
               "Address: " + address + "\n";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}