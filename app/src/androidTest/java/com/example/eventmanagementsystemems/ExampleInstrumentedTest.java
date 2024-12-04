package com.example.eventmanagementsystemems;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.espresso.Espresso;


import com.example.eventmanagementsystemems.accounts.login.SignUpActivity;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    /*@Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.eventmanagementsystems", appContext.getPackageName());
    }*/

    @Rule
    public ActivityScenarioRule<SignUpActivity> activityRule = new ActivityScenarioRule<>(SignUpActivity.class);

    @Test
    public void testPhoneNumberTooShortShowsMessage() {
        Espresso.onView(withId(R.id.etPhone)).perform(ViewActions.typeText("12"));
        Espresso.onView(withId(R.id.btnSignup)).perform(ViewActions.click());
        // Check that the TextView with "Length is too long" is displayed

    }

}