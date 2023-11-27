package com.example.myapplication.activities.MainActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.activities.Conversation;

import org.bson.types.ObjectId;

import java.util.List;

import io.realm.RealmList;


public class ChatFragment extends Fragment {
    ListView chatList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chatList = getView().findViewById(R.id.matched);

        RealmList<ObjectId> list = ApplicationActivity.user.getChatRoomList();
        Log.v("realm test conversation", list.toString());
        ArrayAdapter<ObjectId> arrayAdapter = new ArrayAdapter<>(view.getContext(), R.layout.app_list_view, list);
        chatList.setAdapter(arrayAdapter);
        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), Conversation.class);
                intent.putExtra("chatId", arrayAdapter.getItem(i).toString());
                startActivity(intent);
            }
        });
    }
}