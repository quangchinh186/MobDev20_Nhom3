package com.example.myapplication.schema;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;

public class ChatRoom extends RealmObject {
    @PrimaryKey
    @RealmField("_id")
    private ObjectId id;
    private boolean isAnonymous;
}
