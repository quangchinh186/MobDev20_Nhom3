package com.example.myapplication.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.myapplication.R;
import com.example.myapplication.activities.SetupActivity.SetupActivity;
import com.example.myapplication.schema.AppUser;
import com.example.myapplication.schema.ChatMessage;
import com.example.myapplication.schema.Profile;
import com.example.myapplication.system.BatoSystem;
import com.example.myapplication.system.QueryHelper;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;

public class ApplicationActivity extends AppCompatActivity {
    Cloudinary cloudinary = new Cloudinary();
    //Mongodb stuff
    String AppId = "mobileappdev-hwhug";
    public static App app;
    public static AppUser user;
    public static QueryHelper queryHelper;
    public void onShow(View view){
        queryHelper.findAllUsers();
    }
    ImageView imageView;
    ListView chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        init();
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

        String email = BatoSystem.readString("email", "");
        TextView textView = findViewById(R.id.info_app);
        textView.setText("email: " + email);
        imageView = findViewById(R.id.imageHolder);
        chatList = findViewById(R.id.chatList);
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

    public void onLogout(View view) {
        BatoSystem.writeBoolean("login", false);
        BatoSystem.writeString("email", "");
        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginActivity);
        app.currentUser().logOutAsync(res -> {
            if(res.isSuccess()){
                queryHelper.closeRealm();
                Log.v("realm", "log out success");
            } else {
                Log.v("realm", "fail: " + res.getError().toString());
            }
        });
        finish();
    }

    public void viewMyProfile(View view){
        Log.v("realm test profile", user.getProfile().toString());
    }

    public void viewConversation(View view){
        RealmList<ObjectId> list = user.getChatRoomList();
        Log.v("realm test conversation", list.toString());
        ArrayAdapter<ObjectId> arrayAdapter = new ArrayAdapter<>(this, R.layout.app_list_view, R.id.list_item, list);
        chatList.setAdapter(arrayAdapter);
        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), Conversation.class);
                intent.putExtra("chatId", arrayAdapter.getItem(i).toString());
                startActivity(intent);
            }
        });
    }


    //upload image by this function
    public void selectImage(View view){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        launchSomeActivity.launch(i);
    }
    //launch a select image view
    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null && data.getData() != null) {
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
                    }
                }
            });

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(app.currentUser() != null){
            queryHelper.closeRealm();
        }
    }
}