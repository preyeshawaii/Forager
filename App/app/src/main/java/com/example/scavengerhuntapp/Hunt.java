package com.example.scavengerhuntapp;


import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

public class Hunt {
    static final String KEY_HUNTS = "hunts";
    static final String KEY_HUNT_ID = "huntID";
    static final String KEY_HUNT_NAME = "huntName";

    private String huntID;
    private String huntName;
    private String dateCreated;

    public Hunt(){ }

    public Hunt(String huntID, String huntName){
        this.huntID = huntID;
        this.huntName = huntName;
        this.dateCreated = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
    }

    public String getHuntID() {
        return huntID;
    }

    public String getHuntName() {
        return huntName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

}
