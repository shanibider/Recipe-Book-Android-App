package com.example.myrecipebook.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myrecipebook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class EditProfileActivity extends AppCompatActivity {

    EditText editName, editUsername;
    Button saveButton;
    DatabaseReference reference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();

        editName = findViewById(R.id.editName);
        editUsername = findViewById(R.id.editUsername);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDataChanged()){
                    Toast.makeText(EditProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "No Changes Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isDataChanged() {
        String newName = editName.getText().toString();
        String newUsername = editUsername.getText().toString();

        if (auth == null || auth.getUid() == null || newName.isEmpty() || newUsername.isEmpty()){
            return false;
        } else {
            reference.child(auth.getUid()).child("name").setValue(newName);
            reference.child(auth.getUid()).child("username").setValue(newUsername);
            return true;
        }
    }
}