package com.example.myrecipebook.adapters;

//ADAPTER + VIEWHOLDER

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipebook.R;
import com.example.myrecipebook.activities.CategoryActivity;
import com.example.myrecipebook.models.CategoryModel;

import java.util.List;

//5.ADAPTER class (manage all the viewholders)
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    List<CategoryModel> list;

    //CTOR
    public CategoryAdapter(List<CategoryModel> oneCategoryList) {
        this.list = list;
    }


    @NonNull
    @Override
    //return viewHolder that contain view *object*, that create from match xml file, using inflater
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false ));
    }

    //Bind data to line
    //(bind the items with each item of the oneCategoryList list which than will be shown in recycler view)
    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
    holder.imageView.setImageResource(list.get(position).getImage());
        holder.name.setText(list.get(position).getName());
        holder.detail.setText(list.get(position).getDetail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }





//ViewHolder inner class
//hold object of view of one line and save references to his elements
    public class ViewHolder extends RecyclerView.ViewHolder {

        //references to the views for each data item (from xml file)
        ImageView imageView;
        TextView name, detail;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.category_img);
            name = itemView.findViewById(R.id.category_recipe_name);
            detail =  itemView.findViewById(R.id.category_recipe_detail);

        }
    }
}
