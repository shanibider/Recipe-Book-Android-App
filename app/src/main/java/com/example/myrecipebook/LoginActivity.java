package com.example.myrecipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void mainActivity(View view) {
        //start new activity on button click (open new screen)
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }
}