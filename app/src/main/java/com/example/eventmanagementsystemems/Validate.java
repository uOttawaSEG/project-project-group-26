//check comments in ValidateName.java, we could remove the 3 validation classes.

package com.example.eventmanagementsystemems;

public class Validate {
    
    public boolean isNameLengthValid(String name) {
        return name.length() <= 10;
    }

    public boolean isNumberLengthValid(String number) {
        return number.length() <= 10;
        }

    

}
