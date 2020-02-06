package com.example.scavengerhuntapp;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

public class User {
    static final String KEY_ORGANIZERS = "organizers";
    static final String KEY_PLAYERS = "players";
    static final String KEY_CURRENT_HUNT = "currentHunt";
    static final String KEY_PREVIOUS_HUNT_IDS = "previousHuntIDs";

    private String userID;
    private String fullName;
    private String email;
    private String userType;
    private String currentHunt;
    private List<String> previousHuntIDs;

    public User(){

    }

    public User(String userID, String fullName, String email, String userType){
        this.userID = userID;
        this.fullName = fullName;
        this.email = email;
        this.userType = userType;
        this.currentHunt = "";
        this.previousHuntIDs = new ArrayList<>();
    }

    @Exclude
    public String getUserID() {
        return userID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }

    public String getCurrentHunt() {
        return currentHunt;
    }

    public List<String> getPreviousHuntIDs() {
        return previousHuntIDs;
    }

    public void addHunt(String newHunt){
        this.previousHuntIDs.add(newHunt);
    }
}
