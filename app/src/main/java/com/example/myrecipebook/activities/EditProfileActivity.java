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
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.myrecipebook.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditProfileActivity extends AppCompatActivity {

    EditText editName, editUsername;
    Button saveButton;
    DatabaseReference reference;
    FirebaseAuth auth;
    ImageView imageSelectButton;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();

        editName = findViewById(R.id.editName);
        editUsername = findViewById(R.id.editUsername);
        saveButton = findViewById(R.id.saveButton);
        imageSelectButton = findViewById(R.id.btnChooseImage);

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

    void returnActivity()
    {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        reference = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();
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
                        returnActivity();
                    }
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
            uploadImage();
            return true;
        }
    }
}