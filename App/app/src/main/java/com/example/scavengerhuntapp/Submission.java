package com.example.scavengerhuntapp;

public class Submission {
    static final String KEY_SUBMISSIONS = "submissions";
    static final String KEY_SUBMISSION_ID = "submissionID";

    private String submissionID;
    private String description;
    private String teamName;
    private String playerComments;
    private Integer points;
    private Boolean hasBeenReviewed;
    private Boolean accepted;

    public Submission(){ }

    public Submission(String submissionID){
        this.submissionID = submissionID;
        this.description = "";
        this.teamName = "";
        this.playerComments = "No Comment";
        this.points = 0;
        this.hasBeenReviewed = false;
        this.accepted = false;
    }

    public String getSubmissionID() {
        return submissionID;
    }

    public String getDescription() {
        return description;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getPlayerComments() {
        return playerComments;
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
}
