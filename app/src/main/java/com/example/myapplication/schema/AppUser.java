package com.example.myapplication.schema;

import org.bson.types.ObjectId;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;
import io.realm.annotations.Required;

public class AppUser extends RealmObject {
    @PrimaryKey
    @RealmField("_id")
    private ObjectId id;
    private Profile profile;
    private MatchingState matchingState;
    @Required
    private RealmList<ObjectId> chatRoomList;
    public RealmList<ObjectId> getChatRoomList() {
        return chatRoomList;
    }

    public void setChatRoomList(RealmList<ObjectId> chatRoomList) {
        this.chatRoomList = chatRoomList;
    }

    public MatchingState getMatchingState() {
        return matchingState;
    }

    public void setMatchingState(MatchingState matchingState) {
        this.matchingState = matchingState;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getId() {
        return id;
    }
}


