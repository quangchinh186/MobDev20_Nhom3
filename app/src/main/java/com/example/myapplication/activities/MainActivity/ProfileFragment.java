package com.example.myapplication.activities.MainActivity;

import android.content.Context;
import android.content.Intent;
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
import com.example.myapplication.activities.Authentication.LoginActivity;
import com.example.myapplication.schema.ChatMessage;
import com.example.myapplication.schema.ChatRoom;
import com.example.myapplication.schema.Profile;
import com.example.myapplication.system.BatoSystem;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.annotations.Required;
import io.realm.mongodb.App;


public class ProfileFragment extends Fragment {
    App app = ApplicationActivity.app;

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
        Button logout = view.findViewById(R.id.logoutBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generate();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogout();
            }
        });
    }

    public void onLogout() {
        BatoSystem.writeBoolean("login", false);
        BatoSystem.writeString("email", "");
        Intent loginActivity = new Intent(getContext(), LoginActivity.class);
        startActivity(loginActivity);
        app.currentUser().logOutAsync(res -> {
            if(res.isSuccess()){
                ApplicationActivity.queryHelper.closeRealm();
                Log.v("realm", "log out success");
            } else {
                Log.v("realm", "fail: " + res.getError().toString());
            }
        });
        getActivity().finish();
    }

    public void generate(){
        Profile profile = new Profile();
        //profile.setDob(new Date());
        profile.setName("John Smith");
        profile.setGender("Nam");
        profile.setInterest("Nữ");
        profile.getHobby().add("Fighting");
        profile.setOccupy("Businessman");
        profile.setDescription("I'm rich");
        profile.getAchievement().add("Best growing company");
        profile.getReview().add("He's so hot, but too busy");
        profile.getPhoto().add("https://res.cloudinary.com/dihtkakro/image/upload/f_auto,q_auto/gjmpke6udw7z9tznussb");

        ApplicationActivity.queryHelper.createUser(profile);

    }
}