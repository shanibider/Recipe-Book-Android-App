package com.example.myrecipebook.ui.slideshow;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myrecipebook.R;


public class ProfileFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //inflate the layout for this fragment
        View root  = inflater.inflate(R.layout.activity_profile, container, false);



        return root;
    }

}