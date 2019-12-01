package com.example.debatetracker.models;

import java.io.Serializable;

public class Location implements Serializable {
    private float latitude;
    private float longitude;

    public Location(float lat, float longi) {
        latitude = lat;
        longitude = longi;
    }

    public Location() {

    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
