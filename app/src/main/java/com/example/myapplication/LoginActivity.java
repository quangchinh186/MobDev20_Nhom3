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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;

public class LoginActivity extends AppCompatActivity {
    String AppId = "mobileappdev-hwhug";
    private final String SHARE_PREF = "share_pref";
    App app;
    private EditText emailInput;
    private EditText passwordInput;

    private void sendMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void onLoginClick(View v) {
        if (emailInput.getText().toString().equals("") || passwordInput.getText().toString().equals("")) {
            sendMessage("Bạn cần nhập đầy đủ thông tin");
            return;
        }
        findViewById(R.id.loading_scene).setVisibility(View.VISIBLE);
        String emailUser = emailInput.getText().toString();
        String passwordUser = passwordInput.getText().toString();
        Credentials credentials = Credentials.emailPassword(emailUser, passwordUser);
        app.loginAsync(credentials, it -> {
            if(it.isSuccess()){
                // cache login state
                SharedPreferences sharedPreferences = getSharedPreferences("share_pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("login", true);
                editor.putString("email", emailUser);
                editor.putString("recentEmail", emailUser);
                editor.apply();
                /* change activity */
                startActivity(new Intent(getApplicationContext(), ApplicationActivity.class));
                findViewById(R.id.loading_scene).setVisibility(View.INVISIBLE);
                finish();
            }
            else {
                findViewById(R.id.loading_scene).setVisibility(View.INVISIBLE);
                sendMessage("Tài khoản hoặc mật khẩu không chính xác");
            }
        });
    }
    public void onRegisterNavClick(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        finish();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // to check login state
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREF, MODE_PRIVATE);
        boolean alreadyLogin = sharedPreferences.getBoolean("login", false);
        if (alreadyLogin) {
            startActivity(new Intent(getApplicationContext(), ApplicationActivity.class));
            finish();
        }
        // initialize
        setContentView(R.layout.activity_login_page);
        emailInput = findViewById(R.id.email__input);
        passwordInput = findViewById(R.id.password__input);
        // automatically fill text if app has recent email        
        emailInput.setText(sharedPreferences.getString("recentEmail", ""));
        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppId)
                .appName("My App")
                .build());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}