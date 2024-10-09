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
    
    /**
     * Added this method for the user type, its similar to the method seen in assignment of seg2105
     * Returns the user's type
     * @return String
     */
    public String getUserType(User user) {
        if (user instanceof Attendee) {
            return "Attendee";
        } else if (user instanceof Organizer) {
            return "Organizer";
        } else if (user instanceof Administrator) {
            return "Administrator";
        } else {
            throw IllegalArgumentException("Unknown User Type");
        }
    }

    public abstract void requestRegistration();

    public void logOff(){
        /*This is the simplest form of logoff I could think of. It's not enough though retalk with everyone 
        because there's no way we could have a logoff method written in the user class because the user class 
        is meant to just store information about a user, where as logging off will need to redirect people to 
        another page and clear their data in the database. Hala2 keep it for now but once the button and everything 
        is in i think we should move it to another class which manages the users session 
        (ex: signing in, redirecting to other pages, etc.)*/

        //Nullify the values of the instance to represent that the user has been logged out
        this.firstName = null;
        this.lastName = null;
        this.phoneNumber = null;
        this.emailAddress = null;
        this.password = null;

        System.out.println("User has been logged off.");
    }
}
