package com.example.scavengerhuntapp.organizer;

import com.example.scavengerhuntapp.R;
import com.example.scavengerhuntapp.objects.Challenge;

import java.util.ArrayList;
import java.util.List;

public class CreatingHuntSingleton {
    private static CreatingHuntSingleton creatingHuntSingleton = null;

    final static int[] ICONS = { R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5};//,R.drawable.a6, R.drawable.a7};//, R.drawable.a8, R.drawable.a9, R.drawable.a10, R.drawable.a11, R.drawable.a12, R.drawable.a13, R.drawable.a14, R.drawable.a15, R.drawable.a16, R.drawable.a17, R.drawable.a18, R.drawable.a19, R.drawable.a20, R.drawable.a21, R.drawable.a22, R.drawable.a23, R.drawable.a24, R.drawable.a25, R.drawable.a26, R.drawable.a27, R.drawable.a28,R.drawable.a29,R.drawable.a30,R.drawable.a31,R.drawable.a32,R.drawable.a33,R.drawable.a34,R.drawable.a35,R.drawable.a36, R.drawable.a37,R.drawable.a38,R.drawable.a39,R.drawable.a40};

    final static String[] CHALLENGES = { "Take a picture of your group on a Trolley car", "Come up with a team name and display it creatively", "Ride the lions", "Make a human bridge", "Hold some live seafood"};//, "Give a campaign speech for a California politician", "get dorm markings painted on your body"};//, "Eat the Earthquake", "LOUDLY advertise See's Chocolate", "Do an interpretive dance"};//, "Walk someone's dog", "Find the guy holding the sign that says 'SMILE.'", "Ride the twisty slides", "Take a picture with the hippiest-looking person", "Do the Cha-cha in front of the cha-cha-cha restaurant", "Dance in conga-line fashion", "Imitate a Greek Statue", "Spell out your dorm name using your bodies", "get a group of people to chant along with your dorm chant", "Get a picture of your group", "Shake the hand of a priest at the oldest church", "Create a street show and keep performing until you earn $5", "Do the Macarena with a taxi driver", "Serenade a random couple", "Find a way to get on TV", "Take a picture with the Mayor", "Flop around like a fish on the ground","Give a tip to a street vendor or street performer", "Lead a tour group of at least 10 people", "Go to Sephora and have two people get a makeover", "Give a flower to a stranger", "While riding the bus, break out into a rendition of Yellow Submarine", "Bark on all fours at the bush man", "Take a picture in the driver seat of a hundred thousand dollar car", "Protest the mistreatment of wax people", "Perform Beatles classics", "play 'Heart and Soul' on the piano", "Follow the first people you see and discuss the merits of Adidas with them", "Catch a pigeon", "Propose to someone on your team at a jewelry store with a real diamond ring"};
    final static String[] LOCATIONS = { "San Francisco", "San Francisco", "Chinatown", "Chinatown", "Chinatown"};//, "City Hall", "San Francisco"};//, "Ghirardelli Square", "Ghirardelli Square", " Ghirardelli Factory"};//, "Golden Gate Park", "19th avenue (near Golden Gate Park)", "Golden Gate Park playground", "Haight", "Haight Street", "Lombard street", "Palace of Fine Arts", "SF MOMA", "Golden Gate Park", "Lyle Tuttle's Tattoo Museum", "North Beach", "San Francisco", "San Francisco", "San Francisco", "San Francisco", "San Francisco", "Fisherman’s Wharf", "San Francisco", "San Francisco", "San Francisco", "San Francisco", "San Francisco", "San Francisco", "San Francisco", "wax museum", "Virgin Megastore", "FAO Schwartz", "Niketown", "Union Square", "San Francisco"};
    final static Integer[] POINTS = {5,20,10,10,15};//,25, 45};//,40, 25,30};//, 25, 30, 10, 15, 15, 25, 15, 30,50, 30, 20,40, 35, 100, 60, 300, 30, 50,100,30,40,50,25,35,30,15,35,20,25,100};
    private String huntTitle;
    private List<Challenge> challenges;
    private List<String> iconNames = null;

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

    public int getSpinnerIcon(int position){
        int icon = R.drawable.icecream;
        switch (position) {
            case 0:
                icon = R.drawable.a1;
                break;
            case 1:
                icon = R.drawable.a2;
                break;
            case 2:
                icon = R.drawable.a3;
                break;
            case 3:
                icon = R.drawable.a4;
                break;
            case 4:
                icon = R.drawable.a5;
                break;
            case 5:
                icon = R.drawable.a6;
                break;
            case 6:
                icon = R.drawable.a7;
                break;
            case 7:
                icon = R.drawable.a8;
                break;
            case 8:
                icon = R.drawable.a9;
                break;
            case 9:
                icon = R.drawable.a10;
                break;
            case 10:
                icon = R.drawable.a11;
                break;
            case 11:
                icon = R.drawable.a12;
                break;
            case 12:
                icon = R.drawable.a13;
                break;
            case 13:
                icon = R.drawable.a14;
                break;
            case 14:
                icon = R.drawable.a15;
                break;
            case 15:
                icon = R.drawable.a16;
                break;
            case 16:
                icon = R.drawable.a17;
                break;
            case 17:
                icon = R.drawable.a18;
                break;
            case 18:
                icon = R.drawable.a19;
                break;
            case 19:
                icon = R.drawable.a20;
                break;
            case 20:
                icon = R.drawable.a21;
                break;
            case 21:
                icon = R.drawable.a2;
                break;
            case 22:
                icon = R.drawable.a23;
                break;
            case 23:
                icon = R.drawable.a24;
                break;
            case 24:
                icon = R.drawable.a25;
                break;
            case 25:
                icon = R.drawable.a26;
                break;
            case 26:
                icon = R.drawable.a27;
                break;
            case 27:
                icon = R.drawable.a28;
                break;
            case 28:
                icon = R.drawable.a29;
                break;
            case 29:
                icon = R.drawable.a30;
                break;
            case 30:
                icon = R.drawable.a31;
                break;
            case 31:
                icon = R.drawable.a32;
                break;
            case 32:
                icon = R.drawable.a33;
                break;
            case 33:
                icon = R.drawable.a34;
                break;
            case 34:
                icon = R.drawable.a35;
                break;
            case 35:
                icon = R.drawable.a36;
                break;
            case 36:
                icon = R.drawable.a37;
                break;
            case 37:
                icon = R.drawable.a38;
                break;
            case 38:
                icon = R.drawable.a39;
                break;
            case 39:
                icon = R.drawable.a40;
                break;

            default:
                //Default image
                icon = R.drawable.icecream;
                break;
        }

        return icon;
    }

    public List<String> getIconNameList(){
        if (iconNames == null){
            createList();
        }

        return iconNames;
    }

    private void createList(){
        iconNames = new ArrayList<>();
        iconNames.add("train");//1
        iconNames.add("abc blocks");//2
        iconNames.add("lion");//3
        iconNames.add("holding hands");//4
        iconNames.add("octopus");//5
        iconNames.add("podium");//6
        iconNames.add("henna");//7
        iconNames.add("ice cream");//8
        iconNames.add("megaphone");//9
        iconNames.add("dancing");//10
        iconNames.add("dog");//11
        iconNames.add("happy");//12
        iconNames.add("playground");//13
        iconNames.add("peace");//14
        iconNames.add("cha cha");//15
        iconNames.add("dancing girls");//16
        iconNames.add("mannequin");//17
        iconNames.add("abc blocks 2");//18
        iconNames.add("megaphone 2");//19
        iconNames.add("tattoo parlor");//20
        iconNames.add("priest");//21
        iconNames.add("musician");//22
        iconNames.add("taxi driver");//23
        iconNames.add("music note");//24
        iconNames.add("tv");
        iconNames.add("president");
        iconNames.add("fish");
        iconNames.add("piggy bank");
        iconNames.add("tie");
        iconNames.add("makeover");
        iconNames.add("flower");
        iconNames.add("bus");
        iconNames.add("crawl");
        iconNames.add("car");
        iconNames.add("axe");
        iconNames.add("guitarist");
        iconNames.add("piano");
        iconNames.add("shoe");
        iconNames.add("dove");
        iconNames.add("rings");
    }
}
