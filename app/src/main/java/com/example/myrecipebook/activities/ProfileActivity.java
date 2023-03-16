package com.example.myrecipebook.activities;

import android.content.Intent;
import android.os.Bundle;
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

    TextView profileEmail, profilePassword;
    TextView titleName, titleHello;
    Button editProfile, foodPref, myRecipes, myFavourites, findMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileEmail = findViewById(R.id.profileEmail);
        profilePassword = findViewById(R.id.profilePassword);
        titleName = findViewById(R.id.titleName);
        titleHello = findViewById(R.id.titleHello);


        showUserData();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        });

    }


    public void showUserData() {
        //
        Intent intent = getIntent();

        String emailUser = intent.getStringExtra("email");
        String passwordUser = intent.getStringExtra("password");

        profileEmail.setText(emailUser);
        profilePassword.setText(passwordUser);
    }

    public void passUserData(){
        String userUserEmail = profileEmail.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(userUserEmail);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String nameFromDB = snapshot.child(userUserEmail).child("name").getValue(String.class);
                    String emailFromDB = snapshot.child(userUserEmail).child("email").getValue(String.class);
                    String usernameFromDB = snapshot.child(userUserEmail).child("username").getValue(String.class);
                    String passwordFromDB = snapshot.child(userUserEmail).child("password").getValue(String.class);

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
