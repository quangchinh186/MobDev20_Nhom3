package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.mongodb.App;


public class ResetPasswordActivity extends AppCompatActivity {
    App app = ApplicationActivity.app;

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void resetPassword(){
        app.getEmailPassword().callResetPasswordFunction("email", "pass", null);
    }
}