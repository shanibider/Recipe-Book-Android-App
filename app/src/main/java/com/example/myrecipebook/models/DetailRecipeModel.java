package com.example.myrecipebook.models;

public class DetailRecipeModel {
    int image;
    String name;
    String detail;
    String ingredients;
    String instruction;

    public DetailRecipeModel(int image, String name, String detail, String ingredients, String instruction) {
        this.image = image;
        this.name = name;
        this.detail = detail;
        this.ingredients = ingredients;
        this.instruction = instruction;
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
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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
