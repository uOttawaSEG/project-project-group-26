// User.java
package com.example.eventmanagementsystemems.entities;

/**
 * Abstract Class User that extends Person
 */
public abstract class User extends Person {

    protected String phoneNumber;
    protected String address;

    /**
     * Constructor initializes the user's name, email, password, phone number, and address
     */
    public User(String firstName, String lastName, String emailAddress, String password, String phoneNumber, String address){
        super(firstName, lastName, emailAddress, password);
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
}