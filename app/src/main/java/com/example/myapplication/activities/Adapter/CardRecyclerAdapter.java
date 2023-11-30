package com.example.myapplication.activities.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    Button next, prev;
    ProgressBar currentDisplay;
    ImageView avt;
    LinearLayout hobby1;
    LinearLayout hobby2;
    LinearLayout hobby3;
    TextView reviewBtn;
    TextView reviewLayout;
    RelativeLayout cardcontainer;
    int currentDisplayPhoto = 0;

    public CardRecyclerAdapter(Context context, List<AppUser> users){
        super(context, 0, users);
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentCard = convertView;

        if(currentCard == null){
            currentCard = LayoutInflater.from(context).inflate(R.layout.card, parent, false);
        }

        AppUser currentUser = getItem(position);
        Profile profile = currentUser.getProfile();

        Log.v("realm user profile", profile.toString());

        name = currentCard.findViewById(R.id.username);
        age = currentCard.findViewById(R.id.age);


        job = currentCard.findViewById(R.id.occupy);
        avt = currentCard.findViewById(R.id.avatar);
        currentDisplay = currentCard.findViewById(R.id.avaProgress);

        hobby1 = currentCard.findViewById(R.id.hobby1);
        hobby2 = currentCard.findViewById(R.id.hobby2);
        hobby3 = currentCard.findViewById(R.id.hobby3);

        cardcontainer = currentCard.findViewById(R.id.card_container);
        description = currentCard.findViewById(R.id.displayReview);
        reviewLayout = currentCard.findViewById(R.id.reviews_container);

        cardcontainer.setOnClickListener(v -> {
            reviewLayout.setVisibility(View.GONE);
        });

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
        hobby1.setVisibility(View.GONE);
        hobby2.setVisibility(View.GONE);
        hobby3.setVisibility(View.GONE);
        for (int i = 0; i < profile.getHobby().size(); i++) {
            if (i == 0) {
                hobby1.setVisibility(View.VISIBLE);
                ((TextView) hobby1.findViewById(R.id.hobby_item_text)).setText(profile.getHobby().get(i));
            } else if (i == 1) {
                hobby2.setVisibility(View.VISIBLE);
                ((TextView) hobby2.findViewById(R.id.hobby_item_text)).setText(profile.getHobby().get(i));
            } else if (i == 2) {
                hobby3.setVisibility(View.VISIBLE);
                ((TextView) hobby3.findViewById(R.id.hobby_item_text)).setText(profile.getHobby().get(i));
            }
        }
        description.setText(profile.getDescription());
        currentDisplay.setMax(profile.getPhoto().size());
        currentDisplay.setProgress(1);
        StringBuilder tex = new StringBuilder();
        profile.getReview().forEach(review -> {
            tex.append(review).append("\n");
        });
        description.setText(description.getText() + "\n\n\n\nNhận xét của người khác: \n" + tex);
    }

}