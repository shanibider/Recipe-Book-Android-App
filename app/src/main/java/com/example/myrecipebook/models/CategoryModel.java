package com.example.myrecipebook.models;

//Data (in Category Screen)
public class CategoryModel {
    int image;
    String name;
    String detail;

    public CategoryModel(int image, String name, String detail) {
        this.image = image;
        this.name = name;
        this.detail= detail;

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






}
