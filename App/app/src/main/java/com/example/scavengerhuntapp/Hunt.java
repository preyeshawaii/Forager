package com.example.scavengerhuntapp;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

public class Hunt {
    static final String KEY_HUNTS = "hunts";

    private String huntName;
    private String huntID;

    public Hunt(){

    }

    public Hunt(String huntName, String huntID){
        this.huntName = huntName;
        this.huntID = huntID;
    }

    @Exclude
    public String getHuntName() {
        return huntName;
    }

    public String getHuntID() {
        return huntID;
    }
}
