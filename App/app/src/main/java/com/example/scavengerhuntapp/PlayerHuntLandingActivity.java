package com.example.scavengerhuntapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerHuntLandingActivity extends AppCompatActivity {


    private Button rankingsButton;
    private Button announcementsButton;
    private Button teamsButton;

    private ListView challengesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_hunt_landing);

        rankingsButton = findViewById(R.id.rankings_button);
        announcementsButton = findViewById(R.id.announcements_button);
        teamsButton = findViewById(R.id.teams_button);
        challengesListView = findViewById(R.id.challenge_list);

        rankingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerHuntLandingActivity.this, RankingsActivity.class);
                //intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                //intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                startActivity(intent);
            }
        });

        teamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerHuntLandingActivity.this, TeamsActivity.class);
                //intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                //intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                startActivity(intent);
            }
        });

        announcementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerHuntLandingActivity.this, AnnouncementsActivity.class);
                //intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                //intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                startActivity(intent);
            }
        });


    }
}

