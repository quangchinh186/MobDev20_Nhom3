package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.mongodb.App;

public class VerifyOtpActivity extends AppCompatActivity {
    String otpCode;
    String email;
    EditText otpInput;
    App app;
    String SHARE_PREF = "share_pref";

    public void onVerify(View view) {
        String userOtp = otpInput.getText().toString();
        if (userOtp.equals(otpCode)) {
            app.getEmailPassword().retryCustomConfirmationAsync(email, result -> {
                if (result.isSuccess()) {
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREF, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("recentEmail", email);
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "U good?", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Sai OTP rồi nè :(", Toast.LENGTH_SHORT).show();
        }
    }

    public void onChaneText() {
        otpInput = findViewById(R.id.otpInput);
        otpInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otpCode.equals(otpInput.getText().toString())) {

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp2);
        onChaneText();
        otpCode = getIntent().getStringExtra("otp");
        email = getIntent().getStringExtra("email");


    }
}