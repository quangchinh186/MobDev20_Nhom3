package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activities.Adapter.ChatAdapter;
import com.example.myapplication.schema.ChatMessage;
import com.squareup.picasso.Picasso;

import org.bson.types.ObjectId;

import java.util.ArrayList;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class Conversation extends AppCompatActivity {
    private ObjectId roomId;
    private RealmQuery<ChatMessage> messageRealmQuery;

    ObjectId oppo;
    EditText messageInput;
    ChatAdapter chatAdapter;
    RecyclerView chatRecycler;
    TextView senderName;

    ArrayList<ChatMessage> historyMessages = new ArrayList<>();

    void setUpChatAdapter(){
        chatAdapter = new ChatAdapter(this, historyMessages);
        chatRecycler.setLayoutManager(new LinearLayoutManager(this));
        chatRecycler.setAdapter(chatAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        roomId = new ObjectId(intent.getStringExtra("chatId"));
        oppo = ApplicationActivity.queryHelper.getFriend(roomId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        messageInput = findViewById(R.id.message_edit_text);
        senderName = findViewById(R.id.sender_name);
        //UI
        chatRecycler = findViewById(R.id.chat_recycler_view);
        //business logic
        messageRealmQuery = ApplicationActivity.queryHelper.getMessageRealmQuery(roomId);
        addChangeListener();
        getMessages();
        setUpChatAdapter();
        ImageView avtSender = findViewById(R.id.sender_image);
        Picasso.get()
                .load(ApplicationActivity.queryHelper.getProfilePicture(oppo))
                .into(avtSender);
        senderName.setText(ApplicationActivity.queryHelper.getProfileName(oppo));
        chatRecycler.scrollToPosition(historyMessages.size() - 1);
    }

    public void onBackButtonClicked(View view){
        onBackPressed();
    }

    public void onSendMessage(View view){
        String m = messageInput.getText().toString();
        if(m.equals("")){
            return;
        }
        messageInput.setText("");
        ApplicationActivity.queryHelper.sendMessage(roomId, ApplicationActivity.user.getId(), m);
        addChangeListener();
        chatRecycler.scrollToPosition(historyMessages.size() - 1);
    }

    public void getMessages(){

        RealmResults<ChatMessage> results = realmQuery.findAll();
        historyMessages.clear();
        historyMessages.addAll(results);

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

            if(changeSet.getInsertions().length != 0){
                getMessages();
                setUpChatAdapter();
                chatRecycler.setAdapter(chatAdapter);
            }

        };
        messageRealmQuery.findAll().addChangeListener(changeListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}