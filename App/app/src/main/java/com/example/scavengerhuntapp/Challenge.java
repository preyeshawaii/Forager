package com.example.scavengerhuntapp;

import java.util.ArrayList;

public class Challenge {
    static final String KEY_CHALLENGES = "challenges";

    private String challengeID;
    private String description;
    private int points;
    private String location;
    private int icon;

    public Challenge(){
    }

    public Challenge(String challengeID, String description, int points, String location, int icon){
        this.challengeID = challengeID;
        this.description = description;
        this.points = points;
        this.location = location;
        this.icon = icon;
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
