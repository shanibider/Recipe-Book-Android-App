package com.example.myrecipebook.models;

import java.util.List;

public class DetailRecipeModel {
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return totalTime;
    }

    public void setDetail(String detail) {
        this.totalTime = detail;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
