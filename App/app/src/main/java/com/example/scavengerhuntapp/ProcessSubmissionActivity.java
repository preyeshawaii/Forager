package com.example.scavengerhuntapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProcessSubmissionActivity  extends AppCompatActivity {
    private TextView teamName;
    private TextView challengePoints;
    private TextView message;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_submission);

        teamName = findViewById(R.id.process_submission_team_name);
        challengePoints = findViewById(R.id.process_submission_team_points);
        message = findViewById(R.id.process_submission_text);
        description = findViewById(R.id.process_submission_challenge_description);
    }

    @Override
    protected void onStart(){
        super.onStart();

        String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        String submissionID = getIntent().getExtras().getString(Submission.KEY_SUBMISSION_ID);
        String description = getIntent().getExtras().getString(Submission.KEY_DESCRIPTION);
        String teamComments = getIntent().getExtras().getString(Submission.KEY_TEAM_COMMENTS);
        String points = getIntent().getExtras().getString(Submission.KEY_POINTS);
        String teamName = getIntent().getExtras().getString(Submission.KEY_TEAM_NAME);

        this.teamName.setText(teamName);
        this.description.setText(description);
        challengePoints.setText(points + " points");
        message.setText(teamComments);
    }

    public void clickedApprove(View v){
        Intent intent = new Intent(this, SubmissionsActivity.class);
        intent.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
        intent.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));
        startActivity(intent);
    }

    public void clickedReject(View v){
        Intent intent = new Intent(this, SubmissionsActivity.class);
        intent.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
        intent.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));
        startActivity(intent);
    }


}
