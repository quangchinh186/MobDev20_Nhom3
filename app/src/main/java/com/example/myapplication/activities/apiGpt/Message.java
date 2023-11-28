package com.example.myapplication.activities.apiGpt;

public class Message {
    public static String SENT_BY_ME = "user";
    public static String SENT_BY_API="system";

    String message;
    String sentBy;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentBy() {
        return sentBy;
    }

    public String getApiMess(){
        return "role:" + message + ", content:" + sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public Message(String message, String sentBy) {
        this.message = message;
        this.sentBy = sentBy;
    }
}
