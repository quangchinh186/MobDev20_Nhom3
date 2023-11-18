package com.example.myapplication.schema;

import org.bson.types.ObjectId;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass(embedded = true)
public class MatchingState extends RealmObject {
    @Required
    private RealmList<ObjectId> matched;
    @Required
    private RealmList<ObjectId> isLikedBy;
    @Required
    private RealmList<ObjectId> like;
    @Required
    private RealmList<ObjectId> notLike;

    public MatchingState() {
        matched = new RealmList<>();
        isLikedBy = new RealmList<>();
        like = new RealmList<>();
        notLike = new RealmList<>();
    }
    public RealmList<ObjectId> getMatched() {
        return matched;
    }

    public void setMatched(RealmList<ObjectId> matched) {
        this.matched = matched;
    }

    public RealmList<ObjectId> getIsLikedBy() {
        return isLikedBy;
    }

    public void setIsLikedBy(RealmList<ObjectId> isLikedBy) {
        this.isLikedBy = isLikedBy;
    }

    public RealmList<ObjectId> getLike() {
        return like;
    }

    public void setLike(RealmList<ObjectId> like) {
        this.like = like;
    }

    public RealmList<ObjectId> getNotLike() {
        return notLike;
    }

    public void setNotLike(RealmList<ObjectId> notLike) {
        this.notLike = notLike;
    }
}
