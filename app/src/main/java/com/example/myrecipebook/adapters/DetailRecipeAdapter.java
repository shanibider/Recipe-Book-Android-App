package com.example.myrecipebook.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;

import com.example.myrecipebook.R;
import com.example.myrecipebook.activities.UpdateActivity;
import com.example.myrecipebook.models.CategoryModel;
import com.example.myrecipebook.models.DetailRecipeModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

//ADAPTER + VIEWHOLDER
//ADAPTER class (manage all the viewHolders)
public class DetailRecipeAdapter extends RecyclerView.Adapter<DetailRecipeAdapter.ViewHolder> {

    Context context;
    DetailRecipeModel detailRecipeModel;
    int position;

    public DetailRecipeAdapter(Context context, DetailRecipeModel detailRecipeModel, int position) {
        this.context = context;
        this.detailRecipeModel = detailRecipeModel;
        this.position = position;
    }

    @NonNull
    @Override
    //return viewHolder that contain view *object*, that create from match xml file, using inflater
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detailrecipe_item, parent, false ));
    }

    //called for each item in the list to bind data to its view.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //loads the image using Picasso
        Picasso.get().load(this.detailRecipeModel.getImageUrl()).into(holder.imageView);
        holder.name.setText(this.detailRecipeModel.getName());
        holder.detail.setText(this.detailRecipeModel.getTotalTime());
        holder.ingredients.setText(this.detailRecipeModel.getIngredients());

        //list for health labels
        List<String> health = this.detailRecipeModel.getHealthLabels();
        //if health labels of recipe contain certain strings we set background color
        if (health.contains("Vegetarian")) holder.labelVegetarian.setBackgroundColor(Color.LTGRAY);
        if (health.contains("Vegan")) holder.labelVegan.setBackgroundColor(Color.LTGRAY);
        if (health.contains("Kosher")) holder.labelKosher.setBackgroundColor(Color.LTGRAY);
        if (health.contains("Gluten-Free")) holder.labelGlutenFree.setBackgroundColor(Color.LTGRAY);
        if (health.contains("Dairy-Free")) holder.labelDairyFree.setBackgroundColor(Color.LTGRAY);


        //checks if the currently logged in user is the same as the user who created the recipe. If so, it makes the delete and edit buttons visible and clickable
        if (FirebaseAuth.getInstance().getUid().equals(detailRecipeModel.getUser())){

            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setClickable(true);


            //delete button listener
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to delete this recipe?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                //deletes the recipe from the Firebase Realtime Database
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference recipesRef = databaseRef.child("Recipes");
                                    recipesRef.child(detailRecipeModel.getName()).removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    System.out.println("Successfully Deleted Recipe");
                                                    //finish the current activity
                                                    ((Activity) context).finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Handle failed removal
                                                    Log.e("TAG", "Error removing child: " + e.getMessage());
                                                }
                                            });

                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            });

            //edit button listener
            //if the edit button is clicked, it creates an intent to start the UpdateActivity
            holder.editButton.setVisibility(View.VISIBLE);
            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UpdateActivity.class);
                    //passes the DetailRecipeModel object as an extra
                    intent.putExtra("detailRecipeModel",(Serializable) detailRecipeModel);
                    context.startActivity(intent);
                }
            });
        } else {
            holder.deleteButton.setVisibility(View.GONE);
            holder.deleteButton.setClickable(false);
            holder.editButton.setVisibility(View.GONE);
        }
    }

    //show one item from 'detailrecipe_item' in 'fragment_detailrecipe'
    @Override
    public int getItemCount() {
        return 1;
    }





    //ViewHolder inner class
    //holds references to views in the inflated layout resource
    public class ViewHolder extends RecyclerView.ViewHolder {

        // ImageView imageView;
        TextView name, detail, ingredients;
        ImageView imageView;
        TextView labelVegetarian, labelVegan, labelKosher, labelGlutenFree, labelDairyFree;
        FloatingActionButton editButton, deleteButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.recipe_img);
            name = itemView.findViewById(R.id.recipe_name);
            detail =  itemView.findViewById(R.id.recipe_detail);
            ingredients =  itemView.findViewById(R.id.recipe_ingredients);
            labelVegetarian = itemView.findViewById(R.id.label_vegetarian);
            labelVegan = itemView.findViewById(R.id.label_vegan);
            labelKosher = itemView.findViewById(R.id.label_kosher);
            labelGlutenFree = itemView.findViewById(R.id.label_gluten_free);
            labelDairyFree = itemView.findViewById(R.id.label_dairy_free);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
