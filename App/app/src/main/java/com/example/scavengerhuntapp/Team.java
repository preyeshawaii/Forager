package com.example.scavengerhuntapp;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Team {
    static final String KEY_TEAMS = "teams";
    static final String KEY_TEAM_ID = "teamID";
    static final String KEY_TEAM_NAME = "teamName";
    static final String KEY_POINTS = "points";

    private String teamID;
    private String teamName;
    private int points;
    private Map<String, Map<String, String>> members;
    private Map<String, String> memberNamesAndPhoneNum;

    public Team(){ }

    public Team(String teamID, String teamName) {
        this.teamID = teamID;
        this.teamName = teamName;
        this.points = 0;
        members = new HashMap<>();
    }

    public String getTeamID() {
        return teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getPoints() {
        return points;
    }

    public Map<String, Map<String, String>> getMembers() {
        return members;
    }

    @Exclude
    public Map<String, String> getNamesAndPhoneNums(){
        if (memberNamesAndPhoneNum == null){
            memberNamesAndPhoneNum = new HashMap<>();
            for (Map.Entry<String, Map<String, String>> entry : members.entrySet()){

                for (Map.Entry<String, String> entry2 : entry.getValue().entrySet()){
                    memberNamesAndPhoneNum.put(entry2.getKey(), entry2.getValue());
                }

            }
            return memberNamesAndPhoneNum;
        }

        return memberNamesAndPhoneNum;
    }
}
