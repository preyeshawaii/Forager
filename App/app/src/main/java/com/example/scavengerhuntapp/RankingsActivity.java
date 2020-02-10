package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;

import android.util.Log;
import android.widget.Button;
import android.view.View;


import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class RankingsActivity extends AppCompatActivity {
    /**String[] teamArray = {"Team1","Team2","Team3","Team4",
     "Team5"};
     int[] teamScores = {4,3,40,20,1}; **/

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Button backBtn;
    private ListView teamsListView;
    private ArrayAdapter aAdapter;

    private List<String> teamNames;

    private String TAG = "RankingsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        backBtn = findViewById(R.id.rankings_go_back);
        teamsListView = findViewById(R.id.team_list);

        teamNames = new ArrayList<>();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RankingsActivity.this, HuntLandingActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
                intent.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));
                startActivity(intent);
            }
        });

        aAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, teamNames);
    }

    @Override
    protected void onStart(){
        super.onStart();
        loadRankings();
    }

    private void loadRankings(){
        teamNames.clear();

        String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Team.KEY_TEAMS)
                .orderBy(Team.KEY_POINTS, Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            teamNames.add(documentSnapshot.toObject(Team.class).getTeamName());
                        }

                        teamsListView.setAdapter(aAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }
}
