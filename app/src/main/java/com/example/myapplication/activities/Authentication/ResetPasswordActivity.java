package com.example.myapplication.activities.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.activities.Authentication.LoginActivity;
import com.example.myapplication.system.BatoSystem;

import io.realm.mongodb.App;


public class ResetPasswordActivity extends AppCompatActivity {
    App app = ApplicationActivity.app;
    EditText newPassword, retype;
    String email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        newPassword = findViewById(R.id.password_reset_input);
        retype = findViewById(R.id.verifyPassword_reset_input);
        email = getIntent().getStringExtra("email");
        Button button = findViewById(R.id.resetBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword(){
        String password = newPassword.getText().toString();
        if(!password.equals(retype.getText().toString())){
            BatoSystem.sendMessage("2 Mật Khẩu Phải Khớp Nhau", this);
        }
        String[] args = {"security answer 1", "security answer 2"};
        app.getEmailPassword().callResetPasswordFunctionAsync(email, password, args, it -> {
            if(it.isSuccess()){
                BatoSystem.sendMessage("Đặt Lại Mật Khẩu Thành Công!", this);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Log.e("Realm reset Error", it.getError().toString());
                BatoSystem.sendMessage("Đã Xảy Ra Lỗi, Vui Lòng Thử Lại Sau", this);
            }
        });
    }
}