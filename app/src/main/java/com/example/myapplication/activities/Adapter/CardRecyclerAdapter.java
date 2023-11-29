package com.example.myapplication.activities.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class CardRecyclerAdapter extends ArrayAdapter<AppUser> {
    Context context;
    TextView name, age, description, hobby, job;
    ImageView yes, nope;
    Button next, prev;
    ProgressBar currentDisplay;
    ImageView avt;
    int currentDisplayPhoto = 0;

    public CardRecyclerAdapter(Context context, List<AppUser> users){
        super(context, 0, users);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentCard = convertView;

        if(currentCard == null){
            currentCard = LayoutInflater.from(context).inflate(R.layout.card, parent, false);
        }

        AppUser currentUser = getItem(position);
        Profile profile = currentUser.getProfile();

        name = currentCard.findViewById(R.id.username);
        age = currentCard.findViewById(R.id.age);


        job = currentCard.findViewById(R.id.occupy);
        yes = currentCard.findViewById(R.id.dating_button);
        nope = currentCard.findViewById(R.id.nope_button);
        avt = currentCard.findViewById(R.id.avatar);
        currentDisplay = currentCard.findViewById(R.id.avaProgress);

        setProfile(profile);

        return currentCard;
    }

    public void setProfile(Profile profile) {
        name.setText(profile.getName());
        int age = 1;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            age = Period.between(profile.getDob().toInstant().atOffset(ZoneOffset.UTC).toLocalDate(), LocalDate.now()).getYears();
        }
        this.age.setText("age: " + age + "   " + profile.getGender());
        //description.setText(profile.getDescription());
        StringBuilder hob = new StringBuilder();
        for (String i: profile.getHobby()) {
            hob.append(i).append(", ");
        }
        //hobby.setText(hob);
        job.setText(profile.getOccupy());

        Picasso.get()
                .load(profile.getPhoto().get(0))
                .into(avt);
        currentDisplay.setMax(profile.getPhoto().size());
        currentDisplay.setProgress(1);

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
                        .load(profile.getPhoto().get(currentDisplayPhoto))
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
                        .load(profile.getPhoto().get(currentDisplayPhoto))
                        .into(avt);
            }
        });
    }

}