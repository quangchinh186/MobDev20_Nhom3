package com.example.myapplication.activities.MainActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.example.myapplication.R;
import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.activities.Conversation;
import com.example.myapplication.schema.Profile;
import com.squareup.picasso.Picasso;

import org.bson.types.ObjectId;

import java.util.List;

import io.realm.RealmList;


public class ChatFragment extends Fragment {
    LinearLayout conversationList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    private void loadAvatar(String url, ImageView avatar) {
        Picasso.get()
                .load(url)
                .into(avatar);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);
            conversationList = getActivity().findViewById(R.id.conversation_list_container);
            EditText reviewEdit = getActivity().findViewById(R.id.item_conversation_review_edittext);
            TextView submitReviewBtn = getActivity().findViewById(R.id.item_conversation_submit_review);
            LinearLayout reviewContainer = getActivity().findViewById(R.id.review_container);
            FrameLayout chatFragment = getActivity().findViewById(R.id.fragment_chat);
            chatFragment.setOnClickListener(v -> {
                reviewContainer.setVisibility(View.GONE);
            });

            RealmList<ObjectId> roomList = ApplicationActivity.user.getChatRoomList();
            if (roomList.size() == 0) {
                return;
            }
            Log.v("realm test conversation", roomList.toString());

            roomList.forEach(objectId -> {
                View item = getLayoutInflater().inflate(R.layout.app_list_view, conversationList, false);
                ImageView avt = item.findViewById(R.id.item_conversation_avatar);
                TextView name = item.findViewById(R.id.item_conversation_name);
                ImageView btnView = item.findViewById(R.id.item_conversation_view_button);
                CardView btnDelete = item.findViewById(R.id.item_conversation_delete);
                ImageView btnReview = item.findViewById(R.id.item_conversation_review);
                ObjectId friendId = ApplicationActivity.queryHelper.getFriend(objectId);
                loadAvatar(ApplicationActivity.queryHelper.getProfilePicture(friendId), avt);
                name.setText(ApplicationActivity.queryHelper.getProfileName(friendId));
                btnReview.setOnClickListener(v -> {
                    reviewContainer.setVisibility(View.VISIBLE);
                    submitReviewBtn.setOnClickListener(vv -> {
                        ApplicationActivity.queryHelper.addReview(friendId, reviewEdit.getText().toString());
                        reviewEdit.setText("");
                        reviewContainer.setVisibility(View.INVISIBLE);
                    });
                });
                btnView.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), Conversation.class);
                    intent.putExtra("chatId", objectId.toString());
                    startActivity(intent);
                });
                btnDelete.setOnClickListener(v -> {
                    ApplicationActivity.queryHelper.unMatch(ApplicationActivity.user.getId(), objectId);
                    roomList.remove(objectId);
                    conversationList.removeView(item);
                });
                conversationList.addView(item);
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.v("realm test conversation", e.getMessage());
        }
    }
}