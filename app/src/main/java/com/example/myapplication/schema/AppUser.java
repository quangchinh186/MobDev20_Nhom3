package com.example.myapplication.schema;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Map;

public class AppUser {
    private String name;
    private int age;
    private String gender;
    private String occupy;
    private String aboutMe;
    private ArrayList<String> achievement;
    private ArrayList<String> review;
    private ArrayList<String> photo;
    private ArrayList<String> hobby;
    public AppUser(){}

    public AppUser(Document document) {
        name = document.getString("name");
        age = document.getInteger("age");
        gender = document.getString("gender");
        aboutMe = document.getString("aboutMe");
        achievement = (ArrayList<String>) document.get("achievement");
        review = (ArrayList<String>) document.get("review");
        photo = (ArrayList<String>) document.get("photo");
        hobby = (ArrayList<String>) document.get("hobby");
    }

    public Document toDocument(){
        Document document = new Document();
        document.put("email", name);

        return document;
    }

    public String getAvatar(){
        return photo.get(0);
    }

    @Override
    public String toString(){
        return "user: " + this.name + " " + this.age + " " + this.gender + " " + this.aboutMe + " " + this.photo + " " ;
    }


}
