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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

//work well
public class UploadActivity extends AppCompatActivity {
    ImageView uploadImage;
    Button saveButton;
    EditText uploadName, uploadIngre, uploadLang;
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
        //uploadDesc = findViewById(R.id.uploadDesc);
        uploadName = findViewById(R.id.upload_name);
        //uploadLang = findViewById(R.id.uploadLang);
        uploadIngre = findViewById(R.id.upload_ingredients);
        uploadTotalTime = findViewById(R.id.uploadTotalTime);
        uploadTotalTime.setMinValue(0);
        uploadTotalTime.setMaxValue(200);
        saveButton = findViewById(R.id.saveButton);


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {

                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
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

        if (breakfastCheckBox.isChecked()) {
            category.add("breakfast");
        }
        if (lunchCheckBox.isChecked()) {
            category.add("lunch");
        }
        if (dinnerCheckBox.isChecked()) {
            category.add("dinner");
        }
        if (dessertCheckBox.isChecked()) {
            category.add("dessert");
        }
        List<String> healthLabels = new ArrayList<>();
        veganCB = findViewById(R.id.vegan_checkbox);
        vegetarianCB = findViewById(R.id.vegetarian_checkbox);
        kosherCB = findViewById(R.id.kosher_checkbox);
        glutenCB = findViewById(R.id.gluten_checkbox);
        dairyCB = findViewById(R.id.dairy_checkbox);
        if (vegetarianCB.isChecked()) healthLabels.add("Vegetarian");
        if (veganCB.isChecked()) healthLabels.add("Vegan");
        if (kosherCB.isChecked()) healthLabels.add("Kosher");
        if (glutenCB.isChecked()) healthLabels.add("Gluten-Free");
        if (dairyCB.isChecked()) healthLabels.add("Dairy-Free");
        String name = uploadName.getText().toString();
        String ingredients = uploadIngre.getText().toString();
        int int_totalTime = uploadTotalTime.getValue();
        String totalTime = Integer.toString(int_totalTime) + " min";
        int image = 0;
        DetailRecipeModel detailRecipeModel = new DetailRecipeModel(image, name, totalTime, ingredients, "", category, healthLabels);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference recipeRef = database.getReference("Recipes");
        recipeRef.addValueEventListener(new ValueEventListener() {
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
                        DatabaseReference recipeChildRef = recipeRef.child(detailRecipeModel.name);
                        recipeChildRef.setValue(detailRecipeModel);
                        Toast.makeText(getApplicationContext(), "Recipe uploaded successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Recipe name is already taken", Toast.LENGTH_SHORT).show();
                }
                recipeRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });
    }
}
//    public void saveData() {
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images")
//                .child(uri.getLastPathSegment());
//        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
//        builder.setCancelable(false);
//        builder.setView(R.layout.progress_layout);
//        AlertDialog dialog = builder.create();
//        dialog.show();
//        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                while (!uriTask.isComplete());
//                Uri urlImage = uriTask.getResult();
//                imageURL = urlImage.toString();
//                uploadData();
//                dialog.dismiss();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                dialog.dismiss();
//            }
//        });
//    }



//    public void uploadData() {
//        String title = uploadTopic.getText().toString();
//        String desc = uploadDesc.getText().toString();
//        String lang = uploadLang.getText().toString();
//        DataClass dataClass = new DataClass(title, desc, lang, imageURL);
//
//        //We are changing the child from title to currentDate,
//        // because we will be updating title as well and it may affect child value.
//        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
//
//        FirebaseDatabase.getInstance().getReference("My Recipes").child(currentDate)
//                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
//
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
//                            finish();
//
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
