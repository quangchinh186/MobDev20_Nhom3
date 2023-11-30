package com.example.myapplication.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.cloudinary.*;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.myapplication.R;
import com.example.myapplication.activities.Authentication.LoginActivity;
import com.example.myapplication.activities.MainActivity.ChatFragment;
import com.example.myapplication.activities.MainActivity.HomeFragment;
import com.example.myapplication.activities.MainActivity.ProfileFragment;
import com.example.myapplication.activities.MainActivity.SettingFragment;
import com.example.myapplication.activities.SetupActivity.SetupActivity;
import com.example.myapplication.databinding.ActivityApplicationBinding;
import com.example.myapplication.schema.AppUser;

import com.example.myapplication.schema.Profile;
import com.example.myapplication.system.BatoSystem;
import com.example.myapplication.system.QueryHelper;

import org.bson.types.ObjectId;

import java.util.Map;

import io.realm.ObjectChangeSet;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObjectChangeListener;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

public class ApplicationActivity extends AppCompatActivity {
    private ActivityApplicationBinding binding;
    //Mongodb stuff
    String AppId = "mobileappdev-hwhug";
    public static App app;
    public static AppUser user = null;
    public static QueryHelper queryHelper = null;

    public static boolean filterHobbies = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init once and share pref
        initThingsOnce();
        BatoSystem.initPref(this);

        // to check login state
        if (app.currentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        } else {
            initSyncRealm();
        }

        //view
        binding = ActivityApplicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //set default fragment

        replaceFragment(new HomeFragment());
        binding.itemsNav.setBackground(null);
        binding.itemsNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.chat) {
                replaceFragment(new ChatFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            } else if (itemId == R.id.setting) {
                replaceFragment(new SettingFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    //open sync realm if user is logged in
    private void initSyncRealm(){
        if(app.currentUser() != null){
            queryHelper = new QueryHelper(app.currentUser(), getApplicationContext());
            if(!queryHelper.hasUser(new ObjectId(app.currentUser().getId()))){
                startActivity(new Intent(this, SetupActivity.class));
            } else {
                user = queryHelper.getUser(new ObjectId(app.currentUser().getId()));
                checkNewMatch();
                addListener();
            }
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }

    private void initThingsOnce(){
        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppId)
                .appName("My App")
                .build());
        MediaManager.init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(queryHelper == null){
            initSyncRealm();
        } else {
            user = queryHelper.getUser(new ObjectId(app.currentUser().getId()));
            filterHobbies = BatoSystem.readBoolean(user.getId() + "filter", false);
            checkNewMatch();
            addListener();
        }
        if(binding.itemsNav.getSelectedItemId() == R.id.home){
            binding.itemsNav.setSelectedItemId(R.id.home);
            replaceFragment(new HomeFragment());
        }

    }


    @Override
    protected void onDestroy() {
        if(app.currentUser() != null){
            BatoSystem.writeInteger(user.getId().toString() + "match", user.getMatchingState().getMatched().size());
            BatoSystem.writeBoolean(user.getId().toString() + "filter", filterHobbies);
            closeSyncRealm();
        }
        super.onDestroy();
    }

    public void closeSyncRealm(){
        queryHelper.closeRealm();
        queryHelper = null;
        user = null;
    }

    private void checkNewMatch(){
        int oldM = BatoSystem.readInteger(user.getId().toString() + "match", 0);
        int newM = user.getMatchingState().getMatched().size();
        if(oldM != newM){
            BatoSystem.writeInteger(user.getId().toString(), newM);
            BatoSystem.sendMessage("Danh Sách Tương Hợp Của Bạn Có " + (newM - oldM) + " Sự Thay Đổi, Kiêm Tra Ngay", getApplicationContext());
        }
    }

    private void addListener(){
        user.addChangeListener(new RealmObjectChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel realmModel, @Nullable ObjectChangeSet changeSet) {
                Log.v("Realm change", "have change");
                if(changeSet.getChangedFields().length != 0){
                    user = queryHelper.getUser(new ObjectId(app.currentUser().getId()));
                    int newMatchSize = user.getMatchingState().getMatched().size();
                    int oldMatchSize = BatoSystem.readInteger(user.getId().toString() + "match", 0);
                    Log.v("Realm Change", "old: " + oldMatchSize + ", new: " + newMatchSize );
                    if(oldMatchSize != newMatchSize){
                        BatoSystem.writeInteger(user.getId().toString(), newMatchSize);
                        BatoSystem.sendMessage("Danh Sách Tương Hợp Của Bạn Có " + (newMatchSize - oldMatchSize) + " Sự Thay Đổi, Kiêm Tra Ngay", getApplicationContext());
                    }
                }
            }
        });
    }
}