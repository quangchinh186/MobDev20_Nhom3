package com.example.myapplication.activities;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.myapplication.R;
import com.example.myapplication.activities.Adapter.ChatAdapter;
import com.example.myapplication.schema.ChatMessage;
import com.example.myapplication.system.BatoSensei;
import com.example.myapplication.system.Message;
import com.squareup.picasso.Picasso;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    BatoSensei batoSensei;

    List<ChatMessage> historyMessages = new ArrayList<>();

    public class ResponseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("result");
            retrieveResult(result);
        }
    }

    void setUpChatAdapter(){
        chatAdapter = new ChatAdapter(this, historyMessages);
        chatRecycler.setLayoutManager(new LinearLayoutManager(this));
        chatRecycler.setAdapter(chatAdapter);
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("getRecommend");
        registerReceiver(new ResponseReceiver(), filter);
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
        batoSensei = new BatoSensei(senderName.getText().toString(), ApplicationActivity.user.getProfile().getName());
    }

    public void onBackButtonClicked(View view){
        onBackPressed();
    }

    public void onSendImage(View view){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() != Activity.RESULT_OK) {
                return;
            }
            Intent data = result.getData();
            // do your operation from here....
            if (data == null || data.getData() == null) {
                return;
            }
            Uri selectedImageUri = data.getData();
            MediaManager.get().upload(selectedImageUri).callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {
                    //start upload request
                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {
                    //request in progress
                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    //success upload the image
                    //result
                    Log.v("result data", resultData.toString());
                    //image url
                    String imageUrl = "https://res.cloudinary.com/dihtkakro/image/upload/f_auto,q_auto/" + Objects.requireNonNull(resultData.get("public_id")).toString();

                    ApplicationActivity.queryHelper.sendImage(roomId, ApplicationActivity.user.getId(), imageUrl);
                    addChangeListener();
                    getMessages();
                    chatRecycler.scrollToPosition(historyMessages.size() - 1);
                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    //handle error
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {

                }
            }).dispatch();
            Log.v("Image URI",selectedImageUri.toString());
        });

    public void clearLayout(LinearLayout layout){
        while (layout.getChildCount() > 1)
            layout.removeView(layout.getChildAt(1));
    }

    public void addRecommendLayout(String result){
        View view = getLayoutInflater().inflate(R.layout.item_recommendation_box, null);
        view.setVisibility(View.VISIBLE);
        LinearLayout layout = findViewById(R.id.recommendation_box);
        layout.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 37, 0, 0);
        view.setLayoutParams(params);
        view.setOnClickListener(v -> {
            messageInput.setText(result);
            layout.setVisibility(View.GONE);
            clearLayout(layout);
        });
        TextView recommend = view.findViewById(R.id.recommend_chat);
        recommend.setText(result);
        layout.addView(view);
    }

    public void retrieveResult(String result){
        List<String> listResult = Arrays.asList(result.split("\n"));
        listResult.forEach(this::addRecommendLayout);
    }

    public void onAskSensei(List<String> list) {
        StringBuilder question = new StringBuilder();
        for(int i = 1; i < list.size(); i++){
            question.append(list.get(i)).append(" ");
        }
        List<String> gender = new ArrayList<>();
        gender.add(ApplicationActivity.user.getProfile().getGender());
        gender.add(ApplicationActivity.queryHelper.getProfile(oppo).getGender());
        List<String> oppoHobbies = ApplicationActivity.queryHelper.getProfile(oppo).getHobby();
        batoSensei.asyncGetRecommendation(question.toString(), gender, oppoHobbies, getApplicationContext());
    }

    public void onSendMessage(View view){
        try {
            String m = messageInput.getText().toString();
            if(m.equals("")){
                return;
            }
            List<String> list;
            list = Arrays.asList(m.split(" "));
            if(list.size() > 1 && list.get(0).equals("/sensei")){
                onAskSensei(list);
                clearLayout(findViewById(R.id.recommendation_box));
                messageInput.setText("");
                return;
            }
            messageInput.setText("");
            ApplicationActivity.queryHelper.sendMessage(roomId, ApplicationActivity.user.getId(), m);
            addChangeListener();
            chatRecycler.scrollToPosition(historyMessages.size() - 1);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getMessages(){
        RealmResults<ChatMessage> results = messageRealmQuery.findAll();
        historyMessages.clear();
        historyMessages.addAll(results);
    }

    public void addChangeListener(){
        OrderedRealmCollectionChangeListener<RealmResults<ChatMessage>> changeListener = (collection, changeSet) -> {
            if(changeSet.getInsertions().length != 0){
                getMessages();
                chatAdapter.notifyItemInserted(historyMessages.size()-1);
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