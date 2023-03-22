package com.example.myrecipebook.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myrecipebook.R;
import com.example.myrecipebook.models.UserData;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class RegisterActivity extends AppCompatActivity {

    ImageView imageSelectButton;
    Uri uri;

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

        imageSelectButton = findViewById(R.id.reg_btnChooseImage);
        reference = FirebaseDatabase.getInstance().getReference("users");



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
                                UserData helperClass = new UserData(name, email, username, "");
                                reference.child(auth.getUid()).setValue(helperClass);

                                Toast.makeText(RegisterActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(RegisterActivity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                uploadImage();

            }
        });


        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });






        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {

                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            imageSelectButton.setImageURI(uri);
                        } else {

                        }
                    }
                }
        );


        imageSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
    }

    private void uploadImage()
    {

        if(reference == null || uri == null)
        {
            return;
        }

        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("profile_images/" + auth.getUid() + ".jpg");
        UploadTask uploadTask = imageRef.putFile(uri);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    if (downloadUri != null) {
                        String imageURL = downloadUri.toString();
                        reference.child(auth.getUid()).child("profileImage").setValue(imageURL);
                    }
                }
            }
        });
    }










}



