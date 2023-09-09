package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Base Realm Packages
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
// Realm Authentication Packages
import io.realm.mongodb.User;
import io.realm.mongodb.Credentials;
// MongoDB Service Packages
import io.realm.mongodb.functions.Functions;

// Utility Packages
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String AppId = "mobileappdev-hwhug";
    App app;
    private String email;
    private String password;

    protected void logIn(String email, String password) {
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
//                Intent emptyActivity = new Intent(MainActivity.this, EmptyActivity.class);
//                emptyActivity.putExtra("email", email);
//                emptyActivity.putExtra("password", password);
//                startActivity(emptyActivity);
            }
            else {
                Log.v("TEST_LOGIN", "login failed");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppId)
                .appName("My App")
                .build());

        Button sendLogin = findViewById(R.id.sendLogin);
        sendLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("TEST_LOGIN", "login clicked");
                logIn(MainActivity.this.email, MainActivity.this.password);
            }
        });
    }

    private void onEmailChange() {
        EditText email_box = findViewById(R.id.mail);
        email_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.this.email = email_box.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void onPasswordChange() {
        EditText password_box = findViewById(R.id.password);
        password_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.this.password = password_box.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        onEmailChange();
        onPasswordChange();
    }
}