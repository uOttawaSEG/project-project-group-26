package com.example.eventmanagementsystemems;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import com.example.eventmanagementsystemems.accounts.login.SignUpActivity;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SignUpActivityTests {

    private ValidateName nameValidator;

    @Ignore
    public void setUp() {
        nameValidator = new ValidateName();
    }

    @Ignore
    public void testNameLengthLessThan10() {
        assertTrue(nameValidator.isNameLengthValid("lourdes"));
    }



}