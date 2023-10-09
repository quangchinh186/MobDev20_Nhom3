package com.example.myapplication.activities;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.schema.AppUser;
import com.squareup.picasso.Picasso;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import io.realm.mongodb.sync.SyncConfiguration;

public class ApplicationActivity extends AppCompatActivity {
    String SHARE_PREF = "share_pref";
    String email;
    User user = LoginActivity.app.currentUser();

    ArrayList<AppUser> list = new ArrayList<>();
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREF, MODE_PRIVATE);
        this.email = sharedPreferences.getString("email", "");
        imageView = findViewById(R.id.imageView);
        TextView textView = findViewById(R.id.info_app);
        textView.setText("email: " + this.email.toString());

        getAllUsers();

    }

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
    private void getAllUsers(){
        Log.v("Current user", user.getId());
        MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("test-database");
        MongoCollection<Document> userCollection = mongoDatabase.getCollection("trash");
        userCollection.find().iterator().getAsync(task -> {
            if(task.isSuccess()){
                MongoCursor<Document> results = task.get();
                Log.v("EXAMPLE", "successfully found all user email:");
                while (results.hasNext()) {
                    Document temp = results.next();
                    Document profile = (Document) temp.get("profile");
                    AppUser u = new AppUser(profile);
                    list.add(u);
                    Log.v("EXAMPLE", temp.toString());
                }
                for (AppUser u: list) {
                    Picasso.get().load(u.getAvatar()).into(imageView);
                    Log.v("USER LIST",u.toString());
                }
            } else {
                Log.e("EXAMPLE", "failed to find documents with: ", task.getError());
            }
        });
    }
}