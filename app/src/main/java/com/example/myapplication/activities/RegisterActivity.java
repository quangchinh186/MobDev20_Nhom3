package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.VerifyOtpActivity;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
//Mailing package


public class RegisterActivity extends AppCompatActivity {
  App app;
  EditText emailInput;
  EditText passwordInput;
  EditText passwordVerifyInput;
  String AppId = "mobileappdev-hwhug";

  //-----------------------------------------
  //send popup toast notification
  private void sendMessage(String msg) {
    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
  }
  //-----------------------------------------
  //clicking register
  public void onRegisterClick(View view) {
    emailInput = findViewById(R.id.email_regis_input);
    passwordInput = findViewById(R.id.password_regis_input);
    passwordVerifyInput = findViewById(R.id.verifyPassword_regis_input);
    if (emailInput.getText().equals("") ||
            passwordInput.getText().equals("")) {
      sendMessage("Bạn cần nhập đầy đủ thông tin");
      return;
    } else if (!passwordVerifyInput.getText().toString().equals(passwordInput.getText().toString())) {
      sendMessage("Xác nhận mật khẩu không trùng khớp!");
      return;
    }
//    loading view
    findViewById(R.id.loading_scene).setVisibility(View.VISIBLE);
//    register
    app.getEmailPassword().registerUserAsync(emailInput.getText().toString(), passwordInput.getText().toString(), it -> {
      if (it.isSuccess()) {
        //send toast
        sendMessage("Đăng ký thành công!");
        Log.v("mail","success");
        // change activity
        Intent verifyOtpAct = new Intent(getApplicationContext(), VerifyOtpActivity.class);
        verifyOtpAct.putExtra("email", emailInput.getText().toString());
        findViewById(R.id.loading_scene).setVisibility(View.INVISIBLE);
        startActivity(verifyOtpAct);
        finish();
      } else {
        findViewById(R.id.loading_scene).setVisibility(View.INVISIBLE);
        sendMessage("Email đã tồn tại!");
      }
    });

  }

  //-----------------------------------------
  //default function
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    emailInput = findViewById(R.id.email_regis_input);
    passwordInput = findViewById(R.id.password_regis_input);
    passwordVerifyInput = findViewById(R.id.verifyPassword_regis_input);

    app = new App(new AppConfiguration.Builder(AppId).build());
  }
}