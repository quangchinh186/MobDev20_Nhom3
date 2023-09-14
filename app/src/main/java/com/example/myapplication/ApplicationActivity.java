package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

public class ApplicationActivity extends AppCompatActivity {
    String SHARE_PREF = "share_pref";
    String email;

    public void onLogout(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("login", false);
        editor.putString("email", "");
        editor.apply();
        Log.v("logout", "true");
        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREF, MODE_PRIVATE);
        this.email = sharedPreferences.getString("email", "");

        TextView textView = findViewById(R.id.info_app);
        textView.setText("email: " + this.email.toString());
    }
}