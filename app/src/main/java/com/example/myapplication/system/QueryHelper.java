package com.example.myapplication.system;

import android.util.Log;
import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.schema.AppUser;
import org.bson.Document;
import java.util.ArrayList;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class QueryHelper {
    User user = ApplicationActivity.app.currentUser();

    MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
    MongoDatabase mongoDatabase;
    MongoCollection<Document> collection;

    public QueryHelper(String database, String collection){
        this.mongoDatabase = mongoClient.getDatabase(database);
        this.collection = mongoDatabase.getCollection(collection);
    }

    public synchronized ArrayList findAllUsers(){
        ArrayList<AppUser> allUsers = new ArrayList<>();
        collection.find().iterator().getAsync(task -> {
            if(task.isSuccess()){
                MongoCursor<Document> results = task.get();
                Log.v("EXAMPLE", "successfully found all user email:");
                while (results.hasNext()) {
                    Document temp = results.next();
                    Document profile = (Document) temp.get("profile");
                    AppUser u = new AppUser(profile);
                    allUsers.add(u);
                    Log.v("EXAMPLE", temp.toString());
                }
            } else {
                Log.e("EXAMPLE", "failed to find documents with: ", task.getError());
            }
        });

        return allUsers;
    }

}
