package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.schema.ChatMessage;

import org.bson.types.ObjectId;

import java.util.ArrayList;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class Conversation extends AppCompatActivity {
    private ObjectId roomId;
    private RealmQuery<ChatMessage> messageRealmQuery;

    ArrayList<ChatMessage> history = new ArrayList<>();
    TextView title;
    EditText messageInput;
    TextView messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        roomId = new ObjectId(intent.getStringExtra("chatId"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        //UI
        title = findViewById(R.id.chatName);
        title.setText(roomId.toString());
        messageInput = findViewById(R.id.message);
        messages = findViewById(R.id.messages);
        //business logic
        messageRealmQuery = ApplicationActivity.queryHelper.getMessageRealmQuery(roomId);
        addChangeListener();
        getMessages();
    }

    public void send(View view){
        String m = messageInput.getText().toString();
        if(m.equals("")){
            return;
        }
        messageInput.setText("");
        ApplicationActivity.queryHelper.sendMessage(roomId, ApplicationActivity.user.getId(), m);
    }

    public void getMessages(){
        RealmResults<ChatMessage> m = messageRealmQuery.findAll();
        history.addAll(m);
    }

    public void addChangeListener(){
        OrderedRealmCollectionChangeListener<RealmResults<ChatMessage>> changeListener = (collection, changeSet) -> {
//            OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
//            Log.v("realm insert", "change listener is working");
//            for (OrderedCollectionChangeSet.Range range: insertions) {
//                Log.v("realm insert", range.toString());
//                realmQuery.
//
//            }
            history.clear();
            getMessages();
        };
        messageRealmQuery.findAll().addChangeListener(changeListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}