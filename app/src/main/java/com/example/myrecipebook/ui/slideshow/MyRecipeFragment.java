package com.example.myrecipebook.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipebook.R;
import com.example.myrecipebook.adapters.CategoryAdapter;
import com.example.myrecipebook.adapters.MyRecipesAdapter;
import com.example.myrecipebook.models.DetailRecipeModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyRecipeFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    RecyclerView recyclerView;
    List<DetailRecipeModel> dataRecipeList;
    MyRecipesAdapter myRecipesAdapter;
    FloatingActionButton deleteButton, editButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_category, container, false);
        Bundle bundle = getArguments();
        String categoryName = "All";
        if (bundle != null) {
            categoryName = bundle.getString("categoryName");
        }
        Spinner spinner = root.findViewById(R.id.spinner_my_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.spinner_cate, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        recyclerView = root.findViewById(R.id.my_category_recipes_recList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataRecipeList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference recipeRef = database.getReference("Recipes");
        recipeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataRecipeList.clear();
                System.out.println("children count "+dataSnapshot.getChildrenCount());
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    String recipeName = recipeSnapshot.child("name").getValue(String.class);
                    String recipeUser = recipeSnapshot.child("user").getValue(String.class);
                    String user = recipeSnapshot.child("user").getValue(String.class);
                    String recipeTotalTime = "";
                    if (recipeSnapshot.child("totalTime").getValue(String.class).charAt(0) != '-')
                        recipeTotalTime = recipeSnapshot.child("totalTime").getValue(String.class);
                    String recipeIngredients = recipeSnapshot.child("ingredients").getValue(String.class);
                    String recipeInstruction = recipeSnapshot.child("instruction").getValue(String.class);
                    List<String> recipeCategory = recipeSnapshot.child("category").getValue(new GenericTypeIndicator<List<String>>() {});
                    List<String> recipeHealthLabels = recipeSnapshot.child("healthLabels").getValue(new GenericTypeIndicator<List<String>>() {});
                    String imageUrl = recipeSnapshot.child("imageUrl").getValue(String.class);
                    DetailRecipeModel recipeModel = new DetailRecipeModel(user, recipeName, recipeCategory, recipeHealthLabels, recipeIngredients, recipeInstruction, recipeTotalTime, imageUrl);
                    dataRecipeList.add(recipeModel);
                }
                filterList(spinner.getSelectedItem().toString());

                myRecipesAdapter.notifyDataSetChanged(); // update the adapter with the new data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError);
            }
        });
        myRecipesAdapter= new MyRecipesAdapter(getContext(), dataRecipeList);
        recyclerView.setAdapter(myRecipesAdapter);
        int index = adapter.getPosition(categoryName);
        spinner.setSelection(index);
        myRecipesAdapter.notifyDataSetChanged();
        return root;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

        String selectedCategory = parent.getItemAtPosition(position).toString();
        filterList(selectedCategory);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void filterList (String selectedCategory) {
        List<DetailRecipeModel> filteredRecipeList = new ArrayList<>();
        String curUser = FirebaseAuth.getInstance().getUid();
        for (DetailRecipeModel recipe : dataRecipeList) {
            if (recipe.getUser().equals(curUser)) {
                if (recipe.getCategory().contains(selectedCategory.toLowerCase()) || selectedCategory.equals("All")) {
                    filteredRecipeList.add(recipe);
                }
            }
        }
        myRecipesAdapter.updateList(filteredRecipeList);
    }
}