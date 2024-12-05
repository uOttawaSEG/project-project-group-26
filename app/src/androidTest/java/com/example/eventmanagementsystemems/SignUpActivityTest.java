package com.example.eventmanagementsystemems;

import androidx.test.espresso.Root;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.os.IBinder;
import android.view.WindowManager;

import androidx.test.espresso.Espresso;


import com.example.eventmanagementsystemems.accounts.login.SignUpActivity;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {
    @Rule
    public ActivityScenarioRule<SignUpActivity> activityRule =
            new ActivityScenarioRule<>(SignUpActivity.class);


    @Test
    public void testSuccessfulSignUp() {
        // Enter valid user details
        onView(withId(R.id.etFirstName)).perform(replaceText("John"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.etLastName)).perform(replaceText("Doe"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.etEmail)).perform(replaceText("mik12@example.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.etPassword)).perform(replaceText("password123"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.etPhone)).perform(replaceText("1234567890"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.etAddress)).perform(replaceText("123 Main St"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.rbAttendee)).perform(click());

        // Click the sign-up button
        onView(withId(R.id.btnSignup)).perform(click());

        // Verify the "Registration Successful" toast message is displayed
        onView(withText("Registration Successful"))
                .inRoot(new ToastMatcher()) // Match Toast
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    private static class ToastMatcher extends TypeSafeMatcher<Root> {
        @Override
        public void describeTo(Description description) {
            description.appendText("is a Toast");
        }

        @Override
        public boolean matchesSafely(Root root) {
            // Check if the Root is a Toast
            int type = root.getWindowLayoutParams().get().type;
            if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                // Compare the Toast window's token to the app's window token
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                return windowToken == appToken;
            }
            return false;
        }
    }

/* This is another way of doing it but it also does not work, this one simply checks if it goes to the
pending request screen instead of checking the toast

@Test
public void testPendingActivityUIAfterSignUp() {
    // Enter valid user details
    onView(withId(R.id.etFirstName)).perform(replaceText("John"), closeSoftKeyboard());
    onView(withId(R.id.etLastName)).perform(replaceText("Doe"), closeSoftKeyboard());
    onView(withId(R.id.etEmail)).perform(replaceText("valid@example.com"), closeSoftKeyboard());
    onView(withId(R.id.etPassword)).perform(replaceText("password123"), closeSoftKeyboard());
    onView(withId(R.id.etPhone)).perform(replaceText("1234567890"), closeSoftKeyboard());
    onView(withId(R.id.etAddress)).perform(replaceText("123 Main St"), closeSoftKeyboard());
    onView(withId(R.id.rbAttendee)).perform(click());

    // Click the sign-up button
    onView(withId(R.id.btnSignup)).perform(click());

    // Check that the PendingApplicationActivity is displayed by verifying a unique element
    onView(withId(R.id.tvPendingMessage)) // The unique view in PendingApplicationActivity
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));  // Check if it's displayed
}
 */


}