package com.example.myapplication.activities.MainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.schema.ChatMessage;
import com.example.myapplication.schema.ChatRoom;

import org.bson.types.ObjectId;

import io.realm.RealmQuery;


public class ProfileFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = view.findViewById(R.id.test_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMessage();
            }
        });
    }

    public void getMessage(){
        RealmQuery<ChatMessage> abc = ApplicationActivity.queryHelper.getMessageRealmQuery(new ObjectId("654e07ab59301159ba32e3c4"));
        Log.v("Realm test", abc.findAll().asJSON());
        RealmQuery<ChatRoom> def = ApplicationActivity.queryHelper.getChatRoomRealmQuery(new ObjectId("654e07ab59301159ba32e3c4"));
        Log.v("Realm test", def.findAll().asJSON());
    }
}