package com.example.myrecipebook.models;

import java.util.List;

public class RecipeModel {
    public String name;
    public String image;
    public List<String> category;
    public List<String> healthLabels;
    public List<String> Ingredients;
    public int totalTime;
    public String recipeUrl;

    public RecipeModel(String name, String image, List<String> category, List<String> healthLabels, List<String> ingredients, int totalTime, String recipeUrl) {
        this.name = name;
        this.image = image;
        this.category = category;
        this.healthLabels = healthLabels;
        Ingredients = ingredients;
        this.totalTime = totalTime;
        this.recipeUrl = recipeUrl;
    }
}
