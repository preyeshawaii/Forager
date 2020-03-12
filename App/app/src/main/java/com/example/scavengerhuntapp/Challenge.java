package com.example.scavengerhuntapp;

import com.google.firebase.firestore.Exclude;

public class Challenge {
    static final String KEY_CHALLENGES = "challenges";
    static final String KEY_CHALLENGE_ID = "challengeID";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_LOCATION = "location";
    static final String KEY_POINTS = "points";
    static final String KEY_ICON = "icon";

    static final String KEY_UNATTEMPTED = "unAttempted";
    static final String KEY_IN_REVIEW = "inReview";
    static final String KEY_ACCEPTED = "accepted";
    static final String KEY_REJECTED = "rejected";

    private String challengeID;
    private String description;
    private int points;
    private String location;
    private int icon;
    private String state;

    public Challenge(){
    }

    public Challenge(String challengeID, String description, String location, int points, int icon){
        this.challengeID = challengeID;
        this.description = description;
        this.location = location;
        this.points = points;
        this.icon = icon;
        this.state = KEY_UNATTEMPTED;
    }

    public String getChallengeID() {
        return this.challengeID;
    }

    public String getDescription() {
        return this.description;
    }

    public int getPoints() {
        return points;
    }

    public String getLocation() {
        return this.location;
    }

    public int getIcon() {
        return this.icon;
    }

    public String getState(){
        return this.state;
    }

    public void setState(String state){
        this.state = state;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}

