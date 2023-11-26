package com.example.myapplication.system;

import android.util.Log;

import com.cloudinary.Url;
import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.schema.AppUser;
import com.example.myapplication.schema.ChatMessage;
import com.example.myapplication.schema.ChatRoom;
import com.example.myapplication.schema.MatchingState;
import com.example.myapplication.schema.Profile;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

                    Log.v("realm subscription", "size: " + subscriptions.size());
                })
                .build();
        // instantiate a realm instance with the flexible sync configuration
        realmApp = Realm.getInstance(config);
        Log.v("EXAMPLE","Successfully opened the default realm at: " + realmApp);
    }

    //user query

    public void createUser(Profile profile){
        String id = user.getId();
        realmApp.executeTransaction(r -> {
            AppUser appUser = new AppUser();
            appUser.setId(new ObjectId(id));
            appUser.setProfile(profile);
            appUser.setChatRoomList(new RealmList<>());
            appUser.setMatchingState(new MatchingState());
            r.insert(appUser);
            Log.v("realm", "insert successfully");
        });
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

    //Message

    public void createConversation(ObjectId user1, ObjectId user2, String type){
        //create chatroom
        ObjectId room = new ObjectId();
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(room);
        chatRoom.setUser_1(user1);
        chatRoom.setUser_2(user2);
        chatRoom.setType(type);

        //create
        AppUser user_2 = getUser(user2);
        AppUser user_1 = getUser(user1);
        user_2.getChatRoomList().add(room);
        user_1.getChatRoomList().add(room);
        realmApp.executeTransaction(r -> {
            r.insert(chatRoom);
            //insert
            r.insertOrUpdate(user_2);
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

    public String getProfilePicture(ObjectId id){
        return getUser(id).getProfile().getPhoto().first();
    }

    public String getProfileName(ObjectId id){
        return getUser(id).getProfile().getName();
    }

    public ObjectId getFriend(ObjectId roomId){
        ChatRoom chatRoom = realmApp.where(ChatRoom.class).equalTo("_id", roomId).findFirst();
        ObjectId user1 = chatRoom.getUser_1();
        ObjectId user2 = chatRoom.getUser_2();
        if(user1.equals(ApplicationActivity.user.getId())){
            return user2;
        }
        return user1;
    }

    public RealmQuery<ChatMessage> getMessageRealmQuery(ObjectId room){
        return realmApp.where(ChatMessage.class).equalTo("chatRoom", room);
    }

    public RealmQuery<ChatRoom> getChatRoomRealmQuery(ObjectId room){
        return realmApp.where(ChatRoom.class).equalTo("_id", room);
    }

    public RealmQuery<AppUser> getUserRealmQuery(ObjectId id){
        RealmQuery<AppUser> realmQuery = realmApp.where(AppUser.class).equalTo("_id", id);
        return realmQuery;
    }

    //get users for display
    public List<AppUser> getUsersForDisplay(ObjectId user){
        //get users
        AppUser u = getUser(user);
        List<AppUser> list = new ArrayList<>();
        list.addAll(realmApp.where(AppUser.class).findAll());

//        //create filter
//        List<ObjectId> filter = new ArrayList<>();
//        filter.addAll(u.getMatchingState().getMatched());
//        filter.addAll(u.getMatchingState().getLike());
//        filter.addAll(u.getMatchingState().getIsNotLikedBy());
//        filter.addAll(u.getMatchingState().getNotLike());
//
//        //filter
        list.removeIf(i -> (i.getId().equals(u.getId())));

        return list;
    }


    //matching
    public void match(ObjectId user1, ObjectId user2){
        AppUser u1 = getUser(user1);
        AppUser u2 = getUser(user2);

        //create a conversation
        createConversation(user1, user2, "default");

        realmApp.executeTransaction(r -> {
            //update matching state
            u1.getMatchingState().getMatched().add(user2);
            u2.getMatchingState().getMatched().add(user1);

            u1.getMatchingState().getIsLikedBy().remove(user2);
            u2.getMatchingState().getIsLikedBy().remove(user1);

            u1.getMatchingState().getLike().remove(user2);
            u2.getMatchingState().getLike().remove(user1);


            r.insertOrUpdate(u1);
            r.insertOrUpdate(u2);
        });

        //update UI
    }

    //user swipe right for "like"
    public void like(ObjectId user, ObjectId like){
        AppUser u = getUser(user);
        AppUser u2 = getUser(like);

        //if "like" is in user isLikedBy
        if(u.getMatchingState().getIsLikedBy().contains(like)){
            match(user, like);
            Log.v("realm matching", user.toString() + " matched with " + like.toString());
        } else {
            realmApp.executeTransaction(r -> {
                u.getMatchingState().getLike().add(like);
                u2.getMatchingState().getIsLikedBy().add(user);
                r.insertOrUpdate(u);
                r.insertOrUpdate(u2);
            });
        }
    }

    public void dislike(ObjectId user, ObjectId dis){
        AppUser u = getUser(user);
        AppUser u2 = getUser(dis);
        realmApp.executeTransaction(r -> {
            u.getMatchingState().getNotLike().add(dis);
            u2.getMatchingState().getIsNotLikedBy().add(user);
            r.insertOrUpdate(u);
            r.insertOrUpdate(u2);
        });
    }

    public void unMatch(ObjectId user1, ObjectId user2){
        AppUser u1 = getUser(user1);
        AppUser u2 = getUser(user2);

        realmApp.executeTransaction(r -> {
            u1.getMatchingState().getMatched().remove(user2);
            u2.getMatchingState().getMatched().remove(user1);

            u1.getMatchingState().getUnMatch().add(user2);
            u2.getMatchingState().getUnMatch().add(user1);

            r.insertOrUpdate(u1);
            r.insertOrUpdate(u2);
        });

    }

}
