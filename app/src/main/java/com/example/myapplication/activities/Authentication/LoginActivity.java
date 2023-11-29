package com.example.myapplication.activities.Authentication;

import static com.example.myapplication.system.BatoSystem.sendMessage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

        Credentials credentials = Credentials.emailPassword(emailUser, passwordUser);
        ApplicationActivity.app.loginAsync(credentials, it -> {
            findViewById(R.id.loading_scene).setVisibility(View.INVISIBLE);
            if(it.isSuccess()){
                // cache login state
                BatoSystem.writeString("email", emailUser);
                BatoSystem.writeString("recentEmail", emailUser);
                /* change activity */
                //startActivity(new Intent(this, ApplicationActivity.class));
                finish();
            }
            else {
                sendMessage("Tài khoản hoặc mật khẩu không chính xác", this);
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        emailInput = findViewById(R.id.email__input);
        passwordInput = findViewById(R.id.password__input);
        // automatically fill text if app has recent email
        emailInput.setText(BatoSystem.readString("recentEmail", ""));

        TextView reset = findViewById(R.id.login_forget_pass);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PreResetPasswordActivity.class));
                finish();
            }
        });
    }
}