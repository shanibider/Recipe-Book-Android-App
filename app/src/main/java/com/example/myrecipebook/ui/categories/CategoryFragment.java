package com.example.myrecipebook.ui.categories;
import static java.lang.Thread.sleep;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipebook.R;
import com.example.myrecipebook.adapters.CategoryAdapter;
import com.example.myrecipebook.models.CategoryModel;
import com.example.myrecipebook.models.DetailRecipeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//List of recipes on one category
public class CategoryFragment extends Fragment {

    RecyclerView recyclerView;
    List<DetailRecipeModel> recipeList;
    CategoryAdapter categoryAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_category, container, false);


        recyclerView = root.findViewById(R.id.category_recipes_recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recipeList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference recipeRef = database.getReference("Recipes");
        recipeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeList.clear();
                System.out.println("children count "+dataSnapshot.getChildrenCount());
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    String recipeName = recipeSnapshot.child("name").getValue(String.class);
                    int recipeImage = recipeSnapshot.child("image").getValue(Integer.class);
                    String recipeTotalTime = "";
                    if (recipeSnapshot.child("totalTime").getValue(String.class).charAt(0) != '-')
                        recipeTotalTime = recipeSnapshot.child("totalTime").getValue(String.class);                    String recipeIngredients = recipeSnapshot.child("ingredients").getValue(String.class);
                    String recipeInstruction = recipeSnapshot.child("instruction").getValue(String.class);
                    List<String> recipeCategory = recipeSnapshot.child("category").getValue(new GenericTypeIndicator<List<String>>() {});
                    List<String> recipeHealthLabels = recipeSnapshot.child("healthLabels").getValue(new GenericTypeIndicator<List<String>>() {});
                    DetailRecipeModel recipeModel = new DetailRecipeModel(recipeImage, recipeName, recipeTotalTime, recipeIngredients, recipeInstruction, recipeCategory, recipeHealthLabels);
                    recipeList.add(recipeModel);
                }
                categoryAdapter.notifyDataSetChanged(); // update the adapter with the new data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError);
            }
        });
        System.out.println("list size " +recipeList.size());



        categoryAdapter= new CategoryAdapter(getContext(), recipeList);
        recyclerView.setAdapter(categoryAdapter);

        categoryAdapter.notifyDataSetChanged();

        return root;
    }
}