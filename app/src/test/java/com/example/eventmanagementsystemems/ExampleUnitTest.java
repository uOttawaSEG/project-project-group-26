package com.example.eventmanagementsystemems;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private Validate nameValidator; //it was ValidateName before

    @Before
    public void setUp() {
        nameValidator = new Validate(); //ValidateName(); one second for a bit, i might make them all classes in one file
    }

    @Test
    public void testNameLengthLessThan10() {
        assertTrue(nameValidator.isNameLengthValid("lourdes"));
    }

    public void testNameLengthGreaterThan10() {
        assertFalse(nameValidator.isNameLengthValid("this_name_is_too_long"));
    }
}