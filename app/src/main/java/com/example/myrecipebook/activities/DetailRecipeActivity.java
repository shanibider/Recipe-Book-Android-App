package com.example.myrecipebook.activities;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipebook.R;
import com.example.myrecipebook.adapters.DetailRecipeAdapter;
import com.example.myrecipebook.models.DetailRecipeModel;

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

            detailModelList.add(new DetailRecipeModel(R.drawable.oatpancake, "Oats pancakes", "10 min", "40 gram oats, 1 banana, 1 egg, cinnamon, syrup mapel", "mix all ingredients together until smooth. fry on oily nd heated pan, enjoy."));
            detailModelList.add(new DetailRecipeModel(R.drawable.oatmeal, "Oatmeal", "5 min", "40 gram oats, cacao powder, cinnamon, syrup mapel, peanut butter, peanuts", "mix all ingredients together, and add water until all covered up. cook in the micro for 40 sec, spread peanut butter and peanuts on top, enjoy."));
            detailModelList.add(new DetailRecipeModel(R.drawable.shake, "Fruit shake", "6 min","blubberies, banana, cinnamon, protein powder, coconut milk, ice" ,"Combine all ingredients together and anjoy!"));
            detailModelList.add(new DetailRecipeModel(R.drawable.bowl, "smoothie bowl", "7 min", "blubberies, banana, cinnamon, protein powder, coconut milk, lots of ice, peanut butter, dash of water", "mix all ingredients together and anjoy."));
            detailModelList.add(new DetailRecipeModel(R.drawable.crepe, "Oat crepe", "15 min", "40 gram oats, 70 gram milk, cacao powder, cinnamon, syrup mapel, peanut butter, peanuts", "mix all ingredients together, and add water until all covered up. cook in the micro for 40 sec, spread peanut butter and peanuts on top, enjoy."));



            detailRecipeAdapter = new DetailRecipeAdapter(DetailRecipeActivity.this, detailModelList, position);
            recyclerView.setAdapter(detailRecipeAdapter);
            detailRecipeAdapter.notifyDataSetChanged();
        }
}

