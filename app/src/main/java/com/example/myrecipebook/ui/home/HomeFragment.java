package com.example.myrecipebook.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipebook.R;
import com.example.myrecipebook.Upload_Recipe;
import com.example.myrecipebook.adapters.HomeItemAdapter;
import com.example.myrecipebook.databinding.FragmentHomeBinding;
import com.example.myrecipebook.models.HomeItemModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


//ATTACHING ADAPTER TO RECYCLERVIEW

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    //reference to RecyclerView
    RecyclerView homeItemRecList;
    //List
    List<HomeItemModel> HomeModelList;
    //Adapter (from HomeItemAdapter.java)
    HomeItemAdapter homeItemAdapter;


    //6.Attach the Adapter
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //attaching the variable 'homeItemRecList' (of RecyclerView type) to the Recyclerview in the layout
        homeItemRecList = root.findViewById(R.id.home_items_recList);


        HomeModelList = new LinkedList<>();

        HomeModelList.add(new HomeItemModel(R.drawable.breakfast, "breakfast"));
        HomeModelList.add(new HomeItemModel(R.drawable.lunch, "lunch"));
        HomeModelList.add(new HomeItemModel(R.drawable.dinner, "dinner"));
        HomeModelList.add(new HomeItemModel(R.drawable.dessert, "dessert"));
        HomeModelList.add(new HomeItemModel(R.drawable.snacks, "snacks"));

        //6.specify an adapter
        homeItemAdapter = new HomeItemAdapter(getActivity(),HomeModelList);
        //6.Bind the adapter to the RecyclerView reference
        homeItemRecList.setAdapter(homeItemAdapter);
        //2.use a layout manager
        homeItemRecList.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        //homeItemRecList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        homeItemRecList.setHasFixedSize(true);
        homeItemRecList.setNestedScrollingEnabled(false);

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    //Upload new recipe
    public void btn_uploadActivity(View view) {
    startActivity(new Intent(getActivity(), Upload_Recipe.class));

    }






}