package com.example.scavengerhuntapp;

import java.util.ArrayList;
import java.util.List;

public class CreatingHuntSingleton {
    private static CreatingHuntSingleton creatingHuntSingleton = null;

    final static int[] ICONS = {R.drawable.icecream, R.drawable.group, R.drawable.dog, R.drawable.a1};
    final static String[] CHALLENGES = {"Eat the Earthquake", "Loudly advertise See's Chocolate", "Walk someone's dog"};
    final static String[] LOCATIONS = {"Ghirardelli Square", "Chinatown", "Golden Gate Park"};

    private String huntTitle;
    private List<Challenge> challenges;

    private CreatingHuntSingleton(){
        this.huntTitle = "";
        challenges = new ArrayList<>();
    }

    public static synchronized CreatingHuntSingleton init() {
        if (creatingHuntSingleton == null) {
            creatingHuntSingleton = new CreatingHuntSingleton();
        }
        return creatingHuntSingleton;
    }

    public List<Challenge> getChallenges(){
        return challenges;
    }

    public void addChallenge(Challenge challenge){
        challenges.add(challenge);
    }

    public void setHuntTitle(String title){
        this.huntTitle = title;
    }

    public String getHuntTitle(){
        return this.huntTitle;
    }

    public void clearHunt(){
        this.challenges.clear();
        this.huntTitle = "";
    }
}
