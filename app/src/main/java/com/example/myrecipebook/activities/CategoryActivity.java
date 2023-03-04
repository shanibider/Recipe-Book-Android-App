package com.example.myrecipebook.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.myrecipebook.R;
import com.example.myrecipebook.adapters.CategoryAdapter;
import com.example.myrecipebook.models.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<CategoryModel> categoryModelList;
    CategoryAdapter categoryAdapter;
    ImageButton imageButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        recyclerView = findViewById(R.id.category_recipes_recList);
      //  imageButton = findViewById(R.id.category_recipe_img);

        //set the layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryModelList = new ArrayList<>();
        //specify an adapter
        categoryAdapter = new CategoryAdapter(categoryModelList);
        //Bind the adapter to the RecyclerView reference
        recyclerView.setAdapter(categoryAdapter);

    }
}