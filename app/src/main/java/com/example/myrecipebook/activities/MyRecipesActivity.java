package com.example.myrecipebook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipebook.DataClass;
import com.example.myrecipebook.R;
import com.example.myrecipebook.adapters.MyRecipesAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyRecipesActivity extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    MyRecipesAdapter myRecipesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);

        //References
        recyclerView = findViewById(R.id.myRecipesRecyclerView);



        fab = findViewById(R.id.fab1);

        //Upload new recipe button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyRecipesActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });

        //layout
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MyRecipesActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);


        AlertDialog.Builder builder = new AlertDialog.Builder(MyRecipesActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();

        //adapter
        MyRecipesAdapter adapter = new MyRecipesAdapter(MyRecipesActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        //firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("My Recipes");
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });



    } //onCreate closing tag

  }
