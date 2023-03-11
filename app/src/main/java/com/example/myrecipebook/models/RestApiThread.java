package com.example.myrecipebook.models;

import com.example.myrecipebook.R;

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

public class RestApiThread extends Thread {

    public final String[] foods;
    public final int numRecipes;
    public final String API_ID = "2fbb9e5c";
    public final String API_KEY = "e1fb0aa48ef28311b465ca5ff535a63d";
    public List<String> mealCategories;


    public RestApiThread() {
        numRecipes = 7;
        foods = new String[]{"pancakes","toast","granola","omelette","waffles","chicken", "hummus","curry","quinoa","hamburger","steak","salmon","bolognese","kebab","cake","pie","ice-cream"};
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
        List<RecipeModel> recipeModelsList = new ArrayList<>();
        for(String food:foods) {
            recipeModelsList.add(generateRecipeModel(food));
            try {
                sleep(10000); // Sleep for 10 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(recipeModelsList.size());

        String connectionString = "mongodb+srv://OmerA:Aa123123@CookCluster.mongodb.net/MyCookBook?retryWrites=true&w=majority";

    }

    public RecipeModel generateRecipeModel (String food){
        try {
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
                String my_image = recipe.getString("image");
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
                List<String> my_ingredientLines = new ArrayList<>();
                for(int j=0;j<ingredientLines.length();j++){
                    my_ingredientLines.add(ingredientLines.getString(j));
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

                int my_totalTime = recipe.optInt("totalTime",-1);
                if (my_totalTime > 0) {
                    System.out.println("Total time: " + my_totalTime + " minutes");
                } else {
                    my_totalTime = -1;
                    System.out.println("Total time not available");
                }

                String my_recipeUrl = recipe.optString("url", "");
                if (!my_recipeUrl.isEmpty()) {
                    System.out.println("Recipe URL: " + my_recipeUrl);
                } else {
                    System.out.println("Recipe URL not available");
                }
                System.out.println(my_recipeUrl);
                RecipeModel rm = new RecipeModel(my_q,my_image,my_mealType,my_healthLabels,my_ingredientLines,my_totalTime,my_recipeUrl);
                return rm;
            } else {
                System.out.println("Error: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
