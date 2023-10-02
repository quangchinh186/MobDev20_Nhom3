package com.example.myapplication.system;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class BatoSystem {
    public BatoSystem(){

    }
    private static SharedPreferences sharedPreferences;
    public static void initPref(Context context){
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences("share_pref", Activity.MODE_PRIVATE);
        }
    }

    public static String readString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public static void writeString(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static boolean readBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static void writeBoolean(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public static Integer readInteger(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public static void writeInteger(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(key, value).commit();
    }

    public static void loadImageFromUrl(ImageView imageView, String url){
        Picasso.get().load(url).into(imageView);
    }

    public static void sendMessage(String msg, Context context){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
