package com.example.myrecipebook.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipebook.R;
import com.example.myrecipebook.adapters.HomeItemAdapter;
import com.example.myrecipebook.databinding.FragmentHomeBinding;
import com.example.myrecipebook.models.HomeItemModel;

import java.util.ArrayList;
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

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //attaching the reference (of RecyclerView) to the Recyclerview in the layout
        homeItemRecList = root.findViewById(R.id.home_items_recList);

        HomeModelList = new ArrayList<>();

        HomeModelList.add(new HomeItemModel(R.drawable.breakfast, "breakfast"));
        HomeModelList.add(new HomeItemModel(R.drawable.lunch, "lunch"));
        HomeModelList.add(new HomeItemModel(R.drawable.dinner, "dinner"));
        HomeModelList.add(new HomeItemModel(R.drawable.salad, "salad"));
        HomeModelList.add(new HomeItemModel(R.drawable.dessert, "dessert"));
        HomeModelList.add(new HomeItemModel(R.drawable.cake, "cake"));
        HomeModelList.add(new HomeItemModel(R.drawable.snacks, "snacks"));

        //6.specify an adapter
        homeItemAdapter = new HomeItemAdapter(getActivity(),HomeModelList);
        //6.Bind the adapter to the list
        homeItemRecList.setAdapter(homeItemAdapter);
        //2.use a linear layout manager
        homeItemRecList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
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