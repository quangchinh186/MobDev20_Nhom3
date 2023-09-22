package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;
import java.util.Random;

import io.realm.mongodb.App;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class VerifyOtpActivity extends AppCompatActivity {
    String otpCode;
    String email;
    EditText otpInput;
    String SHARE_PREF = "share_pref";

    public void onVerify(View view) {
        String userOtp = otpInput.getText().toString();
        if (userOtp.equals(otpCode)) {
            LoginActivity.app.getEmailPassword().retryCustomConfirmationAsync(email, result -> {
                if (result.isSuccess()) {
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREF, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("recentEmail", email);
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "U good?", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Sai OTP rồi nè :(", Toast.LENGTH_SHORT).show();
        }
    }

    //----------------------------------------
    // It will generate 6 digit random Number.
    // from 0 to 999999
    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
    //-----------------------------------------
    //send code to email
    public void sendVerificationCode(String email){
        otpCode = getRandomNumberString();
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
            String message = "<h2 style='color:black;'>Hello con sói cô độc, </h2><br/>" +
                    "<div style='color:black;'>Nhập code này để hoàn tất đăng ký tài khoản nhé <3 </div><br/>" +
                    "<b style='color:blue;'>" + otpCode + "</b>" +
                    "<h3>Cheers!</h3>" +
                    "<p>Team Batosoft</p>";

            mimeMessage.setSubject("Đăng ký tài khoản BatoLove");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        otpInput = findViewById(R.id.otpInput);
        email = getIntent().getStringExtra("email");
        sendVerificationCode(email);
    }
}