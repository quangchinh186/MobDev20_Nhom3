package com.example.myapplication.schema;

import androidx.annotation.NonNull;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass(embedded = true)
public class Profile extends RealmObject {
    private String name;
    private int age;
    @Required
    private String gender;
    @Required
    private String interest;
    private String occupy;
    private String description;
    @Required
    private RealmList<String> achievement;
    @Required
    private RealmList<String> review;
    @Required
    private RealmList<String> photo;
    @Required
    private RealmList<String> hobby;

    public Profile(){
        this.name = "Nameless";
        this.age = 0;
        this.gender = "gender";
        this.interest = "";
        this.occupy = "occupy";
        this.description = "description";
        this.achievement = new RealmList<>();
        this.review = new RealmList<>();
        this.photo = new RealmList<>();
        this.hobby = new RealmList<>();
    }

    public Profile(String name, int age, String gender, String occupy, String description,
                   RealmList<String> achievement, RealmList<String> review, RealmList<String> photo,
                   RealmList<String> hobby) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.occupy = occupy;
        this.description = description;
        this.achievement = achievement;
        this.review = review;
        this.photo = photo;
        this.hobby = hobby;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOccupy() {
        return occupy;
    }

    public void setOccupy(String occupy) {
        this.occupy = occupy;
    }

    public RealmList<String> getAchievement() {
        return achievement;
    }

    public void setAchievement(RealmList<String> achievement) {
        this.achievement = achievement;
    }

    public RealmList<String> getReview() {
        return review;
    }

    public void setReview(RealmList<String> review) {
        this.review = review;
    }

    public RealmList<String> getPhoto() {
        return photo;
    }

    public void setPhoto(RealmList<String> photo) {
        this.photo = photo;
    }

    public RealmList<String> getHobby() {
        return hobby;
    }

    public void setHobby(RealmList<String> hobby) {
        this.hobby = hobby;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name + this.age;
    }
}