package com.example.myrecipebook;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.myrecipebook.databinding.ActivityMainBinding;

public class UploadRecipeActivity extends AppCompatActivity {

    ImageView recipeImage;
    Uri uri;
    Button uploadPhoto;

    ImageView uploadImage;
    EditText uploadRecipeName, uploadRecipePreparation, uploadRecipeIngredients, uploadRecipeInstructions;
    ProgressBar progressBar;
    ImageView uploadRecipeImage;
    Uri imageUri;

    ActivityMainBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_recipe);

        recipeImage = (ImageView) findViewById(R.id.upload_recipe_image);
        uploadPhoto =  findViewById(R.id.upload_image);

        activityResultLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    uri = result.getData().getData();
                    recipeImage.setImageURI(uri);
                }
            }
        });

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
               activityResultLauncher.launch(photoPicker);
            }
        });
    }
}
    /*
    public void btnSelectImage(View view) {
        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            uri = data.getData();
            recipeImage.setImageURI(uri);
        } else Toast.makeText(this, "You haven't picked image", Toast.LENGTH_SHORT).show();
    }
}
*/
