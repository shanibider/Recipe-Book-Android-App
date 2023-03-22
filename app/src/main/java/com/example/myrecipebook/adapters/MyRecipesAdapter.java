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

import com.bumptech.glide.Glide;
import com.example.myrecipebook.DataClass;
import com.example.myrecipebook.R;
import com.example.myrecipebook.activities.MyDetailRecipe;
import com.example.myrecipebook.models.DetailRecipeModel;

import java.util.ArrayList;
import java.util.List;


public class MyRecipesAdapter extends RecyclerView.Adapter<MyRecipesAdapter.ViewHolder> {

    Context context;
    List<DataClass> dataList;
    //List<DetailRecipeModel> list;


    public MyRecipesAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_recipes_item, parent, false);
        return new ViewHolder(view);    }



    @Override
    public void onBindViewHolder(@NonNull MyRecipesAdapter.ViewHolder holder, int position) {
        //img
        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);
        holder.recTitle.setText(dataList.get(position).getDataTitle());
        //holder.recTime.setText(dataList.get(position).getTotalTime());
       // holder.recIngr.setText(dataList.get(position).getIngredients());
        //holder.recInst.setText(dataList.get(position).getInstruction());

        holder.recIngr.setText(dataList.get(position).getDataDesc());
        holder.recInst.setText(dataList.get(position).getDataLang());


        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyDetailRecipe.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getDataTitle());
                intent.putExtra("Key",dataList.get(holder.getAdapterPosition()).getKey());
                //intent.putExtra("Ingredients", dataList.get(holder.getAdapterPosition()).getIngredients());
                //intent.putExtra("Instruction", dataList.get(holder.getAdapterPosition()).getInstruction());
                //intent.putExtra("Time", dataList.get(holder.getAdapterPosition()).getTotalTime());


                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public void searchDataList(ArrayList<DataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }




class ViewHolder extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recTitle, recTime, recIngr, recInst;
    CardView recCard;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recTime= itemView.findViewById(R.id.recTime);
        recIngr = itemView.findViewById(R.id.recIngr);
        recInst = itemView.findViewById(R.id.recInst);
        recTitle = itemView.findViewById(R.id.recTitle);

    }
}

}
