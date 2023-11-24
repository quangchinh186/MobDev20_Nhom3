package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.myapplication.R;

public class SlashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_slash);

    new Handler().postDelayed(() -> {
      startActivity(new Intent(SlashActivity.this, ApplicationActivity.class));
      finish();
    }, 2000);
  }
}