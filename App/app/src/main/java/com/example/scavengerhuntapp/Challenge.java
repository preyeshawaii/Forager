package com.example.scavengerhuntapp;

import java.util.ArrayList;

public class Challenge {
    static final String KEY_CHALLENGES = "challenges";

    private String challengeID;
    private String description;
    private int points;

    public Challenge(){
    }

    public Challenge(String challengeID, String description, int points){
        this.challengeID = challengeID;
        this.description = description;
        this.points = points;
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
}
