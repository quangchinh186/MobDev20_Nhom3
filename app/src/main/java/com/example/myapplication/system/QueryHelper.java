package com.example.myapplication.system;

import android.util.Log;

import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.schema.AppUser;
import com.example.myapplication.schema.ChatMessage;
import com.example.myapplication.schema.ChatRoom;
import com.example.myapplication.schema.MatchingState;
import com.example.myapplication.schema.Profile;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Callable;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppException;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SubscriptionSet;
import io.realm.mongodb.sync.SyncConfiguration;
import io.realm.mongodb.sync.SyncSession;

public class QueryHelper {
    private Realm realmApp;
    private User user;
    public QueryHelper(User user){
        openRealm(user);
    }

    public void closeRealm(){
        realmApp.close();
    }

    private void openRealm(User user){
        this.user = user;
        // add an initial subscription to the sync configuration
        SyncConfiguration config =
                new SyncConfiguration.Builder(user)
                .allowWritesOnUiThread(true)
                .initialSubscriptions((realm, subscriptions) -> {
                    subscriptions.addOrUpdate(Subscription.create("user query",
                                realm.where(AppUser.class)));
                    subscriptions.addOrUpdate(Subscription.create("chat query",
                                realm.where(ChatMessage.class)));
                    subscriptions.addOrUpdate(Subscription.create("chat room query",
                                realm.where(ChatRoom.class)));
                })
                .build();
        // instantiate a realm instance with the flexible sync configuration
        realmApp = Realm.getInstance(config);
        Log.v("EXAMPLE","Successfully opened the default realm at: " + realmApp);
    }

    public void createUser(Profile profile){
        String id = user.getId();
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
        RealmQuery<AppUser> realmQuery = realmApp.where(AppUser.class);
        Log.v("realm", realmQuery.findAll().asJSON());
    }

    public AppUser getUser(ObjectId id){
        RealmQuery<AppUser> realmQuery = realmApp.where(AppUser.class).equalTo("_id", id);
        return realmQuery.findFirst();
    }

    public Boolean hasUser(ObjectId id){
        RealmQuery<AppUser> realmQuery = realmApp.where(AppUser.class).equalTo("_id", id);
        RealmResults<AppUser> a = realmQuery.findAll();
        return a.size() != 0;
    }

    public void createConversation(AppUser user_1, ObjectId user_2, String type){
        realmApp.executeTransaction(r -> {
            //create chatroom
            ObjectId room = new ObjectId();
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setId(room);
            chatRoom.setUser_1(user_1.getId());
            chatRoom.setUser_2(user_2);
            chatRoom.setType(type);
            r.insert(chatRoom);
            //create in opponent
            AppUser u_2 = getUser(user_2);
            u_2.getChatRoomList().add(room);
            r.insertOrUpdate(u_2);
            //Create in self
            user_1.getChatRoomList().add(room);
            r.insertOrUpdate(user_1);
            Log.v("realm conversation", "created new conversation!!");
        });
    }

    public void sendMessage(ObjectId roomId, ObjectId from, String message){
        realmApp.executeTransaction(r -> {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setId(new ObjectId());
            chatMessage.setChatRoom(roomId);
            chatMessage.setMessage(message);
            chatMessage.setDateTime(new Date());
            chatMessage.setFrom(from);
            r.insert(chatMessage);
            Log.v("realm", "insert successfully");
        });
    }

    public RealmQuery<ChatMessage> getRealmQuery(ObjectId room){
        RealmQuery<ChatMessage> realmQuery = realmApp.where(ChatMessage.class).equalTo("chatRoom", room);
        return realmQuery;
    }

}
