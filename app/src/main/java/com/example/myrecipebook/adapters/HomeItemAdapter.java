package com.example.myrecipebook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecipebook.R;
import com.example.myrecipebook.models.HomeItemModel;

import java.util.List;


//ADAPTER + VIEWHOLDER

//5.ADAPTER (manage all the viewholders)
public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemAdapter.ViewHolder> {

   Context contxt;
   List<HomeItemModel> list;

    public HomeItemAdapter(Context contxt, List<HomeItemModel> list) {
        this.contxt = contxt;
        this.list = list;
    }

    @NonNull
    @Override
    //create a new list row object= new view holder object (one line of view)
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflater create view object from xml file (home_item.xml)
        //then wrap it in view older object
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false));
    }

    //set the view holder properties according to the object is displayed (Bind data to line)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(list.get(position).getImage());
        holder.name.setText(list.get(position).getName());
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView= itemView.findViewById(R.id.breakfast_img);
            name= itemView.findViewById(R.id.breakfast_title);

        }
    }
}
