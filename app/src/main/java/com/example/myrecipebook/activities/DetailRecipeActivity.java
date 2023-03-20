package com.example.myrecipebook.activities;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipebook.R;
import com.example.myrecipebook.adapters.DetailRecipeAdapter;
import com.example.myrecipebook.models.DetailRecipeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailRecipeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<DetailRecipeModel> detailModelList;
    DetailRecipeAdapter detailRecipeAdapter;


        protected void onCreate (Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.fragment_detailrecipe);

            Intent intent = getIntent();
            int position = intent.getExtras().getInt("number");
            detailModelList = (List<DetailRecipeModel>) intent.getSerializableExtra("recipeList");
            recyclerView = (RecyclerView) findViewById(R.id.recipes_recList);

            recyclerView.setLayoutManager(new LinearLayoutManager(DetailRecipeActivity.this));

            detailRecipeAdapter = new DetailRecipeAdapter(DetailRecipeActivity.this, detailModelList.get(position), position);
            recyclerView.setAdapter(detailRecipeAdapter);
            detailRecipeAdapter.notifyDataSetChanged();
        }
}

