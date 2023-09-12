package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.autofill.UserData;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;

public class RegisterActivity extends AppCompatActivity {
    App app;
    EditText emailInput;
    EditText passwordInput;
    String AppId = "mobileappdev-hwhug";

    private void sendMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void onRegisterClick(View view) {
        app.getEmailPassword().registerUserAsync(emailInput.getText().toString(), passwordInput.getText().toString(), it -> {
            sendMessage("Register successfully");
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailInput = findViewById(R.id.email_regis_input);
        passwordInput = findViewById(R.id.password_regis_input);

        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppId).build());
    }
}