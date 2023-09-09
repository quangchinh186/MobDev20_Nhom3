package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;

public class LoginActivity extends AppCompatActivity {
    private String email;
    private String password;
    App app;
    String AppId = "mobileappdev-hwhug";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppId)
                .appName("My App")
                .build());
        Button sendLogin = findViewById(R.id.sendLogin);
        sendLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                io.realm.mongodb.Credentials credentials = Credentials.emailPassword("21021458@vnu.edu.vn","chinh2003");
                app.loginAsync(credentials, it -> {
                    if(it.isSuccess()){
                        User user = app.currentUser();
                        //app.getEmailPassword().confirmUser();
                        assert user != null;
                        Functions functionsManager = app.getFunctions(user);
                        List<String> args = new ArrayList<>();
                        functionsManager.callFunctionAsync("getAllUsers", args, ArrayList.class, result -> {
                            if (result.isSuccess()) {
                                Log.v("USERS", "users list: " + result.get());
                                ArrayList<Document> usersList = result.get();
                                for (Document u : usersList) {
                                    System.out.println(u.toJson());
                                }
                            } else {
                                Log.e("USERS", "failed to get data: " + result.getError());
                            }
                        });
                        Log.v("user", "login as anonymous");
                        /* change activity */

                    }
                    else {
                        Log.v("user", "login failed");
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
