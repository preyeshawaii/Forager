package com.example.scavengerhuntapp.objects;


public class Broadcast {
    public static final String KEY_BROADCASTS = "broadcasts";
    public static final String KEY_BROADCAST_ID = "broadcastID";
    public static final String KEY_MESSAGE = "message";


    private String broadcastID;
    private String message;

    public Broadcast(){ }

    public Broadcast(String broadcastID, String message) {
        this.broadcastID = broadcastID;
        this.message = message;
    }

    public String getBroadcastID() {
        return broadcastID;
    }

    public String getMessage() {
        return message;
    }
}
