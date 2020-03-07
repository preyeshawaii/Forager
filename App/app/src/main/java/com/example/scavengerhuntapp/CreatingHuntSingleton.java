package com.example.scavengerhuntapp;

import java.util.ArrayList;
import java.util.List;

public class CreatingHuntSingleton {
    private static CreatingHuntSingleton creatingHuntSingleton = null;

    final static int[] ICONS = { R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5};//,R.drawable.a6};//, R.drawable.a7};//, R.drawable.a8, R.drawable.a9, R.drawable.a10, R.drawable.a11, R.drawable.a12, R.drawable.a13, R.drawable.a14, R.drawable.a15, R.drawable.a16, R.drawable.a17, R.drawable.a18, R.drawable.a19, R.drawable.a20, R.drawable.a21, R.drawable.a22, R.drawable.a23, R.drawable.a24, R.drawable.a25, R.drawable.a26, R.drawable.a27, R.drawable.a28,R.drawable.a29,R.drawable.a30,R.drawable.a31,R.drawable.a32,R.drawable.a33,R.drawable.a34,R.drawable.a35,R.drawable.a36, R.drawable.a37,R.drawable.a38,R.drawable.a39,R.drawable.a40};

    final static String[] CHALLENGES = { "Take a picture of your group on a Trolley car", "Come up with a team name and display it creatively", "Ride the lions", "Make a human bridge", "Hold some live seafood"};//, "Give a campaign speech for a California politician"};//, "get dorm markings painted on your body"};//, "Eat the Earthquake", "LOUDLY advertise See's Chocolate", "Do an interpretive dance"};//, "Walk someone's dog", "Find the guy holding the sign that says 'SMILE.'", "Ride the twisty slides", "Take a picture with the hippiest-looking person", "Do the Cha-cha in front of the cha-cha-cha restaurant", "Dance in conga-line fashion", "Imitate a Greek Statue", "Spell out your dorm name using your bodies", "get a group of people to chant along with your dorm chant", "Get a picture of your group", "Shake the hand of a priest at the oldest church", "Create a street show and keep performing until you earn $5", "Do the Macarena with a taxi driver", "Serenade a random couple", "Find a way to get on TV", "Take a picture with the Mayor", "Flop around like a fish on the ground","Give a tip to a street vendor or street performer", "Lead a tour group of at least 10 people", "Go to Sephora and have two people get a makeover", "Give a flower to a stranger", "While riding the bus, break out into a rendition of Yellow Submarine", "Bark on all fours at the bush man", "Take a picture in the driver seat of a hundred thousand dollar car", "Protest the mistreatment of wax people", "Perform Beatles classics", "play 'Heart and Soul' on the piano", "Follow the first people you see and discuss the merits of Adidas with them", "Catch a pigeon", "Propose to someone on your team at a jewelry store with a real diamond ring"};
    final static String[] LOCATIONS = { "San Francisco", "San Francisco", "Chinatown", "Chinatown", "Chinatown"};//, "City Hall"};//, "San Francisco"};//, "Ghirardelli Square", "Ghirardelli Square", " Ghirardelli Factory"};//, "Golden Gate Park", "19th avenue (near Golden Gate Park)", "Golden Gate Park playground", "Haight", "Haight Street", "Lombard street", "Palace of Fine Arts", "SF MOMA", "Golden Gate Park", "Lyle Tuttle's Tattoo Museum", "North Beach", "San Francisco", "San Francisco", "San Francisco", "San Francisco", "San Francisco", "Fisherman’s Wharf", "San Francisco", "San Francisco", "San Francisco", "San Francisco", "San Francisco", "San Francisco", "San Francisco", "wax museum", "Virgin Megastore", "FAO Schwartz", "Niketown", "Union Square", "San Francisco"};
    final static Integer[] POINTS = {5,20,10,10,15};//,25};//, 45};//,40, 25,30};//, 25, 30, 10, 15, 15, 25, 15, 30,50, 30, 20,40, 35, 100, 60, 300, 30, 50,100,30,40,50,25,35,30,15,35,20,25,100};
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

    public void updateList(List<Challenge> challenges){
        this.challenges = new ArrayList<>(challenges);

    }
}
