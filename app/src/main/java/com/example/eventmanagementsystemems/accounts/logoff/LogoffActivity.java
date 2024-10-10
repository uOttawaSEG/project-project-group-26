package com.example.eventmanagementsystemems.accounts.logoff;

public class LogoffActivity {

    public void logOff(){
        /*This is the simplest form of logoff I could think of. It's not enough though retalk with everyone
        because there's no way we could have a logoff method written in the user class because the user class
        is meant to just store information about a user, where as logging off will need to redirect people to
        another page and clear their data in the database. Hala2 keep it for now but once the button and everything
        is in i think we should move it to another class which manages the users session
        (ex: signing in, redirecting to other pages, etc.)*/

        //Nullify the values of the instance to represent that the user has been logged out

        //this.firstName = null;
        //this.lastName = null;
       //this.phoneNumber = null;
        //this.emailAddress = null;
        //this.password = null;

        //System.out.println("User has been logged off.");
    }
}
