package com.example.myrecipebook.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myrecipebook.MainActivity;
import com.example.myrecipebook.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

//My Detailed recipe
public class MyDetailRecipe extends AppCompatActivity {

    TextView detailTitle, detailIngr, detailInst;
    ImageView detailImage;
    String key = "";
    String imageUrl = "";
    FloatingActionButton deleteButton, editButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_detaile_recipe);

        detailTitle = findViewById(R.id.detailTitle);
        detailImage = findViewById(R.id.detailImage);
        detailIngr = findViewById(R.id.detailIngr);
        detailInst = findViewById(R.id.detailInst);

        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null){

            detailIngr.setText(bundle.getString("ingredients"));
            detailTitle.setText(bundle.getString("Title"));
            detailInst.setText(bundle.getString("Instructions"));
            detailInst.setText(bundle.getString("Instructions"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);

        }
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyDetailRecipe.this, MyRecipeUpdateActivity.class)
                        .putExtra("Title", detailTitle.getText().toString())
                        .putExtra("ingredients", detailIngr.getText().toString())
                        .putExtra("Instructions", detailInst.getText().toString())
                        .putExtra("Image", imageUrl)
                        .putExtra("Key", key);
                startActivity(intent);
            }
        });

    }
}





/*
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android Tutorials");
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainnActivity.class));
                        finish();
                    }
                });
            }
        });


*/











