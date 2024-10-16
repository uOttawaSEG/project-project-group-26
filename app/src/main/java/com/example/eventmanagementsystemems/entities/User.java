package com.example.eventmanagementsystemems.entities;

/**
 * Abstract Class User that extends Person
 */

public abstract class User extends Person {

    protected String phoneNumber;
    protected String emailAddress;
    protected String password;
    protected String address;

    /**
     * Parameterized Constructor that calls its parent constructor with given names
     * and initializes the user's phone number, email address, password, and address.
     */
    public User(String firstName, String lastName, String phoneNumber, String emailAddress, String password, String address){
        super(firstName, lastName);
        setPhoneNumber(phoneNumber);
        setEmailAddress(emailAddress);
        setPassword(password);
        setAddress(address);
    }

    /**
     * Returns the user's email address
     * @return String
     */
    public String getEmailAddress(){
        return emailAddress;
    }

    /**
     * Sets the user's email address to the given address
     * @param emailAddress
     */
    public void setEmailAddress(String emailAddress){
        if (emailAddress == null || emailAddress.isEmpty()){
            throw new IllegalArgumentException("Email address is invalid");
        }
        this.emailAddress = emailAddress;
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
        if (phoneNumber == null || phoneNumber.isEmpty()){
            throw new IllegalArgumentException("Phone number is invalid");
        }

        if (phoneNumber.length() != 10){
            throw new IllegalArgumentException("Phone number must be 10 digits");
        }

        try{
            Long.parseLong(phoneNumber);
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
        else if (password.length() < 8 || password.length() > 20) {
            throw new IllegalArgumentException("Password must be between 8 and 20 characters.");
        }

        this.password = password;
    }
    /**
     * Returns the user's address
     * @return String
     */
    public String getAddress(){ return address; }

    public void setAddress(String address){
        if (address == null || address.isEmpty()){
            throw new IllegalArgumentException("No address entered");
        }
        else if (address.length() < 5){  // setting a minimum of characters for address
            throw new IllegalArgumentException("Invalid address");
        }

        this.address = address;
    }

    /**
     * Returns the user's type
     * @return String
     */
    public String getUserType() {
        if (this instanceof Attendee) {
            return "Attendee";
        } else if (this instanceof Organizer) {
            return "Organizer";
        } else {
            throw new IllegalArgumentException("Unknown User Type");
        }
    }

    /**
     * Overriding the toString() method in class Person to display
     * additional User information.
     * @return String
     */
    @Override
    public String toString() {
        return "Account created successfully!\n" +
                "Name: " + firstName + " " + lastName + "\n" +
                "Username: " + emailAddress + "\n" +
                "Phone number: " + phoneNumber + "\n" +
                "Address: " + address + "\n";
    }
}