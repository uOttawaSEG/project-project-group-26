/* making this class a comment, both ValidateName and ValidatePhoneNumber classes are in Validate.java, but from what i see
no need for either, field validation and error messages were already in signupactivity class, 
I added one field validation tho to make sure that no email is in use twice.

Unless you need this class for something other than the field validation and error messages part of deliverable, we could delete the 3 classes, Validate.java, ValidateName.java, ValidatePhoneNumber.java.

package com.example.eventmanagementsystemems;

public class ValidateName {

    public boolean isNameLengthValid(String name) {
        return name.length() <= 10;
    }
}
*/