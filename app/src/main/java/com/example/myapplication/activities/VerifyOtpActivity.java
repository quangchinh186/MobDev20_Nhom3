package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import io.realm.mongodb.Credentials;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.SetupActivity.SetupActivity;
import com.example.myapplication.system.BatoSystem;

import java.util.Random;


public class VerifyOtpActivity extends AppCompatActivity {
    String otpCode;
    String email;
    String password;
    EditText otpInput;

    public void onVerify(View view) {
        String userOtp = otpInput.getText().toString();
        if (userOtp.equals(otpCode)) {
            ApplicationActivity.app.getEmailPassword().retryCustomConfirmationAsync(email, result -> {
                if (result.isSuccess()) {
                    BatoSystem.writeString("recentEmail", email);
                    BatoSystem.writeString("email", password);
                    startActivity(new Intent(getApplicationContext(), SetupActivity.class));
                    finish();
                } else {
                    BatoSystem.sendMessage("Có lỗi đã xảy ra, vui lòng thử lại sau", this);
                }
            });
        } else {
            BatoSystem.sendMessage("Sai OTP rồi nè :(", this);
        }
    }

    private void sendVerify(){
        email = getIntent().getStringExtra("email");
        otpCode = getOtp();
        BatoSystem.sendVerificationCode(email, otpCode);
    }

    public void resendOtpCode(View view) {
        sendVerify();
        BatoSystem.sendMessage("Đã gửi lại email tới tài khoản", this);
    }

    //----------------------------------------
    // It will generate 6 digit random Number.
    // from 0 to 999999
    public static String getOtp() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        otpInput = findViewById(R.id.otpInput);
        sendVerify();
    }
}