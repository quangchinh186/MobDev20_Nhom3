package com.example.myapplication.activities.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.system.BatoSystem;

public class PreResetPasswordActivity extends AppCompatActivity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_reset_password);
        button = findViewById(R.id.sendEmail);
        EditText mail = findViewById(R.id.email_reset_input);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mail.getText().toString();
                if(email.equals("")){
                    BatoSystem.sendMessage("Vui Lòng Không Bỏ Trống Email", getApplicationContext());
                }
                onResetPasswordClick(email);
            }
        });
    }

    public void onResetPasswordClick(String email){
        Intent intent = new Intent(getApplicationContext(), VerifyOtpActivity.class);
        intent.putExtra("action", "RESET");
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }

}