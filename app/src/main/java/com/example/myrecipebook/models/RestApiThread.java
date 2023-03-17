package com.example.myrecipebook.models;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myrecipebook.R;
import com.example.myrecipebook.databinding.DetailrecipeItemBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class RestApiThread extends Thread {

    public String[] foods;
    public String API_ID = "2fbb9e5c";
    public String API_KEY = "e1fb0aa48ef28311b465ca5ff535a63d";
    public int[] images;
    public List<String> mealCategories;


    public RestApiThread() {
        foods = new String[]{"pancakes","toast","granola","omelette","waffles","chicken", "hummus","curry","quinoa","hamburger","steak","salmon","bolognese","kebab","cake","pie","ice-cream"};
        images = new int[] {R.drawable.pancakes,R.drawable.toast,R.drawable.granola,R.drawable.omlette,R.drawable.waffles,R.drawable.chicken,R.drawable.hummus,R.drawable.curry,R.drawable.quinoa,R.drawable.hamburger,R.drawable.steak,R.drawable.salmon,R.drawable.bolognese,R.drawable.kebab,R.drawable.cake,R.drawable.pie,R.drawable.icecream};
        mealCategories = new ArrayList<>();
        mealCategories.add("Vegetarian");
        mealCategories.add("Vegan");
        mealCategories.add("Kosher");
        mealCategories.add("Gluten-Free");
        mealCategories.add("Dairy-Free");
    }

    @Override
    public void run() {
        System.out.println("RestApiThread run test");
        final AtomicBoolean flag = new AtomicBoolean(false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference recipeRef = database.getReference("Recipes");

        recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChildren()) {
                    System.out.println("Don't have children");
                    flag.set(true);
                } else {
                    System.out.println("Have children");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });

        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (flag.get()) {
            for(int i=0;i<foods.length;i++) {
                DetailRecipeModel detailRecipeModel = generateRecipeModel(i);
                DatabaseReference recipeChildRef = recipeRef.child(detailRecipeModel.name);
                recipeChildRef.setValue(detailRecipeModel);
                try {
                    sleep(10000); // Sleep for 10 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("finished uploading to firebase db");
        }
    }

    public DetailRecipeModel generateRecipeModel (int i){
        try {
            String food = foods[i];
            String url = "https://api.edamam.com/search?q=" + food +"&app_id=" + API_ID + "&app_key=" + API_KEY;

            URL requestUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) requestUrl.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                String my_q = jsonObject.getString("q");

                System.out.println(my_q);
                JSONArray hits = jsonObject.getJSONArray("hits");
                JSONObject hit = hits.getJSONObject(0);

                JSONObject recipe = hit.getJSONObject("recipe");
                int my_image = images[i];
                System.out.println(my_image);

                List<String> my_healthLabels = new ArrayList<>();
                JSONArray healthLabels = recipe.getJSONArray("healthLabels");
                for(int j=0;j<healthLabels.length();j++) {
                    if (mealCategories.contains(healthLabels.getString(j))){
                        my_healthLabels.add(healthLabels.getString(j));
                    }
                }
                System.out.println(my_healthLabels);

                JSONArray ingredientLines = recipe.getJSONArray("ingredientLines");
                String my_ingredientLines = "";
                for(int j=0;j<ingredientLines.length();j++){
                    my_ingredientLines +=ingredientLines.getString(j);
                }
                System.out.println(my_ingredientLines);

                List<String> my_mealType = new ArrayList<>();
                JSONArray mealTypeArray = recipe.getJSONArray("mealType");
                for(int j=0;j<mealTypeArray.length();j++){
                    if(mealTypeArray.getString(0).contains("breakfast"))
                        my_mealType.add("breakfast");
                    if(mealTypeArray.getString(0).contains("lunch"))
                        my_mealType.add("lunch");
                    if (mealTypeArray.getString(0).contains("dinner"))
                        my_mealType.add("dinner");
                    if (mealTypeArray.getString(0).contains("dessert"))
                        my_mealType.add("dessert");
                    if (mealTypeArray.getString(0).contains("salad"))
                        my_mealType.add("salad");
                    if (mealTypeArray.getString(0).contains("soup"))
                        my_mealType.add("soup");
                }
                System.out.println(my_mealType);

                int int_totalTime = recipe.optInt("totalTime",-1);
                if (int_totalTime > 0) {
                    System.out.println("Total time: " + int_totalTime + " minutes");
                } else {
                    int_totalTime = -1;
                    System.out.println("Total time not available");
                }
                String my_totalTime = Integer.toString(int_totalTime) + " min";

                String my_recipeUrl = recipe.optString("url", "");
                if (!my_recipeUrl.isEmpty()) {
                    System.out.println("Recipe URL: " + my_recipeUrl);
                } else {
                    System.out.println("Recipe URL not available");
                }
                DetailRecipeModel drm = new DetailRecipeModel(my_image,my_q,my_totalTime,my_ingredientLines,my_recipeUrl,my_mealType,my_healthLabels);
                return drm;
            } else {
                System.out.println("Error: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
