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

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    RecyclerView homeItemRec;
    List<HomeItemModel> HomeModelList;
    HomeItemAdapter homeItemAdapter;
    //(from HomeItemAdapter.java)


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeItemRec = root.findViewById(R.id.home_items_rec);

        HomeModelList = new ArrayList<>();

        HomeModelList.add(new HomeItemModel(R.drawable.breakfast, "breakfast"));
        HomeModelList.add(new HomeItemModel(R.drawable.lunch, "lunch"));
        HomeModelList.add(new HomeItemModel(R.drawable.dinner, "dinner"));
        HomeModelList.add(new HomeItemModel(R.drawable.salad, "salad"));
        HomeModelList.add(new HomeItemModel(R.drawable.dessert, "dessert"));
        HomeModelList.add(new HomeItemModel(R.drawable.cake, "cake"));
        HomeModelList.add(new HomeItemModel(R.drawable.snacks, "snacks"));

        homeItemAdapter = new HomeItemAdapter(getActivity(),HomeModelList);
        homeItemRec.setAdapter(homeItemAdapter);
        homeItemRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        homeItemRec.setHasFixedSize(true);
        homeItemRec.setNestedScrollingEnabled(false);



        homeItemRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false ));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}