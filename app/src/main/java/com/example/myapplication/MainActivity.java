package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

// Base Realm Packages
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
// Realm Authentication Packages
import io.realm.mongodb.User;
import io.realm.mongodb.Credentials;
// MongoDB Service Packages
import io.realm.mongodb.functions.Functions;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.MongoCollection;
// Utility Packages
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String AppId = "mobileappdev-hwhug";
    String databaseName = "test-database";
    String collectionName = "test-collection";
    App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppId)
                .appName("My App")
                .build());

        app.loginAsync(Credentials.anonymous(), it -> {
            if(it.isSuccess()){
                User user = app.currentUser();
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
            }
            else {
                Log.v("user", "login failed");
            }
        });


    }
}