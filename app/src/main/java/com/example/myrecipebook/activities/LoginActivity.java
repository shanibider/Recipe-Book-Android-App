package com.example.myrecipebook.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myrecipebook.MainActivity;
import com.example.myrecipebook.R;

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