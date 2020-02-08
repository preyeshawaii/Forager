package com.example.scavengerhuntapp;

public class Submission {
    static final String KEY_SUBMISSIONS = "submissions";
    static final String KEY_SUBMISSION_ID = "submissionID";
    static final String KEY_TEAM_COMMENTS = "teamComments";
    static final String KEY_TEAM_NAME = "teamName";
    static final String KEY_POINTS = "points";
    static final String KEY_DESCRIPTION = "description";

    private String submissionID;
    private String description;
    private String teamName;
    private String teamComments;
    private Integer points;
    private Boolean hasBeenReviewed;
    private Boolean accepted;

    public Submission(){ }

    public Submission(String submissionID){
        this.submissionID = submissionID;
        this.description = "";
        this.teamName = "";
        this.teamComments = "No Comment";
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
}
