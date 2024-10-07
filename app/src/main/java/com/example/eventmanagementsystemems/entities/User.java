package com.example.eventmanagementsystemems.entities;

public abstract class User extends Person {

    protected String phoneNumber;
    protected String emailAddress;
    protected String password;

    public User (String firstName, String lastName, String phoneNumber, String emailAddress, String password){
        super(firstName, lastName);
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public String getAddress(){
        return emailAddress;
    }

    public void setAddress(String address){

        if (address == null){
            throw new IllegalArgumentException("Address is invalid");
        }

        this.emailAddress = address;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

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
