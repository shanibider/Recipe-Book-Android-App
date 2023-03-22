package com.example.myrecipebook.models;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.myrecipebook.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RestApiThread extends Thread {

    public String[] foods;
    public String API_ID = "2fbb9e5c";
    public String API_KEY = "e1fb0aa48ef28311b465ca5ff535a63d";
    public int[] images;
    public List<String> mealCategories;
    String my_imageUrl;


    public RestApiThread() {
        foods = new String[]{"pancakes","toast","granola","omelette","waffles","chicken", "hummus","curry","quinoa","hamburger","steak","salmon","bolognese","kebab","cake","pie","muffin"};
        images = new int[] {R.drawable.pancakes,R.drawable.toast,R.drawable.granola,R.drawable.omelette,R.drawable.waffles,R.drawable.chicken,R.drawable.hummus,R.drawable.curry,R.drawable.quinoa,R.drawable.hamburger,R.drawable.steak,R.drawable.salmon,R.drawable.bolognese,R.drawable.kebab,R.drawable.cake,R.drawable.pie,R.drawable.icecream};
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

        //uses Firebase Realtime Database to check if there are any child nodes under the "Recipes" node
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
        //iterates over foods array and generates a DetailRecipeModel object for each food item. Then adds this object as a child node to the Firebase Realtime Db under "Recipes"
        if (flag.get()) {
            for(int i=0;i<foods.length;i++) {
                DetailRecipeModel detailRecipeModel = generateRecipeModel(i);
                DatabaseReference recipeChildRef = recipeRef.child(detailRecipeModel.name);
                recipeChildRef.setValue(detailRecipeModel);
            }
            System.out.println("finished uploading to firebase db");
        }
    }

    //generates DetailRecipeModel object for a given index in the foods array
    public DetailRecipeModel generateRecipeModel (int i){
        try {
            String food = foods[i];
            //constructs the URL (request URL with endpoint, necessary parameters, and my API key) for the API request (contains page with all foods[i] recipes)
            String url = "https://api.edamam.com/search?q=" + food +"&app_id=" + API_ID + "&app_key=" + API_KEY;

            //creates a URL object
            //Send HTTP GET request to the constructed URL. The response will be in JSON format, contain a list of recipe objects
            URL requestUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) requestUrl.openConnection();
            con.setRequestMethod("GET");

            //checks the HTTP response code
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //reads the API response and converts it to a JSON object
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                //extracts the required information from the JSON object (recipe name, image, labels, ingredient, meal type, total time, recipe URL, image URL)

                //Creates a new JSONObject using the response from the API
                JSONObject jsonObject = new JSONObject(response.toString());
                //Extracts the value of q key (name of the recipe) from jsonObject
                String my_q = jsonObject.getString("q");

                System.out.println(my_q);
                //Extracts the first item from hits array (contain the recipe information) in jsonObject
                JSONArray hits = jsonObject.getJSONArray("hits");
                //first object contains the most relevant information about the recipe
                JSONObject hit = hits.getJSONObject(0);

                //Extracts the recipe object from the hit object
                //'recipe' contains all the information about the recipe
                JSONObject recipe = hit.getJSONObject("recipe");
                int my_image = images[i];
                System.out.println(my_image);

                //use Firebase Storage to get download URL for the recipe image and stores it in "my_imageUrl"
                FirebaseStorage storage = FirebaseStorage.getInstance();
                //retrieves a StorageReference object for the recipe image in Firebase Storage
                StorageReference storageRef = storage.getReference().child("recipe_images/" + my_q +".jpg");
                //retrieves the download URL for the recipe image from Firebase Storage
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        my_imageUrl = uri.toString();
                        System.out.println("my_imageUrl : " + my_imageUrl);
                        // Use the imageUrl to display the image in your ImageView
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors that occur while getting the download URL
                        System.out.println("Problem with "+ my_q);
                        System.out.println(exception);
                    }
                });

                sleep(10000);


                //My HealthLabels
                List<String> my_healthLabels = new ArrayList<>();
                JSONArray healthLabels = recipe.getJSONArray("healthLabels");
                //iterates through the "healthLabels" array (from FireBase) and adds the labels to the my_healthLabels list if they match the mealCategories list (our categories)
                for(int j=0;j<healthLabels.length();j++) {
                    if (mealCategories.contains(healthLabels.getString(j))){
                        my_healthLabels.add(healthLabels.getString(j));
                    }
                }
                //my_healthLabels should contain: Vegetarian, vegan, kosher, Gluten-Free, Dairy-Free
                System.out.println(my_healthLabels);

                //My IngredientLines
                JSONArray ingredientLines = recipe.getJSONArray("ingredientLines");
                String my_ingredientLines = "";
                for(int j=0;j<ingredientLines.length();j++){
                    my_ingredientLines +=ingredientLines.getString(j);
                }
                System.out.println(my_ingredientLines);

                //My MealType
                List<String> my_mealType = new ArrayList<>();
                //extracts 'mealTypeArray' array from 'recipe' JSON object
                JSONArray mealTypeArray = recipe.getJSONArray("mealType");

                //checks if the string contains any of the following keywords. If it does, it adds the corresponding meal type to the my_mealType list
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

                //TotalTime
                //extracts the int value for "totalTime" from the recipe JSON object
                int int_totalTime = recipe.optInt("totalTime",-1);
                if (int_totalTime > 0) {
                    System.out.println("Total time: " + int_totalTime + " minutes");
                } else {
                    int_totalTime = -1;
                    System.out.println("Total time not available");
                }
                //integer value is converted to a string
                String my_totalTime = Integer.toString(int_totalTime) + " min";

                //My Recipe
                //extracts the recipe URL from the recipe JSON object using the optString
                String my_recipeUrl = recipe.optString("url", "");
                if (!my_recipeUrl.isEmpty()) {
                    System.out.println("Recipe URL: " + my_recipeUrl);
                } else {
                    System.out.println("Recipe URL not available");
                }
                //creates a new DetailedRecipeModel object after extracting all the necessary information and returns it
                DetailRecipeModel drm = new DetailRecipeModel("GeneratedFromRestApi",my_q, my_mealType, my_healthLabels, my_ingredientLines, my_recipeUrl, my_totalTime, my_imageUrl);
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
