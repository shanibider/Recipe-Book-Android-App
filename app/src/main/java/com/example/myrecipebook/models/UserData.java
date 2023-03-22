package com.example.myrecipebook.models;


public class UserData {

    String name, email, username, profileImage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public UserData(String name, String email, String username, String profileImage) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.profileImage = profileImage;
    }

    public UserData() {
    }
}