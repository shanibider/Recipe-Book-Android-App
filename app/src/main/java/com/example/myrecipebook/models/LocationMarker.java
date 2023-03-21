package com.example.myrecipebook.models;


import com.google.android.gms.maps.model.LatLng;

public class LocationMarker {

    String title, address, hours;
    double lat, lng;

    public String getTitle() {
        return title;
    }
    public String getHours() {
        return hours;
    }
    public String getAddress() {
        return address;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setHours(String hours) {
        this.hours = hours;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() { return lat;}
    public double getLng() { return lng;}

    public void setLat(double lat) {this.lat = lat;}
    public void setLng(double lng) {this.lng = lng;}

    public LocationMarker(String title, double lat, double lng, String address, String hours) {
        this.title = title;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.hours = hours;
    }

    public LocationMarker() {
    }
}