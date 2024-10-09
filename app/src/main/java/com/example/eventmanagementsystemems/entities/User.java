package com.example.eventmanagementsystemems.entities;

public class User extends Person {
    private int userId;
    private String emailAddress;
    private String password;
    private String phoneNumber;
    private String userType; // "Organizer" or "Attendee"

    public User(String firstName, String lastName, String emailAddress, String password, String phoneNumber, String userType) {
        super(firstName, lastName);
        this.emailAddress = emailAddress;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
    }

    // Getters and setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

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
