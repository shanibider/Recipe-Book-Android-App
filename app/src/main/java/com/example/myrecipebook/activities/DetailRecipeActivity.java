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


        @SuppressLint("MissingInflatedId")
        protected void onCreate (Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.fragment_detailrecipe);

            Intent intent = getIntent();
            int position = intent.getExtras().getInt("number");

            recyclerView = (RecyclerView) findViewById(R.id.recipes_recList);

            recyclerView.setLayoutManager(new LinearLayoutManager(DetailRecipeActivity.this));

            detailModelList = new ArrayList<>();

//            detailModelList.add(new DetailRecipeModel(R.drawable.oatpancake, "Oats pancakes", "10 min", "40 gram oats, 1 banana, 1 egg, cinnamon, syrup mapel", "mix all ingredients together until smooth. fry on oily nd heated pan, enjoy.",null,null));
//            detailModelList.add(new DetailRecipeModel(R.drawable.oatmeal, "Oatmeal", "5 min", "40 gram oats, cacao powder, cinnamon, syrup mapel, peanut butter, peanuts", "mix all ingredients together, and add water until all covered up. cook in the micro for 40 sec, spread peanut butter and peanuts on top, enjoy.",null,null));
//            detailModelList.add(new DetailRecipeModel(R.drawable.shake, "Fruit shake", "6 min","blubberies, banana, cinnamon, protein powder, coconut milk, ice" ,"Combine all ingredients together and anjoy!",null,null));
//            detailModelList.add(new DetailRecipeModel(R.drawable.bowl, "smoothie bowl", "7 min", "blubberies, banana, cinnamon, protein powder, coconut milk, lots of ice, peanut butter, dash of water", "mix all ingredients together and anjoy.",null,null));
//            detailModelList.add(new DetailRecipeModel(R.drawable.crepe, "Oat crepe", "15 min", "40 gram oats, 70 gram milk, cacao powder, cinnamon, syrup mapel, peanut butter, peanuts", "mix all ingredients together, and add water until all covered up. cook in the micro for 40 sec, spread peanut butter and peanuts on top, enjoy.",null,null));

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference recipeRef = database.getReference("Recipes");
            recipeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    detailModelList.clear();
                    System.out.println("children count "+dataSnapshot.getChildrenCount());
                    for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                        String recipeName = recipeSnapshot.child("name").getValue(String.class);
                        int recipeImage = recipeSnapshot.child("image").getValue(Integer.class);
                        String recipeTotalTime = "";
                        if (recipeSnapshot.child("totalTime").getValue(String.class).charAt(0) != '-')
                            recipeTotalTime = recipeSnapshot.child("totalTime").getValue(String.class);
                        String recipeIngredients = recipeSnapshot.child("ingredients").getValue(String.class);
                        String recipeInstruction = recipeSnapshot.child("instruction").getValue(String.class);
                        List<String> recipeCategory = recipeSnapshot.child("category").getValue(new GenericTypeIndicator<List<String>>() {});
                        List<String> recipeHealthLabels = recipeSnapshot.child("healthLabels").getValue(new GenericTypeIndicator<List<String>>() {});
                        DetailRecipeModel recipeModel = new DetailRecipeModel(recipeImage, recipeName, recipeTotalTime, recipeIngredients, recipeInstruction, recipeCategory, recipeHealthLabels);
                        detailModelList.add(recipeModel);
                    }
                    detailRecipeAdapter.notifyDataSetChanged(); // update the adapter with the new data
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println(databaseError);
                }
            });
            System.out.println("list size " +detailModelList.size());

            detailRecipeAdapter = new DetailRecipeAdapter(DetailRecipeActivity.this, detailModelList, position);
            recyclerView.setAdapter(detailRecipeAdapter);
            detailRecipeAdapter.notifyDataSetChanged();
        }
}

