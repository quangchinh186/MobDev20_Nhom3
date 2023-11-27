package com.example.myapplication.activities.MainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.activities.Adapter.CardRecyclerAdapter;
import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.schema.AppUser;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    List<AppUser> data = new ArrayList<>();
    RecyclerView cardDeck;
    CardRecyclerAdapter cardRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        data = ApplicationActivity.queryHelper.getUsersForDisplay(ApplicationActivity.user.getId());
        Log.v("realm view data", "data size: " + data.size());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(cardDeck == null){
            cardRecyclerAdapter = new CardRecyclerAdapter(view.getContext(), data);
            cardDeck = getView().findViewById(R.id.cardDeck);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext()){
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            cardDeck.setLayoutManager(linearLayoutManager);
            cardDeck.setAdapter(cardRecyclerAdapter);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(cardDeck);
        }

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    swipeLeft(position);
                    data.remove(position);
                    cardRecyclerAdapter.notifyItemRemoved(position);
                    break;

                case ItemTouchHelper.RIGHT:
                    swipeRight(position);
                    data.remove(position);
                    cardRecyclerAdapter.notifyItemRemoved(position);
                    break;
            }
        }
    };

    public void swipeRight(int pos){
        AppUser temp = data.get(pos);
        ApplicationActivity.queryHelper.like(ApplicationActivity.user.getId(), temp.getId());
    }

    public void swipeLeft(int pos){
        AppUser temp = data.get(pos);
        ApplicationActivity.queryHelper.dislike(ApplicationActivity.user.getId(), temp.getId());
    }

}