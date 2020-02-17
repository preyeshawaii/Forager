package com.example.scavengerhuntapp;

import com.google.firebase.firestore.Exclude;

public class Submission extends Challenge {

    static final int SUBMISSION_ID_LENGTH =  12;
    static final String KEY_SUBMISSIONS = "submissions";
    static final String KEY_SUBMISSION_ID = "submissionID";
    static final String KEY_TEAM_COMMENTS = "teamComments";
    static final String KEY_IMAGE_URI = "imageURI";

    private String submissionID;
    private String teamID;
    private String teamName;
    private String teamComments;
    private String imageURL;



    public Submission(){ }

    public Submission(String challengeID, String submissionID, String teamID, String teamName, String description, String location, int points, int icon){
        super(challengeID, description, location, points, icon);
        this.submissionID = submissionID;
        this.teamID = teamID;
        this.teamName = teamName;

        this.teamComments = "No Comments";
        this.imageURL = "";
    }

    public String getDescription() {
        return super.getDescription();
    }

    public String getLocation() {
        return super.getLocation();
    }

    public int getPoints() {
        return super.getPoints();
    }

    public int getIcon() {
        return super.getIcon();
    }

    public String getSubmissionID() {
        return submissionID;
    }

    public String getTeamID() {
        return teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getTeamComments() {
        return teamComments;
    }


    public String getState() {
        return super.getState();
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setTeamComments(String teamComments) {
        this.teamComments = teamComments;
    }

    @Exclude
    public void submissionAccepted(){
        super.setState(Challenge.KEY_ACCEPTED);
    }

    @Exclude
    public void submissionRejected(){
        super.setState(Challenge.KEY_REJECTED);
    }

    @Exclude
    public void submissionInReview(){
        super.setState(Challenge.KEY_IN_REVIEW);
    }

}
