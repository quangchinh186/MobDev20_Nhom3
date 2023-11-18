package com.example.myapplication.schema;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;

public class ChatRoom extends RealmObject {
    @PrimaryKey
    @RealmField("_id")
    private ObjectId id;
    private ObjectId user_1;
    private ObjectId user_2;
    private String type;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getUser_1() {
        return user_1;
    }

    public void setUser_1(ObjectId user_1) {
        this.user_1 = user_1;
    }

    public ObjectId getUser_2() {
        return user_2;
    }

    public void setUser_2(ObjectId user_2) {
        this.user_2 = user_2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
