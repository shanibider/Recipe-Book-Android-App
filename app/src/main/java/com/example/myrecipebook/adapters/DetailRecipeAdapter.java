package com.example.myrecipebook.adapters;


//ADAPTER + VIEWHOLDER

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipebook.R;
import com.example.myrecipebook.models.CategoryModel;
import com.example.myrecipebook.models.DetailRecipeModel;

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
        System.out.println(this.position);

        holder.imageView.setImageResource(list.get(this.position).getImage());
        holder.name.setText(list.get(this.position).getName());
        holder.detail.setText(list.get(this.position).getTotalTime());
        holder.ingredients.setText(list.get(this.position).getIngredients());
        holder.instruction.setText(list.get(this.position).getInstruction());

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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.recipe_img);
            name = itemView.findViewById(R.id.recipe_name);
            detail =  itemView.findViewById(R.id.recipe_detail);
            ingredients =  itemView.findViewById(R.id.recipe_ingredients);
            instruction = itemView.findViewById(R.id.recipe_instruction);
        }
    }
}
