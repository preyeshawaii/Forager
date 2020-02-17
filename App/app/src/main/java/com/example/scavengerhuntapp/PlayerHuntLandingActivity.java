package com.example.scavengerhuntapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PlayerHuntLandingActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextView title;

    private ListView challengesListView;

    private List<Challenge> challengesList;
    private CustomAdapter huntNamesArray;

    private String huntID;
    private String huntName;
    private String teamName;
    private String teamID;

    private String TAG = "PlayerHuntLandingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_hunt_landing);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        title = findViewById(R.id.hunt_name_text_view);
        challengesListView = findViewById(R.id.challenge_list);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        huntName = getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME);
        teamName = getIntent().getExtras().getString(Team.KEY_TEAM_NAME);
        teamID = getIntent().getExtras().getString(Team.KEY_TEAM_ID);

        title.setText(huntName);

        challengesList = new ArrayList<>();
        huntNamesArray = new CustomAdapter();

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_alerts:
                        Intent intent = new Intent(PlayerHuntLandingActivity.this, AnnouncementsActivity.class);
                        intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                        intent.putExtra(Team.KEY_TEAM_NAME, teamName);
                        startActivity(intent);
                        break;
                    case R.id.action_challenges:
                        break;
                    case R.id.action_team:
                        break;
                    case R.id.action_rankings:
                        Intent intent3 = new Intent(PlayerHuntLandingActivity.this, RankingsActivity.class);
                        intent3.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent3.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                        intent3.putExtra(Team.KEY_TEAM_NAME, teamName);
                        intent3.putExtra(User.KEY_PLAYER_TYPE, User.KEY_PLAYER);

                        startActivity(intent3);
                        break;
                }

                return false;
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        createCurrChallListView();
    }

    private void createCurrChallListView(){
        challengesList.clear();

        String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        Log.w(TAG, "Given huntID: " + huntID);

        db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Challenge.KEY_CHALLENGES).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots){
                        Log.w(TAG, Integer.toString(queryDocumentSnapshots.size()));
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            challengesList.add(documentSnapshot.toObject(Challenge.class));
                            Log.w(TAG, documentSnapshot.getId());
                        }
                        challengesListView.setAdapter(huntNamesArray);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return challengesList.size();
        }

        @Override
        public Object getItem(int i) {
            return challengesList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.challenge_custom_view, null);

            // TODO Need to make rejected/accepted/pending colors more appealing
            if (challengesList.get(i).getState().equals(Challenge.KEY_IN_REVIEW)){
                view.setBackgroundColor(Color.YELLOW);
            } else if (challengesList.get(i).getState().equals(Challenge.KEY_REJECTED)){
                view.setBackgroundColor(Color.RED);
            } else if (challengesList.get(i).getState().equals(Challenge.KEY_ACCEPTED)){
                view.setBackgroundColor(Color.GREEN);
            }

            // Populate list view
            ImageView imageView = view.findViewById(R.id.iconImageView);
            TextView challengeTextView = view.findViewById(R.id.challengeTextView);
            TextView challengeLocationTextView = view.findViewById(R.id.challengeLocationTextView);
            CheckBox checkBox = view.findViewById(R.id.checkBox);
            Button submitChallenge = view.findViewById(R.id.submitButtonChallenge);
            TextView points = view.findViewById(R.id.challengePoints);

            submitChallenge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PlayerHuntLandingActivity.this, SubmitChallengeActivity.class);
                    intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                    intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                    intent.putExtra(Team.KEY_TEAM_NAME, teamName);
                    intent.putExtra(Team.KEY_TEAM_ID, teamID);
                    intent.putExtra(Challenge.KEY_CHALLENGE_ID, challengesList.get(i).getChallengeID());
                    intent.putExtra(Submission.KEY_DESCRIPTION, challengesList.get(i).getDescription());
                    intent.putExtra(Submission.KEY_LOCATION, challengesList.get(i).getLocation());
                    //intent.putExtra(Submission.KEY_POINTS, challengesList.get(i).getPoints());
                    intent.putExtra(Submission.KEY_ICON, String.valueOf(challengesList.get(i).getIcon()));
                    startActivity(intent);
                }
            });

            imageView.setImageResource(challengesList.get(i).getIcon());
            challengeTextView.setText(challengesList.get(i).getDescription());
            challengeLocationTextView.setText(challengesList.get(i).getLocation());
            //points.setText(challengesList.get(i).getPoints());
            checkBox.setVisibility(View.GONE);

            return view;
        }
    }
}

