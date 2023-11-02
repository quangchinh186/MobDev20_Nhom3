package com.example.myapplication.system;

import android.util.Log;

import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.schema.AppUser;
import com.example.myapplication.schema.ChatMessage;
import com.example.myapplication.schema.ChatRoom;
import com.example.myapplication.schema.MatchingState;
import com.example.myapplication.schema.Profile;

import org.bson.types.ObjectId;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;

public class QueryHelper {
    private Realm realmApp;

    public QueryHelper(App app){
        openRealm(app);
    }

    public void closeRealm(){
        realmApp.close();
    }

    private void openRealm(App app){
        // add an initial subscription to the sync configuration
        SyncConfiguration config = new SyncConfiguration.Builder(app.currentUser())
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .initialSubscriptions((realm, subscriptions) -> {
                    Log.v("realm", "subscription size: "+ subscriptions.size());
                    subscriptions.addOrUpdate(Subscription.create("user query",
                            realm.where(AppUser.class)));
                    subscriptions.addOrUpdate(Subscription.create("chat query",
                            realm.where(ChatMessage.class)));
                    subscriptions.addOrUpdate(Subscription.create("chat room query",
                            realm.where(ChatRoom.class)));
                    })
                .build();
        // instantiate a realm instance with the flexible sync configuration
        Log.v("EXAMPLE", "Successfully build a realm.");
        Realm.getInstanceAsync(config, new Realm.Callback() {
            @Override
            public void onSuccess(Realm realm) {
                realmApp = realm;
                Log.v("EXAMPLE", "Successfully opened a realm.");
                Log.v("EXAMPLE","Successfully opened the default realm at: " + realm.getPath());
            }

        });
    }

    public void createUser(Profile profile, String id){
        realmApp.executeTransaction(r -> {
            AppUser appUser = new AppUser();
            appUser.setId(new ObjectId(id));
            appUser.setProfile(profile);
            appUser.setChatRoomList(new RealmList<>());
            MatchingState matchingState = new MatchingState();
            appUser.setMatchingState(matchingState);
            r.insert(appUser);
            Log.v("realm", "insert successfully");
        });
    }

    public void findUsers() {
        RealmQuery<AppUser> result = realmApp.where(AppUser.class);
        RealmResults<AppUser> a = result.findAll();
        Log.v("realm", "found: " + result.count() + " result");
        Log.v("realm", a.asJSON());
    }

    public Boolean hasUser(String id){
        RealmQuery<AppUser> result = realmApp.where(AppUser.class).equalTo("_id", new ObjectId(id));
        RealmResults<AppUser> a = result.findAll();

        return a.size() != 0;
    }

}
