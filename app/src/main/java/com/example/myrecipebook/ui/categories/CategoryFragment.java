package com.example.myrecipebook.ui.categories;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipebook.R;
import com.example.myrecipebook.adapters.CategoryAdapter;
import com.example.myrecipebook.models.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    RecyclerView recyclerView;
    List<CategoryModel> categoryModelList;
    CategoryAdapter categoryAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_category, container, false);


        recyclerView = root.findViewById(R.id.category_recipes_recList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        categoryModelList = new ArrayList<>();

        categoryModelList.add(new CategoryModel(R.drawable.oatpancake, "Oats pancakes", "10 min"));
        categoryModelList.add(new CategoryModel(R.drawable.oatmeal, "Oatmeal", "7 min"));
        categoryModelList.add(new CategoryModel(R.drawable.shake, "Fruit shake", "6 min"));
        categoryModelList.add(new CategoryModel(R.drawable.bowl, "Smoothie bowl", "5 min"));
        categoryModelList.add(new CategoryModel(R.drawable.crepe, "Oats crepe", "15 min"));


        categoryAdapter= new CategoryAdapter(getContext(), categoryModelList);
        recyclerView.setAdapter(categoryAdapter);

        categoryAdapter.notifyDataSetChanged();

        return root;
    }







}