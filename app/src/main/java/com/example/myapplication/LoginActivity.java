package com.example.myapplication;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
<<<<<<< Updated upstream
=======
    String AppId = "mobileappdev-hwhug";
    App app;
    String email;
    String password;

    protected void onLogIn(String email, String password) {
        if (email == null || password == null) {
            Toast.makeText(getApplicationContext(), "You suck!", Toast.LENGTH_SHORT).show();
            return;
        }

        Credentials credentials = Credentials.emailPassword(email, password);
        Log.v("credentials", credentials.asJson());
        app.loginAsync(credentials, it -> {
            if(it.isSuccess()){
                Log.v("TEST_LOGIN", "login successfully");
                /* change activity */
                Intent application = new Intent(getApplicationContext(), ApplicationActivity.class);
                application.putExtra("email", LoginActivity.this.email);
                application.putExtra("password", LoginActivity.this.password);
                startActivity(application);
            }
            else {
                Log.v("TEST_LOGIN", "login failed");
            }
        });
    }

    private void startEmailChangeListener() {
        EditText emailInput = findViewById(R.id.email__input);
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LoginActivity.this.email = emailInput.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void startPasswordChangeListener() {
        EditText passwordInput = findViewById(R.id.pasword__input);
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                LoginActivity.this.password = passwordInput.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

>>>>>>> Stashed changes
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
