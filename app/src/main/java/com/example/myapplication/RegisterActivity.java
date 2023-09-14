package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.util.Log;
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
    String SHARE_PREF = "share_pref";

    private void sendMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void onRegisterClick(View view) {
        EditText emailInput = findViewById(R.id.email_regis_input);
        EditText passwordInput = findViewById(R.id.password_regis_input);
        EditText passwordVerifyInput = findViewById(R.id.verifyPassword_regis_input);
        if (emailInput.getText().equals("") ||
            passwordInput.getText().equals("")) {
            sendMessage("Bạn cần nhập đầy đủ thông tin");
            return;
        } else if (!passwordVerifyInput.getText().toString().equals(passwordInput.getText().toString())) {
            sendMessage("Xác nhận mật khẩu không trùng khớp!");
            return;
        }
        findViewById(R.id.loading_scene).setVisibility(View.VISIBLE);
        app.getEmailPassword().registerUserAsync(emailInput.getText().toString(), passwordInput.getText().toString(), it -> {
            if (it.isSuccess()) {
                sendMessage("Đăng ký thành công!");
                // cache email that recently login in app
                SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("recentEmail", emailInput.getText().toString());
                editor.apply();
                // change activity
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                findViewById(R.id.loading_scene).setVisibility(View.INVISIBLE);
                finish();
            } else {
                findViewById(R.id.loading_scene).setVisibility(View.INVISIBLE);
                sendMessage("Email đã tồn tại!");
            }
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