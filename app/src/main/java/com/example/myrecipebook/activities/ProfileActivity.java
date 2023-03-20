package com.example.myrecipebook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myrecipebook.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

//not working well

public class ProfileActivity extends AppCompatActivity {

    TextView profileName, profileUsername ,profileEmail, profilePassword;
    TextView titleHello;
    Button editProfile, foodPref, myRecipes, myFavourites, findMap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        profileName = findViewById(R.id.profileName);
        profileUsername = findViewById(R.id.profileUsername);
        profileEmail = findViewById(R.id.profileEmail);
        profilePassword = findViewById(R.id.profilePassword);
        titleHello = findViewById(R.id.titleHello);
        editProfile = findViewById(R.id.edit_profile);

        showAllUserData();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        });
    }


    public void showAllUserData(){

        //creates a new Intent object to pull the Intent that started the current activity
        Intent intent = getIntent();
        //pull a string value associated with the key "name" from the Intent object. This key-value pair was passed from the previous activity to this activity using the putExtra() method
        String nameUser = intent.getStringExtra("name");
        String usernameUser = intent.getStringExtra("username");
        String emailUser = intent.getStringExtra("email");
        String passwordUser = intent.getStringExtra("password");

        //sets the pulled email address to a TextView object named profileEmail
        profileName.setText(nameUser);
        profileUsername.setText(usernameUser);
        profileEmail.setText(emailUser);
        profilePassword.setText(passwordUser);
        titleHello.setText(nameUser);
    }



    public void passUserData(){

        String userUsername = profileUsername.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                    String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                    String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("username", usernameFromDB);
                    intent.putExtra("password", passwordFromDB);
                    startActivity(intent);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





}
