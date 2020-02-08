package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class User {
    static final String KEY_ORGANIZERS = "organizers";
    static final String KEY_PLAYERS = "players";
    static final String KEY_CURRENT_HUNT = "currentHunt";
    static final String KEY_PREVIOUS_HUNTS = "previousHunts";

    private String userID;
    private String fullName;
    private String email;
    private String userType;
    private String currentHunt;
    private Map<String, String> previousHunts;

    public User(){
    }

    public User(String userID, String fullName, String email, String userType){
        this.userID = userID;
        this.fullName = fullName;
        this.email = email;
        this.userType = userType;
        this.currentHunt = "";
        this.previousHunts = new HashMap<>();
    }

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

    public Map<String, String> getPreviousHunts() {
        return previousHunts;
    }

    public void addHunt(String huntID, String huntName){
        this.previousHunts.put(huntID, huntName);
    }
}
