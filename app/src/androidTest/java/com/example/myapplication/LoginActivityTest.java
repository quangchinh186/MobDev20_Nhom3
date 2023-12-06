package com.example.myapplication;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.myapplication.activities.Authentication.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void  isEmailDisplayed() {
        Espresso.onView(withId(R.id.email__input)).check(matches(isDisplayed()));
    }

    @Test
    public void  isPasswordDisplayed() {
        Espresso.onView(withId(R.id.password__input)).check(matches(isDisplayed()));
    }

    @Test
    public void  isLogoDisplayed() {
        Espresso.onView(withId(R.id.login__logo)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginWithValidData() {
        // Enter valid email
        Espresso.onView(withId(R.id.email__input))
                .perform(ViewActions.typeText("quangchinh1122@gmail.com"), ViewActions.closeSoftKeyboard());

        // Enter valid password
        Espresso.onView(ViewMatchers.withId(R.id.password__input))
                .perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard());

        // Click register button
        Espresso.onView(ViewMatchers.withId(R.id.loginBtn))
                .perform(ViewActions.click());
    }

    @Test
    public void testLoginWithNoEmail() {
        // Enter valid email
        Espresso.onView(withId(R.id.email__input))
                .perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard());

        // Enter valid password
        Espresso.onView(ViewMatchers.withId(R.id.password__input))
                .perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard());

        // Click register button
        Espresso.onView(ViewMatchers.withId(R.id.loginBtn))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText("Bạn cần nhập đầy đủ thông tin")).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginWithInvalidData() {
        // Enter valid email
        Espresso.onView(withId(R.id.email__input))
                .perform(ViewActions.typeText("banana@gmail.com"), ViewActions.closeSoftKeyboard());

        // Enter valid password
        Espresso.onView(ViewMatchers.withId(R.id.password__input))
                .perform(ViewActions.typeText("123456"), ViewActions.closeSoftKeyboard());

        // Click register button
        Espresso.onView(ViewMatchers.withId(R.id.loginBtn))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText("Tài khoản hoặc mật khẩu không chính xác")).check(matches(isDisplayed()));
    }
}
