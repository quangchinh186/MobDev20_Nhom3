package com.example.myapplication.system;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class BatoSystem {
    public BatoSystem(){

    }
    private static SharedPreferences sharedPreferences;
    public static void initPref(Context context){
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences("share_pref", Activity.MODE_PRIVATE);
        }
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
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

    public static void sendVerificationCode(String email, String code){
        try {
            String senderEmail = "batosrsoft@gmail.com";
            String receiverEmail = email;
            String senderEmailPassword = "cvqducttiblezttq";
            String stringHost = "smtp.gmail.com";
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");
            jakarta.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderEmailPassword);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));
            String message =
                    "<h2 style='color:black;'>Hello con sói cô độc, </h2><br/>" +
                    "<div style='color:black;'>Nhập code này để xác minh email nhé <3 </div><br/>" +
                    "<b style='color:blue;'>" + code + "</b>" +
                    "<h3>Cheers!</h3>" +
                    "<p>Team Batosoft</p>";
            mimeMessage.setSubject("Quản lý tài khoản BatoLove");
            mimeMessage.setContent(message, "text/html; charset=utf-8");

            Thread thread = new Thread(() -> {
                try {
                    Transport.send(mimeMessage);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });
            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        finally {
            Log.v("notice", "send function complete running");
        }
    }

}
