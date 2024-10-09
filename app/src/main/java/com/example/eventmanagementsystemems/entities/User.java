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

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
}
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

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
}
