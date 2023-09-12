package com.example.myapplication;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;

public class LoginActivity extends AppCompatActivity {
    String AppId = "mobileappdev-hwhug";
    App app;
    String email;
    String password;
    private final String SHARE_PREF = "share_pref";

    protected void onLogIn(String email, String password) {
        if (email == null || password == null) {
            Toast.makeText(getApplicationContext(), "You suck!", Toast.LENGTH_SHORT).show();
            return;
        }

        Credentials credentials = Credentials.emailPassword(email, password);
        Log.v("credentials", credentials.asJson());
        app.loginAsync(credentials, it -> {
            if(it.isSuccess()){
                SharedPreferences sharedPreferences = getSharedPreferences("share_pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("login", true);
                editor.putString("email", email);
                editor.apply();
                /* change activity */
                Intent application = new Intent(getApplicationContext(), ApplicationActivity.class);
                application.putExtra("email", LoginActivity.this.email);
                application.putExtra("password", LoginActivity.this.password);
                startActivity(application);
                finish();
            }
            else {
                Log.v("TEST_LOGIN", "login failed");
            }
        });
    }

    private void startEmailChangeListener() {
        EditText emailInput = findViewById(R.id.email__input);
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
    }
    private void startPasswordChangeListener() {
        EditText passwordInput = findViewById(R.id.pasword__input);
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LoginActivity.this.password = passwordInput.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        this.startEmailChangeListener();
        this.startPasswordChangeListener();

        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppId)
                .appName("My App")
                .build());

        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREF, MODE_PRIVATE);
        boolean alreadyLogin = sharedPreferences.getBoolean("login", false);
        Log.v("check_login", String.valueOf(alreadyLogin));
        if (alreadyLogin) {
            Intent application = new Intent(getApplicationContext(), ApplicationActivity.class);
            application.putExtra("password", LoginActivity.this.password);
            startActivity(application);
            finish();
        }

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