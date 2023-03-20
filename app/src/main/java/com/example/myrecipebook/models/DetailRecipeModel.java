package com.example.myrecipebook.models;

import java.io.Serializable;
import java.util.List;

public class DetailRecipeModel implements Serializable {
    public String name;
    public int image;
    public List<String> category;
    public List<String> healthLabels;
    public String ingredients;
    public String instruction;
    public String totalTime;




    public DetailRecipeModel(int image, String name, String totalTime, String ingredients, String instruction, List<String> category, List<String> healthLabels) {
        this.image = image;
        this.name = name;
        this.totalTime = totalTime;
        this.ingredients = ingredients;
        this.instruction = instruction;
        this.category=category;
        this.healthLabels=healthLabels;
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

    public DetailRecipeModel(){}

}
