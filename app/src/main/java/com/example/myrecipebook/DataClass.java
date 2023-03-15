package com.example.myrecipebook;

public class DataClass {

    private String imageURL, caption;

    public DataClass(){

    }

    public DataClass(String imageURL, String caption) {
        this.imageURL = imageURL;
        this.caption = caption;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
