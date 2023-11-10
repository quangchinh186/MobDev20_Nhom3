package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.schema.ChatMessage;

import org.bson.types.ObjectId;

import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;

public class Conversation extends AppCompatActivity {
    private ObjectId roomId;
    TextView title;
    EditText messageInput;
    TextView messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        roomId = new ObjectId(intent.getStringExtra("chatId"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        title = findViewById(R.id.chatName);
        title.setText(roomId.toString());
        messageInput = findViewById(R.id.message);
        messages = findViewById(R.id.messages);
        addChangeListener();
    }

    public void send(View view){
        String m = messageInput.getText().toString();
        if(m.equals("")){
            return;
        }
        messageInput.setText("");
        ApplicationActivity.queryHelper.sendMessage(roomId, ApplicationActivity.user.getId(), m);
    }

    public void getMessages(View view){
        String m = ApplicationActivity.queryHelper.getMessageInConversation(roomId);
        messages.setText(m);
    }

    public void addChangeListener(){
        OrderedRealmCollectionChangeListener<RealmResults<ChatMessage>> changeListener = (collection, changeSet) -> {
            if(changeSet.getInsertions().length != 0){
                String m = ApplicationActivity.queryHelper.getMessageInConversation(roomId);
                messages.setText(m);
            }
        };
        ApplicationActivity.queryHelper.chatMessages.findAll().addChangeListener(changeListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}