package com.example.myrecipebook.models;

import java.io.Serializable;
import java.util.List;

public class DetailRecipeModel implements Serializable {
    public int image;
    public String name;
    public List<String> category;
    public List<String> healthLabels;
    public String ingredients;
    public String instruction;
    public String totalTime;
    public String imageUrl;

    public DetailRecipeModel(int image, String name, List<String> category, List<String> healthLabels, String ingredients, String instruction, String totalTime, String imageUrl) {
        this.image = image;
        this.name = name;
        this.category = category;
        this.healthLabels = healthLabels;
        this.ingredients = ingredients;
        this.instruction = instruction;
        this.totalTime = totalTime;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public List<String> getCategory() {
        return category;
    }

    public List<String> getHealthLabels() {
        return healthLabels;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
