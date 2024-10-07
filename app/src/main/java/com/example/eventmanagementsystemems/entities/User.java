package com.example.eventmanagementsystemems.entities;

/**
 * Abstract Class User that extends Person
 */

public abstract class User extends Person {

    protected String phoneNumber;
    protected String emailAddress;
    protected String password;

    /**
     * Parameterized Constructor that calls its parent constructor with given names
     * and initializes the user's phone number, email address, and password to the given fields.
     *
     */
    public User (String firstName, String lastName, String phoneNumber, String emailAddress, String password){
        super(firstName, lastName);
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    /**
     * Returns the user's address
     * @return String
     */
    public String getAddress(){
        return emailAddress;
    }

    /**
     * Sets the user's email address to the given address
     * @param address
     */
    public void setAddress(String address){

        if (address == null){
            throw new IllegalArgumentException("Address is invalid");
        }

        this.emailAddress = address;
    }

    /**
     * Returns the user's phone number
     * @return String
     */
    public String getPhoneNumber(){
        return phoneNumber;
    }

    /**
     * Sets the user's phone number to the given number
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber){

        if (phoneNumber == null){
            throw new IllegalArgumentException("Phone number is invalid");
        }

        if (phoneNumber.length() != 10){
            throw new IllegalArgumentException("Phone number must be 10 digits");
        }

        try{
            Integer.parseInt(phoneNumber);
        }
        catch (NumberFormatException e){
            throw new IllegalArgumentException("Phone number must contain only numbers");
        }

        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the user's password
     * @return String
     */
    public String getPassword(){ return password; }

    public void setPassword(String password){
        if (password == null || password.isEmpty()){
            throw new IllegalArgumentException("Invalid password");
        }
        if (password.length() < 8 || password.length() > 20) {
            throw new IllegalArgumentException("Password must be between 3 and 50 characters.");
        }

        this.password = password;
    }

    public abstract void requestRegistration();

    public void logOff(){}
}
