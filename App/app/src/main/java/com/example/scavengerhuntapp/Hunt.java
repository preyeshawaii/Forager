package com.example.scavengerhuntapp;


import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

public class Hunt {
    static final String KEY_HUNTS = "hunts";
    static final String KEY_HUNT_NAME = "huntName";

    private String huntID;
    private String huntName;
    private String dateCreated;
    //private Timestamp dateCreated;

    public Hunt(){ }

    public Hunt(String huntName){
        this.huntName = huntName;
        this.dateCreated = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
    }

    @Exclude
    public String getHuntID() {
        return huntID;
    }

    public String getHuntName() {
        return huntName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setHuntID(String huntID) {
        this.huntID = huntID;
    }
}
