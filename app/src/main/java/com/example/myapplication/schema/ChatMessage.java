package com.example.myapplication.schema;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Timer;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;

public class ChatMessage extends RealmObject {

    @PrimaryKey
    @RealmField("_id")
    private ObjectId id;
    private ObjectId from;
    private ObjectId chatRoom;
    private String message;
    private Date dateTime;

    public ObjectId getFrom() {
        return from;
    }

    public void setFrom(ObjectId from) {
        this.from = from;
    }

    public ObjectId getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ObjectId chatRoom) {
        this.chatRoom = chatRoom;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
    public ObjectId getId() {
        return id;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

}