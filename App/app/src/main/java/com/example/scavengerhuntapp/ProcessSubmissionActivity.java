package com.example.scavengerhuntapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProcessSubmissionActivity  extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_submission);
        Intent currIntent = getIntent();

        TextView teamName = findViewById(R.id.process_submission_team_name);
        teamName.setText(currIntent.getStringExtra("teamName"));
        TextView challengePoints = findViewById(R.id.process_submissioon_team_points);
        challengePoints.setText(currIntent.getStringExtra("challengePoints") + " points");
        TextView message = findViewById(R.id.process_submission_text);
        message.setText(currIntent.getStringExtra("message"));
    }

    public void clickedApprove(View v){
        Intent intent = new Intent(this, SubmissionsActivity.class);
        startActivity(intent);
    }

    public void clickedReject(View v){
        Intent intent = new Intent(this, SubmissionsActivity.class);
        startActivity(intent);
    }


}
