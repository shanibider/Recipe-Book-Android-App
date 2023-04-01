package com.example.myrecipebook.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.myrecipebook.DataClass;
import com.example.myrecipebook.R;
import com.example.myrecipebook.models.DetailRecipeModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//allows users to upload recipe details and images to a Firebase Realtime Database
public class UploadActivity extends AppCompatActivity {
    ImageView uploadImage;
    Button saveButton;
    EditText uploadName, uploadIngre;
    String imageURL;
    NumberPicker uploadTotalTime;
    Uri uri;
    private CheckBox breakfastCheckBox, lunchCheckBox, dinnerCheckBox, dessertCheckBox;
    private CheckBox veganCB, vegetarianCB, kosherCB, glutenCB, dairyCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_upload);

        uploadImage = findViewById(R.id.uploadImage);
        uploadName = findViewById(R.id.upload_name);
        uploadIngre = findViewById(R.id.upload_ingredients);
        uploadTotalTime = findViewById(R.id.uploadTotalTime);
        uploadTotalTime.setMinValue(0);
        uploadTotalTime.setMaxValue(120);
        saveButton = findViewById(R.id.saveButton);

        //ActivityResultLauncher takes Intent and receive its result
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {

                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            //gets the Intent that was returned from the activity.
                            Intent data = result.getData();
                            // gets the URI of the image that was selected by the user and sets it to the uri variable
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        } else {
                            Toast.makeText(UploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        //Upload recipe image button
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        //Save data button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    public void saveData() {
        List<String> category = new ArrayList<>();
        breakfastCheckBox = findViewById(R.id.breakfast_checkbox);
        lunchCheckBox = findViewById(R.id.lunch_checkbox);
        dinnerCheckBox = findViewById(R.id.dinner_checkbox);
        dessertCheckBox = findViewById(R.id.dessert_checkbox);
        if (breakfastCheckBox.isChecked()) category.add("breakfast");
        if (lunchCheckBox.isChecked()) category.add("lunch");
        if (dinnerCheckBox.isChecked()) category.add("dinner");
        if (dessertCheckBox.isChecked()) category.add("dessert");

        List<String> healthLabels = new ArrayList<>();
        veganCB = findViewById(R.id.vegan_checkbox);
        vegetarianCB = findViewById(R.id.vegetarian_checkbox);
        kosherCB = findViewById(R.id.kosher_checkbox);
        glutenCB = findViewById(R.id.gluten_checkbox);
        dairyCB = findViewById(R.id.dairy_checkbox);
        healthLabels.add("healthLabels");
        if (vegetarianCB.isChecked()) healthLabels.add("Vegetarian");
        if (veganCB.isChecked()) healthLabels.add("Vegan");
        if (kosherCB.isChecked()) healthLabels.add("Kosher");
        if (glutenCB.isChecked()) healthLabels.add("Gluten-Free");
        if (dairyCB.isChecked()) healthLabels.add("Dairy-Free");

        String name = uploadName.getText().toString();
        String ingredients = uploadIngre.getText().toString();
        int int_totalTime = uploadTotalTime.getValue();

        String totalTime = Integer.toString(int_totalTime) + " min";

        //retrieves the current user's ID using Firebase Authentication
        String curUser = FirebaseAuth.getInstance().getUid();
        //creates a DetailRecipeModel object with including the current user's ID and recipe details
        DetailRecipeModel detailRecipeModel = new DetailRecipeModel(curUser, name, category, healthLabels, ingredients, "", totalTime, "");

        //If a URI for image is provided, it creates a reference to a Firebase Storage location for the image, and uploads the image using an UploadTask
        if (uri != null) {
            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("recipe_images/" + name + ".jpg");
            UploadTask uploadTask = imageRef.putFile(uri);

            //called on the UploadTask to retrieve the download URL for the uploaded image
            //This method takes a Continuation as a parameter, which is used to process the TaskSnapshot returned by the upload task and return a Task that resolves to the download URL
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                }
                //When the download URL task completes, the OnCompleteListener is called, which retrieves the download URL and sets it as the imageUrl property of the DetailRecipeModel
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (downloadUri != null) {
                            String imageURL = downloadUri.toString();
                            detailRecipeModel.imageUrl = imageURL;
                            System.out.println("imageURL");
                            System.out.println(imageURL);

                            // Save recipe data to Firebase Realtime Database
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference recipeRef = database.getReference("Recipes");

                            recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    boolean flag = false;
                                    if (snapshot.exists()) {
                                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                            if (childSnapshot.getKey().equals(detailRecipeModel.getName())) {
                                                flag = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (!flag) {
                                        if (name.equals(""))
                                            Toast.makeText(getApplicationContext(), "Please set recipe name", Toast.LENGTH_SHORT).show();
                                        else if (ingredients.equals(""))
                                            Toast.makeText(getApplicationContext(), "Please set ingredients", Toast.LENGTH_SHORT).show();
                                        else if (int_totalTime == 0)
                                            Toast.makeText(getApplicationContext(), "Please set total time", Toast.LENGTH_SHORT).show();
                                        else if (category.size() == 0)
                                            Toast.makeText(getApplicationContext(), "Please choose at least 1 category", Toast.LENGTH_SHORT).show();
                                        else {
                                            //If recipe doesn't exists, it adds the new recipe to the database
                                            DatabaseReference recipeChildRef = recipeRef.child(detailRecipeModel.getName());
                                            recipeChildRef.setValue(detailRecipeModel);
                                            Toast.makeText(getApplicationContext(), "Recipe uploaded successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Recipe name is already taken", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    System.out.println(error);
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to get download URL", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(UploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }
}
