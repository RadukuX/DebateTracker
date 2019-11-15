package com.example.debatetracker.models;

public class Debate {
    public String name;
    public String location;
    public String description;

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
