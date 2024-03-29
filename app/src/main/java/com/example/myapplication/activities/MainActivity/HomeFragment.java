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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.Adapter.CardRecyclerAdapter;
import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.schema.AppUser;
import com.example.myapplication.system.BatoSystem;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {
    List<AppUser> data = new ArrayList<>();
    //RecyclerView cardDeck;
    ArrayAdapter<AppUser> cardRecyclerAdapter;
    SwipeFlingAdapterView cardDeck;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            data = ApplicationActivity.queryHelper.getUsersForDisplay(ApplicationActivity.user.getId(), ApplicationActivity.filterHobbies);

        } catch (Exception e) {
            //Toast.makeText(getActivity(), "Hãy đợi có thêm người dùng tham gia ứng dụng nhé!", Toast.LENGTH_LONG).show();
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(cardDeck == null && ApplicationActivity.user != null){
            //data = ApplicationActivity.queryHelper.getUsersForDisplay(ApplicationActivity.user.getId(), ApplicationActivity.filterHobbies);
            cardDeck = getView().findViewById(R.id.cardDeck);
            Collections.shuffle(data);
            cardRecyclerAdapter = new CardRecyclerAdapter(view.getContext(), data);
            cardDeck.setAdapter(cardRecyclerAdapter);
            try {
                cardDeck.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
                    @Override
                    public void removeFirstObjectInAdapter() {
                        // this is the simplest way to delete an object from the Adapter (/AdapterView)
                        cardRecyclerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onLeftCardExit(Object dataObject) {
                        //Do something on the left!
                        //You also have access to the original object.
                        //If you want to use it just cast it (String) dataObject
                        swipeLeft(0);
                        data.remove(0);
                        //BatoSystem.sendMessage("You disliked ", getContext());
                    }

                    @Override
                    public void onRightCardExit(Object dataObject) {
                        swipeRight(0);
                        data.remove(0);
                        //BatoSystem.sendMessage("You liked ", getContext());
                    }

                    @Override
                    public void onAdapterAboutToEmpty(int itemsInAdapter) {
                        Toast.makeText(getActivity(), "Hãy đợi có thêm người dùng tham gia ứng dụng nhé!", Toast.LENGTH_LONG).show();
                        System.out.println("itemsInAdapter = " + itemsInAdapter);
                    }

                    @Override
                    public void onScroll(float scrollProgressPercent) {

                    }
                });
            } catch (Exception e) {
                //Toast.makeText(getActivity(), "Hãy đợi có thêm người dùng tham gia ứng dụng nhé!", Toast.LENGTH_LONG).show();
            }

        }

    }

    public void swipeRight(int pos){
        AppUser temp = data.get(pos);
        Log.v("realm swipe", temp.getId() + " " + temp.getProfile().getName());
        ApplicationActivity.queryHelper.like(ApplicationActivity.user.getId(), temp.getId());
    }

    public void swipeLeft(int pos){
        AppUser temp = data.get(pos);
        Log.v("realm swipe", temp.getId() + " " + temp.getProfile().getName());
        ApplicationActivity.queryHelper.dislike(ApplicationActivity.user.getId(), temp.getId());
    }

}