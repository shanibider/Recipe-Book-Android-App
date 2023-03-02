package com.example.myrecipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //loading xml file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }


    public void mainActivity(View view) {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
    }
}