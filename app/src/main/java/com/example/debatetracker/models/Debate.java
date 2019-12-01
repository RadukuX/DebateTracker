package com.example.debatetracker.models;

import java.util.ArrayList;

public class Debate {
    private String name;
    private String location;
    private String description;
    private ArrayList<Location> markerLocations;

    public ArrayList<Location> getMarkerLocations() {
        return markerLocations;
    }

    public void setMarkerLocations(ArrayList<Location> markerLocations) {
        this.markerLocations = markerLocations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Default constructor required for getting data from data snapshots
    public Debate() {

    }

    public Debate(String name, String location, String description) {
        this.name = name;
        this.location = location;
        this.description = description;
    }

    @Override
    public String toString() {
        String s = "\n";
        s += "Name: " + name + "\n\n";
        s += "Location: " + location + "\n\n";
        s += "Description: " + description + "\n\n";
        return s;
    }
}
