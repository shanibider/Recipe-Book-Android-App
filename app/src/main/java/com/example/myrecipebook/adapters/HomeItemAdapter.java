package com.example.myrecipebook.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipebook.R;
import com.example.myrecipebook.models.HomeItemModel;
import com.example.myrecipebook.ui.categories.CategoryFragment;
import com.example.myrecipebook.ui.home.HomeFragment;

import java.util.List;

//ADAPTER + VIEWHOLDER

//5.ADAPTER (manage all the viewholders)
public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemAdapter.ViewHolder> {

   Context context;
   List<HomeItemModel> list;


    public HomeItemAdapter(Context context, List<HomeItemModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    //create a new list row object= new view holder object (one line of view)
    //inside we inflate the view of itemView and return new ViewHolder object containing this layout
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflater create view object from xml file (home_item.xml)
        //then wrap it in view older object
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false));
    }

    //set the view holder properties according to the object is displayed (Bind data to line)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int itemPosition = position;
        holder.imageView.setImageResource(list.get(position).getImage());
        holder.name.setText(list.get(position).getName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("categoryName", list.get(itemPosition).getName());
                Navigation.findNavController(v).navigate(R.id.nav_categories, bundle);
            }
        });
    }

    //number of objects to display in the list
    @Override
    public int getItemCount() {
        return list.size();
    }

    //VIEWHOLDER
    //hold object of view of one line and save references to his elements (image & text)
    public class ViewHolder extends RecyclerView.ViewHolder {

        //references to the views for each data item
        ImageView imageView;
        TextView name;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView= itemView.findViewById(R.id.category_img);
            name= itemView.findViewById(R.id.category_title);
            cardView = itemView.findViewById(R.id.homeCard);
        }
    }
}



