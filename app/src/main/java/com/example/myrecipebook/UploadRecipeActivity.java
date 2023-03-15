package com.example.myrecipebook;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myrecipebook.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadRecipeActivity extends AppCompatActivity {

    Uri uri;

    Button BtnUploadImage;
    Button BtUploadRecipe;
    EditText uploadRecipeName, uploadRecipePreparation, uploadRecipeIngredients, uploadRecipeInstructions;
    ProgressBar progressBar;
    ImageView recipeImage;
    Uri imageUri;

    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Images");
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    ActivityMainBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_recipe);

        BtnUploadImage =  findViewById(R.id.btn_upload_image);
        BtUploadRecipe =  findViewById(R.id.btn_upload_recipe);
        recipeImage =  findViewById(R.id.upload_recipe_image);
        recipeImage =  findViewById(R.id.upload_recipe_image);
        progressBar =  findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        uploadRecipeName =  findViewById(R.id.upload_recipe_name);
        uploadRecipePreparation =  findViewById(R.id.upload_recipe_preparation);
        uploadRecipeIngredients =  findViewById(R.id.upload_recipe_ingredients);
        uploadRecipeInstructions  =  findViewById(R.id.upload_recipe_instruction);


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        imageUri = data.getData();
                        recipeImage.setImageURI(imageUri);
                    } else {
                        Toast.makeText(UploadRecipeActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
       );

    //Upload recipe image button
        BtnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

    //Upload recipe button
        BtUploadRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null){
                    uploadToFirebase(imageUri);
               } else {
                Toast.makeText(UploadRecipeActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
               }
            }
        });

    }



    //outside onCreate
    //Upload recipe to Firebase DB
    private void uploadToFirebase(Uri uri){
        String RecipeName = uploadRecipeName.getText().toString();

        final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        imageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {
                        DataClass dataClass = new DataClass(uri.toString(), RecipeName);

                        String key = databaseReference.push().getKey();
                        databaseReference.child(key).setValue(dataClass);

                        progressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(UploadRecipeActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UploadRecipeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(UploadRecipeActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
}


