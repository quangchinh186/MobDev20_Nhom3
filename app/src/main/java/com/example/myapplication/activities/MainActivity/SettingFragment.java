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
import android.widget.Button;
import android.widget.Switch;

import com.example.myapplication.R;
import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.activities.Authentication.LoginActivity;
import com.example.myapplication.activities.Authentication.PreResetPasswordActivity;
import com.example.myapplication.schema.Profile;
import com.example.myapplication.system.BatoSystem;

import io.realm.mongodb.App;

public class SettingFragment extends Fragment {
    App app = ApplicationActivity.app;
    Button logout, changePass;
    Switch age;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logout = view.findViewById(R.id.logout);
        changePass = view.findViewById(R.id.change_password);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogout();
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PreResetPasswordActivity.class));
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

        ApplicationActivity.queryHelper.createUser(profile);
    }
}