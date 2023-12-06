package com.example.myapplication;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import androidx.test.filters.LargeTest;
import com.example.myapplication.activities.Authentication.RegisterActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterActivityTest {
    @Rule
    public ActivityScenarioRule<RegisterActivity> activityScenarioRule = new ActivityScenarioRule<>(RegisterActivity.class);

    @Test
    public void  isEmailDisplayed() {
        Espresso.onView(withId(R.id.email_regis_input)).check(matches(isDisplayed()));
    }

    @Test
    public void  isPasswordDisplayed() {
        Espresso.onView(withId(R.id.password_regis_input)).check(matches(isDisplayed()));
    }

    @Test
    public void  isVerifyDisplayed() {
        Espresso.onView(withId(R.id.verifyPassword_regis_input)).check(matches(isDisplayed()));
    }

    @Test
    public void testRegisterWithValidData() {
        // Enter valid email
        Espresso.onView(withId(R.id.email_regis_input))
                .perform(ViewActions.typeText("validemail@example.com"), ViewActions.closeSoftKeyboard());

        // Enter valid password
        Espresso.onView(ViewMatchers.withId(R.id.password_regis_input))
                .perform(ViewActions.typeText("validPassword"), ViewActions.closeSoftKeyboard());

        // Enter matching password verification
        Espresso.onView(ViewMatchers.withId(R.id.verifyPassword_regis_input))
                .perform(ViewActions.typeText("validPassword"), ViewActions.closeSoftKeyboard());

        // Click register button
        Espresso.onView(ViewMatchers.withId(R.id.registerBtn))
                .perform(ViewActions.click());

        // Verify that loading view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.loading_scene))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testRegisterWithInsufficientData() {
        // Enter invalid email (empty in this case)
        Espresso.onView(ViewMatchers.withId(R.id.email_regis_input))
                .perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard());

        // Enter invalid password (empty in this case)
        Espresso.onView(ViewMatchers.withId(R.id.password_regis_input))
                .perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard());

        // Enter mismatching password verification
        Espresso.onView(ViewMatchers.withId(R.id.verifyPassword_regis_input))
                .perform(ViewActions.typeText("nothing"), ViewActions.closeSoftKeyboard());

        // Perform click on the register button
        Espresso.onView(ViewMatchers.withId(R.id.registerBtn))
                .perform(ViewActions.click());

        //Verify that an error message is displayed
        Espresso.onView(withText("Bạn cần nhập đầy đủ thông tin")).inRoot(withDecorView(not(is(activityScenarioRule.this.getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void testRegisterWithMismatchPassword() {
        // Enter invalid email (empty in this case)
        Espresso.onView(ViewMatchers.withId(R.id.email_regis_input))
                .perform(ViewActions.typeText("validemail@example.com"), ViewActions.closeSoftKeyboard());

        // Enter invalid password (empty in this case)
        Espresso.onView(ViewMatchers.withId(R.id.password_regis_input))
                .perform(ViewActions.typeText("validPassword"), ViewActions.closeSoftKeyboard());

        // Enter mismatching password verification
        Espresso.onView(ViewMatchers.withId(R.id.verifyPassword_regis_input))
                .perform(ViewActions.typeText("nothing"), ViewActions.closeSoftKeyboard());

        // Perform click on the register button
        Espresso.onView(ViewMatchers.withId(R.id.registerBtn))
                .perform(ViewActions.click());

        //Verify that an error message is displayed
        Espresso.onView(withText("Xác nhận mật khẩu không trùng khớp!")).inRoot(withDecorView(not(is(activityScenarioRule.this.getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void testRegisterWithExistedEmail() {
        // Enter invalid email (empty in this case)
        Espresso.onView(ViewMatchers.withId(R.id.email_regis_input))
                .perform(ViewActions.typeText("quangchinh1122@gmail.com"), ViewActions.closeSoftKeyboard());

        // Enter invalid password (empty in this case)
        Espresso.onView(ViewMatchers.withId(R.id.password_regis_input))
                .perform(ViewActions.typeText("nothing"), ViewActions.closeSoftKeyboard());

        // Enter mismatching password verification
        Espresso.onView(ViewMatchers.withId(R.id.verifyPassword_regis_input))
                .perform(ViewActions.typeText("nothing"), ViewActions.closeSoftKeyboard());

        // Perform click on the register button
        Espresso.onView(ViewMatchers.withId(R.id.registerBtn))
                .perform(ViewActions.click());

        //Verify that an error message is displayed
        Espresso.onView(withText("Bạn cần nhập đầy đủ thông tin")).check(matches(isDisplayed()));
    }
}
