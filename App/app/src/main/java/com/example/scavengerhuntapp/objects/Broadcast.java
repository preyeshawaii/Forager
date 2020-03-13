package com.example.scavengerhuntapp.objects;


public class Broadcast {
    public static final String KEY_BROADCASTS = "broadcasts";
    public static final String KEY_BROADCAST_ID = "broadcastID";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_MESSAGE_NUM = "messageNum";


    private String broadcastID;
    private String message;
    private int messageNum;

    public Broadcast(){ }

    public Broadcast(String broadcastID, String message, int messageNum) {
        this.broadcastID = broadcastID;
        this.message = message;
        this.messageNum = messageNum;
    }

    public String getBroadcastID() {
        return broadcastID;
    }

    public String getMessage() {
        return message;
    }

    public int getMessageNum() {
        return messageNum;
    }
}
