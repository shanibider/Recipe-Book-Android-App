package com.example.myrecipebook.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myrecipebook.R;
import com.example.myrecipebook.models.HelperClass;
import com.example.myrecipebook.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword;
    private EditText signupName, signupUsername;

    private Button signupButton;
    private TextView loginRedirectText;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        signupEmail = findViewById(R.id.register_email);
        signupPassword = findViewById(R.id.register_password);
        signupButton = findViewById(R.id.register_button);
        loginRedirectText = findViewById(R.id.not_yet_register);
        signupName = findViewById(R.id.register_name);
        signupUsername = findViewById(R.id.register_username);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();

                if (name.isEmpty()){
                    signupEmail.setError("Email cannot be empty");
                }
                if (password.isEmpty()){
                    signupPassword.setError("Password cannot be empty");
                } else{

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                //Save to Firebase Realtime Database
                                database = FirebaseDatabase.getInstance();
                                reference = database.getReference("users");
                                HelperClass helperClass = new HelperClass(name, email, username, password);
                                reference.child(username).setValue(helperClass);

                                Toast.makeText(RegisterActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(RegisterActivity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}


