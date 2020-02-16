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
    private Boolean hasBeenReviewed;
    private Boolean accepted;

    public Submission(){ }

    public Submission(String submissionID, String teamID, String teamName, String description, String location, int points, int icon){
        super(description, location, points, icon);
        this.submissionID = submissionID;
        this.teamID = teamID;
        this.teamName = teamName;

        this.teamComments = "No Comments";
        this.imageURL = "";

        this.hasBeenReviewed = false;
        this.accepted = false;
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

    public Boolean getHasBeenReviewed() {
        return hasBeenReviewed;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setTeamComments(String teamComments) {
        this.teamComments = teamComments;
    }

    public void setHasBeenReviewed(Boolean hasBeenReviewed) {
        this.hasBeenReviewed = hasBeenReviewed;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    @Exclude
    public void submissionAccepted(){
        setAccepted(true);
        setHasBeenReviewed(true);
    }

    @Exclude
    public void submissionRejected(){
        setAccepted(false);
        setHasBeenReviewed(true);
    }

}
