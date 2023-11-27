package com.example.myapplication.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.myapplication.R;
import com.example.myapplication.activities.Authentication.LoginActivity;
import com.example.myapplication.activities.MainActivity.ChatFragment;
import com.example.myapplication.activities.MainActivity.HomeFragment;
import com.example.myapplication.activities.MainActivity.ProfileFragment;
import com.example.myapplication.activities.SetupActivity.SetupActivity;
import com.example.myapplication.databinding.ActivityApplicationBinding;
import com.example.myapplication.schema.AppUser;

import com.example.myapplication.system.BatoSystem;
import com.example.myapplication.system.QueryHelper;

import org.bson.types.ObjectId;

import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObjectChangeListener;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

public class ApplicationActivity extends AppCompatActivity {

    private ActivityApplicationBinding binding;
    Cloudinary cloudinary = new Cloudinary();
    //Mongodb stuff
    String AppId = "mobileappdev-hwhug";
    public static App app;
    public static AppUser user;
    public static QueryHelper queryHelper;

    ImageView imageView;
    ListView chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        init();
        BatoSystem.initPref(this);
        super.onCreate(savedInstanceState);

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
            }
            return true;
        });


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

        String email = BatoSystem.readString("email", "");

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null).commit();
    }

    //create local realm and open sync realm if user is logged in
    private void init(){
        Realm.init(this);
        app = new App(new AppConfiguration.Builder(AppId)
                .appName("My App")
                .build());

        if(app.currentUser() != null){
            queryHelper = new QueryHelper(app.currentUser());
            if(!queryHelper.hasUser(new ObjectId(app.currentUser().getId()))){
                startActivity(new Intent(this, SetupActivity.class));
            }
            user = queryHelper.getUser(new ObjectId(app.currentUser().getId()));
        }
    }

    //upload image by this function
    public void selectImage(View view){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() != Activity.RESULT_OK) {
                    return;
                }
                Intent data = result.getData();
                // do your operation from here....
                if (data == null || data.getData() == null) {
                    return;
                }
                Uri selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
                MediaManager.get().upload(selectedImageUri).callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        //start upload request
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        //request in progress
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        //success upload the image
                        //result
                        Log.v("result data", resultData.toString());
                        //image url
                        Log.v("link image", resultData.get("url").toString());
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        //handle error
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                }).dispatch();
                Log.v("Image URI",selectedImageUri.toString());
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(app.currentUser() != null){
            queryHelper.closeRealm();
        }
    }
}