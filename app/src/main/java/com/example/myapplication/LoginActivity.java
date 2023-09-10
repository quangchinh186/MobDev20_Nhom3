package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;

public class LoginActivity extends AppCompatActivity {
    String AppId = "mobileappdev-hwhug";
    App app;
    String email;
    String password;

    protected void onLogIn(String email, String password) {
        if (email == null || password == null) {
            Toast.makeText(getApplicationContext(), "You suck!", Toast.LENGTH_SHORT).show();
            return;
        }

        Credentials credentials = Credentials.emailPassword(email, password);
        Log.v("credentials", credentials.asJson());
        app.loginAsync(credentials, it -> {
            if(it.isSuccess()){
                Log.v("TEST_LOGIN", "login successfully");
                /* change activity */
                Intent emptyActivity = new Intent(LoginActivity.this, EmptyActivity.class);
                emptyActivity.putExtra("email", email);
                emptyActivity.putExtra("password", password);
                startActivity(emptyActivity);
            }
            else {
                Log.v("TEST_LOGIN", "login failed");
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        EditText emailInput = findViewById(R.id.email_box);
        EditText passwordInput = findViewById(R.id.pasword__input);

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LoginActivity.this.email = emailInput.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LoginActivity.this.password = passwordInput.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppId)
                .appName("My App")
                .build());

        Button loginButton = findViewById(R.id.login__submit);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogIn(LoginActivity.this.email, LoginActivity.this.password);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
