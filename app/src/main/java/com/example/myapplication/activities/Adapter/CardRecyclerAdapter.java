package com.example.myapplication.activities.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.schema.AppUser;
import com.example.myapplication.schema.Profile;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

public class CardRecyclerAdapter extends RecyclerView.Adapter<CardRecyclerAdapter.ViewHolder>{
    Context context;
    List<AppUser> users;

    public CardRecyclerAdapter(Context context, List<AppUser> users){
        this.context = context;
        this.users = users;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card, parent,false);
        return new CardRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.v("REALM TEST BIND VIEW", users.get(position).getProfile().getName());
        holder.setProfile(users.get(position).getProfile());
        Picasso.get()
                .load(users.get(position).getProfile().getPhoto().get(0))
                .into(holder.avt);
        holder.currentDisplay.setMax(users.get(position).getProfile().getPhoto().size());
        holder.currentDisplay.setProgress(1);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Profile profile;
        //everything in a card
        TextView name, age;
        Button yes, nope;
        ProgressBar currentDisplay;
        ImageView avt;
        private int currentDisplayPhoto = 0;
        public ViewHolder(@NonNull View item){
            super(item);
            name = item.findViewById(R.id.username);
            age = item.findViewById(R.id.age);
            yes = item.findViewById(R.id.dating_button);
            nope = item.findViewById(R.id.nope_button);
            avt = item.findViewById(R.id.avatar);
            currentDisplay = item.findViewById(R.id.avaProgress);

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("REALM CHANGE PHOTO", "currently display: " + currentDisplayPhoto + " out of " + profile.getPhoto().size());
                    if(currentDisplayPhoto == (profile.getPhoto().size() - 1)){
                        return;
                    }
                    currentDisplayPhoto += 1;
                    currentDisplay.setProgress(currentDisplayPhoto + 1);
                    Picasso.get()
                            .load(currentPic())
                            .into(avt);
                }
            });

            nope.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("REALM CHANGE PHOTO", "currently display: " + currentDisplayPhoto + " out of " + profile.getPhoto().size());
                    if(currentDisplayPhoto == 0){
                        return;
                    }
                    currentDisplayPhoto -= 1;
                    currentDisplay.setProgress(currentDisplayPhoto + 1);
                    Picasso.get()
                            .load(currentPic())
                            .into(avt);
                }
            });

        }

        public String currentPic(){
            return profile.getPhoto().get(currentDisplayPhoto);
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
            name.setText(profile.getName());
            int age = 1;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                age = Period.between(profile.getDob().toInstant().atOffset(ZoneOffset.UTC).toLocalDate(), LocalDate.now()).getYears();
            }
            this.age.setText("age: " + age);

        }
    }
}