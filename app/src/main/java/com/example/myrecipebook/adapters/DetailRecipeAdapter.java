package com.example.myrecipebook.adapters;


//ADAPTER + VIEWHOLDER

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;

import com.example.myrecipebook.R;
import com.example.myrecipebook.models.CategoryModel;
import com.example.myrecipebook.models.DetailRecipeModel;
import com.squareup.picasso.Picasso;

import java.util.List;

//5.ADAPTER class (manage all the viewHolders)
public class DetailRecipeAdapter extends RecyclerView.Adapter<DetailRecipeAdapter.ViewHolder> {

    Context context;
    List<DetailRecipeModel> list;
    int position;

    //CTOR
    public DetailRecipeAdapter(Context context, List<DetailRecipeModel> list, int position) {
        this.context = context;
        this.list = list;
        this.position = position;
    }

    @NonNull
    @Override
    //return viewHolder that contain view *object*, that create from match xml file, using inflater
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detailrecipe_item, parent, false ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DetailRecipeModel detailRecipeModel = list.get(this.position);
        Picasso.get().load(detailRecipeModel.getImageUrl()).into(holder.imageView);
        holder.name.setText(detailRecipeModel.getName());
        holder.detail.setText(detailRecipeModel.getTotalTime());
        holder.ingredients.setText(detailRecipeModel.getIngredients());
        holder.instruction.setText(detailRecipeModel.getInstruction());
        List<String> health = detailRecipeModel.getHealthLabels();
        if (health.contains("Vegetarian")) holder.labelVegetarian.setBackgroundColor(Color.GREEN);
        if (health.contains("Vegan")) holder.labelVegan.setBackgroundColor(Color.GREEN);
        if (health.contains("Kosher")) holder.labelKosher.setBackgroundColor(Color.GREEN);
        if (health.contains("Gluten-Free")) holder.labelGlutenFree.setBackgroundColor(Color.GREEN);
        if (health.contains("Dairy-Free")) holder.labelDairyFree.setBackgroundColor(Color.GREEN);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //ViewHolder inner class
//hold object of view of one line and save references to his elements
    public class ViewHolder extends RecyclerView.ViewHolder {

        //references to the views for each data item (from xml file)
        // ImageView imageView;
        TextView name, detail, ingredients, instruction;
        ImageView imageView;
        TextView labelVegetarian, labelVegan, labelKosher, labelGlutenFree, labelDairyFree;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.recipe_img);
            name = itemView.findViewById(R.id.recipe_name);
            detail =  itemView.findViewById(R.id.recipe_detail);
            ingredients =  itemView.findViewById(R.id.recipe_ingredients);
            instruction = itemView.findViewById(R.id.recipe_instruction);
            labelVegetarian = itemView.findViewById(R.id.label_vegetarian);
            labelVegan = itemView.findViewById(R.id.label_vegan);
            labelKosher = itemView.findViewById(R.id.label_kosher);
            labelGlutenFree = itemView.findViewById(R.id.label_gluten_free);
            labelDairyFree = itemView.findViewById(R.id.label_dairy_free);
        }
    }
}
