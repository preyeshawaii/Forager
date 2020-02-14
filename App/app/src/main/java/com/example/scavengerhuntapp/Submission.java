package com.example.scavengerhuntapp;

import com.google.firebase.firestore.Exclude;

public class Submission {
    static final int SUBMISSION_ID_LENGTH =  12;
    static final String KEY_SUBMISSIONS = "submissions";
    static final String KEY_SUBMISSION_ID = "submissionID";
    static final String KEY_TEAM_NAME = "teamName";
    static final String KEY_POINTS = "points";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_LOCATION = "location";
    static final String KEY_ICON = "icon";
    static final String KEY_TEAM_COMMENTS = "teamComments";
    static final String KEY_IMAGE_URI = "imageURI";

    private String submissionID;
    private String teamID;
    private String teamName;
    private String description;
    private int icon;
    private String location;
    private int points;
    private String mImageUrl;
    private String teamComments;
    private Boolean hasBeenReviewed;
    private Boolean accepted;

    public Submission(){ }

    public Submission(String submissionID, String teamID, String teamName, String description, String icon, String location, String points){
        this.submissionID = submissionID;
        this.teamID = teamID;
        this.teamName = teamName;
        this.description = description;
        this.icon = Integer.parseInt(icon);
        this.location = location;
        this.points = Integer.parseInt(points);

        this.mImageUrl = "";
        this.teamComments = "No Comments";

        this.hasBeenReviewed = false;
        this.accepted = false;
    }

    public String getSubmissionID() {
        return submissionID;
    }

    public String getTeamID() {
        return teamID;
    }

    public String getDescription() {
        return description;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamComments() {
        return teamComments;
    }

    public Integer getPoints() {
        return points;
    }

    public Boolean getHasBeenReviewed() {
        return hasBeenReviewed;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
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
