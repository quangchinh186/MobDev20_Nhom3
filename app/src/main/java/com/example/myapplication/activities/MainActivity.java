package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Base Realm Packages
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
// Realm Authentication Packages
import io.realm.mongodb.User;
import io.realm.mongodb.Credentials;
// MongoDB Service Packages
import io.realm.mongodb.functions.Functions;

// Utility Packages
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String AppId = "mobileappdev-hwhug";
    App app;
    private String email;
    private String password;


    @Override
    protected void onStart() {
        
        super.onStart();
    }
}