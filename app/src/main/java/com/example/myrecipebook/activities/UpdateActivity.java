package com.example.myrecipebook.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myrecipebook.R;
import com.example.myrecipebook.models.DetailRecipeModel;
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

import java.util.ArrayList;
import java.util.List;

//work well
public class UpdateActivity extends AppCompatActivity {
    ImageView updateImage;
    Button updatesaveButton;
    TextView updateName;
    EditText updateIngre, uploadLang;
    String imageURL;
    NumberPicker updateTotalTime;
    Uri uri;
    DetailRecipeModel detailRecipeModel;
    private CheckBox breakfastCheckBox, lunchCheckBox, dinnerCheckBox, dessertCheckBox;
    private CheckBox veganCB, vegetarianCB, kosherCB, glutenCB, dairyCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update);

        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            detailRecipeModel = (DetailRecipeModel) extras.getSerializable("detailRecipeModel");
        }
        if (detailRecipeModel == null) {
            finish();
        }
        updateImage = findViewById(R.id.updateImage);
        updateName = findViewById(R.id.update_name);
        updateIngre = findViewById(R.id.update_ingredients);
        updateTotalTime = findViewById(R.id.updateTotalTime);
        updateTotalTime.setMinValue(0);
        updateTotalTime.setMaxValue(200);
        updatesaveButton = findViewById(R.id.update_saveButton);

        updateName.setText(detailRecipeModel.getName());
        updateIngre.setText(detailRecipeModel.getIngredients());
        if (Character.isDigit(detailRecipeModel.getTotalTime().charAt(0))){
            updateTotalTime.setValue(detailRecipeModel.getTotalTime().charAt(0));
        }

        breakfastCheckBox = findViewById(R.id.update_breakfast);
        lunchCheckBox = findViewById(R.id.update_lunch);
        dinnerCheckBox = findViewById(R.id.update_dinner);
        dessertCheckBox = findViewById(R.id.update_dessert);
        List<String> category = detailRecipeModel.getCategory();
        if (category.contains("breakfast")) breakfastCheckBox.setChecked(true);
        if (category.contains("lunch")) lunchCheckBox.setChecked(true);
        if (category.contains("dinner")) dinnerCheckBox.setChecked(true);
        if (category.contains("dessert")) dessertCheckBox.setChecked(true);

        veganCB = findViewById(R.id.update_vegan);
        vegetarianCB = findViewById(R.id.update_vegetarian);
        kosherCB = findViewById(R.id.update_kosher);
        glutenCB = findViewById(R.id.update_gluten);
        dairyCB = findViewById(R.id.update_dairy);

        List<String> healthLabels = detailRecipeModel.getHealthLabels();
        if (healthLabels.contains("Vegetarian")) vegetarianCB.setChecked(true);
        if (healthLabels.contains("Vegan")) veganCB.setChecked(true);
        if (healthLabels.contains("Kosher")) kosherCB.setChecked(true);
        if (healthLabels.contains("Gluten-Free")) glutenCB.setChecked(true);
        if (healthLabels.contains("Dairy-Free")) dairyCB.setChecked(true);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {

                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            updateImage.setImageURI(uri);
                        } else {
                            Toast.makeText(UpdateActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        //Upload recipe image button
        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        //Save data button
        updatesaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    public void saveData() {
        List<String> category = new ArrayList<>();
        if (breakfastCheckBox.isChecked()) category.add("breakfast");
        if (lunchCheckBox.isChecked()) category.add("lunch");
        if (dinnerCheckBox.isChecked()) category.add("dinner");
        if (dessertCheckBox.isChecked()) category.add("dessert");

        List<String> healthLabels = new ArrayList<>();
        healthLabels.add("healthLabels");
        if (vegetarianCB.isChecked()) healthLabels.add("Vegetarian");
        if (veganCB.isChecked()) healthLabels.add("Vegan");
        if (kosherCB.isChecked()) healthLabels.add("Kosher");
        if (glutenCB.isChecked()) healthLabels.add("Gluten-Free");
        if (dairyCB.isChecked()) healthLabels.add("Dairy-Free");
        String name = updateName.getText().toString();
        String ingredients = updateIngre.getText().toString();
        int int_totalTime = updateTotalTime.getValue();
        String totalTime = Integer.toString(int_totalTime) + " min";
        String curUser = FirebaseAuth.getInstance().getUid();
        DetailRecipeModel detailRecipeModel = new DetailRecipeModel(curUser, name, category, healthLabels, ingredients, "", totalTime, "");

        if (uri != null) {
            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("recipe_images/" + name + ".jpg");
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
            Toast.makeText(UpdateActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

}