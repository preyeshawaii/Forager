package com.example.scavengerhuntapp.objects;


import com.google.firebase.firestore.Exclude;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Hunt {
    public static final int LENGTH_OF_ID = 12;
    public static final String KEY_HUNTS = "hunts";
    public static final String KEY_HUNT_ID = "huntID";
    public static final String KEY_HUNT_NAME = "huntName";

    private String huntID;
    private String huntName;
    private String dateCreated;
    private Map<String, String> players;
    private Boolean viewPoints;

    public Hunt(){ }

    public Hunt(String huntID, String huntName){
        this.huntID = huntID;
        this.huntName = huntName;
        this.dateCreated = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        this.players = new HashMap<>();
        this.viewPoints = false;
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

    public Map<String, String> getPlayers(){
        return players;
    }

    public Boolean getViewPoints() {
        return viewPoints;
    }

    public void setViewPoints(Boolean viewPoints) {
        this.viewPoints = viewPoints;
    }
}
