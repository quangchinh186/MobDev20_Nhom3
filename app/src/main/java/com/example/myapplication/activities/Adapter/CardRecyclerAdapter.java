package com.example.myapplication.activities.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.schema.AppUser;
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
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view = layoutInflater.inflate(R.layout.card, parent, false);
//        ViewHolder viewHolder = new ViewHolder(view);

        View view = LayoutInflater.from(context).inflate(R.layout.card, parent,false);
        return new CardRecyclerAdapter.ViewHolder(view);

        //return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(users.get(position).getProfile().getName());
        Date dob = users.get(position).getProfile().getDob();
        int age = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            age = Period.between(dob.toInstant().atOffset(ZoneOffset.UTC).toLocalDate(), LocalDate.now()).getYears();
        }
        holder.age.setText("Age: " + age);
        Picasso.get()
                .load(ApplicationActivity.queryHelper.getProfilePicture(users.get(position).getId()))
                .into(holder.avt);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //everything in a card
        TextView name, age;
        ImageButton yes, nope;
        ImageView avt;
        public ViewHolder(@NonNull View item){
            super(item);
            name = item.findViewById(R.id.username);
            age = item.findViewById(R.id.age);
            yes = item.findViewById(R.id.dating_button);
            nope = item.findViewById(R.id.nope_button);
            avt = item.findViewById(R.id.avatar);

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            nope.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    }
}