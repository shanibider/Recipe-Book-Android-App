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
import com.example.myrecipebook.UploadRecipeActivity;
import com.example.myrecipebook.adapters.HomeItemAdapter;
import com.example.myrecipebook.databinding.FragmentHomeBinding;
import com.example.myrecipebook.models.HomeItemModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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


    FloatingActionButton fab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeItemRecList = root.findViewById(R.id.home_items_recList);

        fab= root.findViewById(R.id.btn_uploadActivity);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UploadRecipeActivity.class);
                startActivity(intent);
            }
        });


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
        startActivity(new Intent(getActivity(), UploadRecipeActivity.class));
    }



}







    /*
    holder.cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, CategoryFragment.class);
            context.startActivity(intent);
        }
    });
*/

