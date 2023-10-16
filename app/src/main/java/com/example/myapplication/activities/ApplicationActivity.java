package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.schema.AppUser;
import com.example.myapplication.schema.ChatMessage;
import com.example.myapplication.schema.Profile;
import com.example.myapplication.system.BatoSystem;
import com.example.myapplication.system.QueryHelper;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;

public class ApplicationActivity extends AppCompatActivity {
    //Mongodb stuff
    String AppId = "mobileappdev-hwhug";
    public static App app;
    QueryHelper queryHelper;

    //UI stuff
    ListView userList;
    TextView tempUser;
    EditText newUsername;
    EditText newUserAge;
    public void onShow(View view){
        queryHelper.findUsers();
    }

    public void onInsert(View view){
        Profile profile = new Profile();
        profile.setName(newUsername.getText().toString());
        profile.setAge(Integer.parseInt(newUserAge.getText().toString()));

        queryHelper.insert(profile);
    }

    public void onEdit(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createRealm();
        BatoSystem.initPref(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        // to check login state

        Boolean isLogin = BatoSystem.readBoolean("login", false);
        if (!isLogin) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        } else {
            Map<String, ?> allEntries = BatoSystem.getSharedPreferences().getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                Log.v("realm", entry.getKey() + ": " + entry.getValue().toString());
            }
            Log.v("realm", "current user: "+ app.currentUser().getId());
        }

        //UI
        newUserAge = findViewById(R.id.new_user_age);
        newUsername = findViewById(R.id.new_username);
        String email = BatoSystem.readString("email", "");
        tempUser = findViewById(R.id.tempUser);
        TextView textView = findViewById(R.id.info_app);
        textView.setText("email: " + email);


    }



    private void createRealm(){
        if(app != null){
            return;
        }
        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppId)
                .appName("My App")
                .build());

        queryHelper = new QueryHelper(app);
    }

    public void onLogout(View view) {
        BatoSystem.writeBoolean("login", false);
        BatoSystem.writeString("email", "");
        Log.v("logout", "true");
        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginActivity);

        app.currentUser().logOutAsync(res -> {
            if(res.isSuccess()){
                Log.v("realm", "log out success");
            } else {
                Log.v("realm", "fail: " + res.getError().toString());
            }
        });


        finish();
    }

}