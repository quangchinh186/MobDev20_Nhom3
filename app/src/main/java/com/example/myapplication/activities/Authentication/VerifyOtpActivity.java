package com.example.myapplication.activities.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.system.BatoSystem;

import java.util.Random;


public class VerifyOtpActivity extends AppCompatActivity {
    String otpCode, email, action;
    EditText otpInput;


    public void onVerify(View view) {
        String userOtp = otpInput.getText().toString();
        if (userOtp.equals(otpCode)) {
            if(action.equals("REGISTER")){
                ApplicationActivity.app.getEmailPassword().retryCustomConfirmationAsync(email, result -> {
                    if (result.isSuccess()) {
                        BatoSystem.writeString("recentEmail", email);
                        BatoSystem.writeString("email", email);

                        finish();
                    } else {
                        BatoSystem.sendMessage("Có lỗi đã xảy ra, vui lòng thử lại sau", this);
                    }
                });
            }
            if(action.equals("RESET")){
                Intent intent = new Intent(this, ResetPasswordActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }
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
        BatoSystem.sendMessage("Đã gửi lại mã tới email: " + email, this);
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
        action = getIntent().getStringExtra("action");
        sendVerify();
    }
}