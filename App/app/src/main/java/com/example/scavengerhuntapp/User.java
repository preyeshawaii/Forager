package com.example.scavengerhuntapp;


import java.util.HashMap;
import java.util.Map;

public class User {
    static final String KEY_ORGANIZERS = "organizers";
    static final String KEY_PLAYERS = "players";
    static final String KEY_HUNTS = "hunts";

    private String userID;
    private String fullName;
    private String email;
    private String userType;
    private String currentHunt;
    private Map<String, String> hunts;

    public User(){
    }

    public User(String userID, String fullName, String email, String userType){
        this.userID = userID;
        this.fullName = fullName;
        this.email = email;
        this.userType = userType;
        this.currentHunt = "";
        this.hunts = new HashMap<>();
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

    public Map<String, String> getHunts() {
        return hunts;
    }

    public void addHunt(String huntID, String huntName){
        this.hunts.put(huntID, huntName);
    }
}
