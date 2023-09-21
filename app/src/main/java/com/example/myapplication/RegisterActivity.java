package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;
import java.util.Random;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
//Mailing package
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;


public class RegisterActivity extends AppCompatActivity {
  App app;
  EditText emailInput;
  EditText passwordInput;
  EditText passwordVerifyInput;
  String AppId = "mobileappdev-hwhug";
  String SHARE_PREF = "share_pref";
  String code;

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
    code = getRandomNumberString();
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
              "<b style='color:blue;'>" + code + "</b>" +
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
  //-----------------------------------------
  //send popup toast notification
  private void sendMessage(String msg) {
    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
  }
  //-----------------------------------------
  //clicking register
  public void onRegisterClick(View view) {
    emailInput = findViewById(R.id.email_regis_input);
    passwordInput = findViewById(R.id.password_regis_input);
    passwordVerifyInput = findViewById(R.id.verifyPassword_regis_input);
    if (emailInput.getText().equals("") ||
            passwordInput.getText().equals("")) {
      sendMessage("Bạn cần nhập đầy đủ thông tin");
      return;
    } else if (!passwordVerifyInput.getText().toString().equals(passwordInput.getText().toString())) {
      sendMessage("Xác nhận mật khẩu không trùng khớp!");
      return;
    }
//    loading view
    findViewById(R.id.loading_scene).setVisibility(View.VISIBLE);
//    register
    app.getEmailPassword().registerUserAsync(emailInput.getText().toString(), passwordInput.getText().toString(), it -> {
      if (it.isSuccess()) {
        //send toast
        sendMessage("Đăng ký thành công!");
        //send mail
        sendVerificationCode(emailInput.getText().toString());
        // cache email that recently login in app
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("recentEmail", emailInput.getText().toString());
        editor.apply();
        // change activity
        Intent verifyOtpAct = new Intent(getApplicationContext(), VerifyOtpActivity.class);
        verifyOtpAct.putExtra("email", emailInput.getText().toString());
        verifyOtpAct.putExtra("otp", this.code);
        findViewById(R.id.loading_scene).setVisibility(View.INVISIBLE);
        finish();
      } else {
        findViewById(R.id.loading_scene).setVisibility(View.INVISIBLE);
        sendMessage("Email đã tồn tại!");
      }
    });

  }

  //-----------------------------------------
  //default function
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    emailInput = findViewById(R.id.email_regis_input);
    passwordInput = findViewById(R.id.password_regis_input);
    passwordVerifyInput = findViewById(R.id.verifyPassword_regis_input);

    app = new App(new AppConfiguration.Builder(AppId).build());
  }
}