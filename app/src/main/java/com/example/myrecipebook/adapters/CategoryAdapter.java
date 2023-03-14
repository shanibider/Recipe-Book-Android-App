package com.example.myrecipebook.adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipebook.R;
import com.example.myrecipebook.activities.DetailRecipeActivity;
import com.example.myrecipebook.models.CategoryModel;

import java.util.List;

//ADAPTER + VIEWHOLDER

//5.ADAPTER class (manage all the viewHolders)
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;
    List<CategoryModel> categoryList;

    //CTOR
    public CategoryAdapter(Context context, List<CategoryModel> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

     @Override
    //return viewHolder that contain view *object*, that create from match xml file, using inflater
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false ));
    }

    //Bind data to line
    //(bind the items with each item of the oneCategoryList list which than will be shown in recycler view)
    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageResource(categoryList.get(position).getImage());
        holder.name.setText(categoryList.get(position).getName());
        holder.detail.setText(categoryList.get(position).getDetail());


//this connect between recipe card (in category) and his own recipe detail
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Adapter position:" + holder.getAdapterPosition());

                Intent intent = new Intent(context, DetailRecipeActivity.class);
               //here i need to put what i have in detailrecipe
                intent.putExtra("Image", categoryList.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("Name", categoryList.get(holder.getAdapterPosition()).getName());
                intent.putExtra("Detail", categoryList.get(holder.getAdapterPosition()).getDetail());
                intent.putExtra("number", holder.getAdapterPosition());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }






    //ViewHolder inner class
//hold object of view of one line and save references to his elements
    public class ViewHolder extends RecyclerView.ViewHolder {

        //references to the views for each data item (from xml file)
        // ImageView imageView;
        TextView name, detail;
        ImageView imageView;
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.category_recipe_img);
            name = itemView.findViewById(R.id.category_recipe_name);
            detail =  itemView.findViewById(R.id.category_recipe_detail);
            cardView = itemView.findViewById(R.id.myCardView);
        }
    }
}


