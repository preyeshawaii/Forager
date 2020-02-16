package com.example.scavengerhuntapp;

import com.google.firebase.firestore.Exclude;

public class Challenge {
    static final String KEY_CHALLENGES = "challenges";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_LOCATION = "location";
    static final String KEY_POINTS = "points";
    static final String KEY_ICON = "icon";

    private String challengeID;
    private String description;
    private int points;
    private String location;
    private int icon;

    public Challenge(){
    }

    public Challenge(String challengeID, String description, String location, int points, int icon){
        this.challengeID = challengeID;
        this.description = description;
        this.location = location;
        this.points = points;
        this.icon = icon;
    }

    public Challenge(String description, String location, int points, int icon){
        this("", description, location, points, icon);
    }

    public String getChallengeID() {
        return challengeID;
    }

    public String getDescription() {
        return description;
    }

    public int getPoints() {
        return points;
    }

    public String getLocation() {
        return location;
    }

    public int getIcon() {
        return icon;
    }
}

