package com.example.scavengerhuntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HuntLandingActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextView title;
    private TextView joinCode;
    private Button teamsBtn;
    private Button rankingsBtn;
    private Button challangesBtn;
    private Button submissionsBtn;
    private Button broadcastBtn;
    private Button backBtn;

    private String TAG = "HuntLandingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt_landing);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        title = findViewById(R.id.hunt_landing_title);
        joinCode = findViewById(R.id.join_code_text);
        teamsBtn = findViewById(R.id.teams_button);
        rankingsBtn = findViewById(R.id.rankings_button);
        challangesBtn = findViewById(R.id.challenges_button);
        submissionsBtn = findViewById(R.id.submissions_button);
        broadcastBtn = findViewById(R.id.broadcast_button);
        backBtn = findViewById(R.id.hunt_landing_back_btn);

        final String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        final String huntName = getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME);

        title.setText(huntName);
        joinCode.setText(huntID);

        teamsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HuntLandingActivity.this, TeamsActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                startActivity(intent);
            }
        });

        rankingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HuntLandingActivity.this, RankingsActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                intent.putExtra(User.KEY_PLAYER_TYPE, User.KEY_ORGANIZER);
                startActivity(intent);
            }
        });

        challangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HuntLandingActivity.this, CurrentChallengesActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                startActivity(intent);
            }
        });

        submissionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HuntLandingActivity.this, SubmissionsActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                startActivity(intent);
            }
        });

        broadcastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HuntLandingActivity.this, BroadcastActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HuntLandingActivity.this, OrganizerLandingActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
    }
}
