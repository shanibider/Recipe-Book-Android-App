package com.example.myrecipebook.ui.home;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipebook.R;
import com.example.myrecipebook.activities.UploadActivity;
import com.example.myrecipebook.adapters.HomeItemAdapter;
import com.example.myrecipebook.databinding.FragmentHomeBinding;
import com.example.myrecipebook.models.HomeItemModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;
import java.util.List;

//ATTACHING ADAPTER TO RECYCLERVIEW

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    RecyclerView homeItemRecList;
    List<HomeItemModel> HomeModelList;
    HomeItemAdapter homeItemAdapter;

    FloatingActionButton fab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeItemRecList = root.findViewById(R.id.home_items_recList);

        fab = root.findViewById(R.id.fab1);

        //Upload new recipe button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UploadActivity.class);
                startActivity(intent);
            }
        });


        HomeModelList = new LinkedList<>();

        HomeModelList.add(new HomeItemModel(R.drawable.breakfast, "Breakfast"));
        HomeModelList.add(new HomeItemModel(R.drawable.lunch, "Lunch"));
        HomeModelList.add(new HomeItemModel(R.drawable.dinner, "Dinner"));
        HomeModelList.add(new HomeItemModel(R.drawable.dessert, "Dessert"));

        homeItemAdapter = new HomeItemAdapter(getActivity(),HomeModelList);
        homeItemRecList.setAdapter(homeItemAdapter);
        homeItemRecList.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        homeItemRecList.setHasFixedSize(true);
        homeItemRecList.setNestedScrollingEnabled(false);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}