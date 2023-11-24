package com.example.myapplication.activities.Authentication;

import static com.example.myapplication.system.BatoSystem.sendMessage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.system.BatoSystem;

import java.util.concurrent.atomic.AtomicBoolean;

import io.realm.mongodb.Credentials;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput;
    private EditText passwordInput;
    public void onLoginClick(View v) {
        String emailUser = emailInput.getText().toString();
        String passwordUser = passwordInput.getText().toString();
        if (emailUser.equals("") || passwordUser.equals("")) {
            sendMessage("Bạn cần nhập đầy đủ thông tin", this);
            return;
        }
        findViewById(R.id.loading_scene).setVisibility(View.VISIBLE);
        AtomicBoolean isConfirmed = new AtomicBoolean(false);
        Credentials credentials = Credentials.emailPassword(emailUser, passwordUser);
//        ApplicationActivity.app.getEmailPassword().retryCustomConfirmationAsync(emailUser, it -> {
//            isConfirmed.set(it.isSuccess());
//        });
//        if(!isConfirmed.get()){
//            findViewById(R.id.loading_scene).setVisibility(View.INVISIBLE);
//            BatoSystem.sendMessage("Tài khoản chưa được xác nhận", this);
//            Intent verifyOtpAct = new Intent(getApplicationContext(), VerifyOtpActivity.class);
//            verifyOtpAct.putExtra("email", emailInput.getText().toString());
//            verifyOtpAct.putExtra("password", passwordInput.getText().toString());
//            startActivity(verifyOtpAct);
//            return;
//        }
        ApplicationActivity.app.loginAsync(credentials, it -> {
            if(it.isSuccess()){
                // cache login state
                BatoSystem.writeBoolean("login", true);
                BatoSystem.writeString("email", emailUser);
                BatoSystem.writeString("recentEmail", emailUser);
                /* change activity */
                startActivity(new Intent(this, ApplicationActivity.class));
                findViewById(R.id.loading_scene).setVisibility(View.INVISIBLE);
                finish();
            }
            else {
//                findViewById(R.id.loading_scene).setVisibility(View.INVISIBLE);
//                sendMessage("Tài khoản hoặc mật khẩu không chính xác", this);
                System.out.println(it.getError());
            }
        });
    }
    public void onRegisterNavClick(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        finish();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            // initialize
            setContentView(R.layout.activity_login_page);
            emailInput = findViewById(R.id.email__input);
            passwordInput = findViewById(R.id.password__input);
            // automatically fill text if app has recent email
            emailInput.setText(BatoSystem.readString("recentEmail", ""));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}