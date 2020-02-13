package com.example.scavengerhuntapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private Button rankingsButton;
    private Button announcementsButton;
    private Button teamsButton;
    private ListView challengesListView;

    private List<Challenge> challengesList;
    private CustomAdapter huntNamesArray;

    private String TAG = "PlayerHuntLandingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_hunt_landing);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        title = findViewById(R.id.hunt_name_text_view);
        rankingsButton = findViewById(R.id.rankings_button);
        announcementsButton = findViewById(R.id.announcements_button);
        teamsButton = findViewById(R.id.teams_button);
        challengesListView = findViewById(R.id.challenge_list);

        final String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        final String huntName = getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME);
        final String teamName = getIntent().getExtras().getString(Team.KEY_TEAM_NAME);

        title.setText(huntName);

        challengesList = new ArrayList<>();
        huntNamesArray = new CustomAdapter();

        rankingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerHuntLandingActivity.this, RankingsActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                intent.putExtra(Team.KEY_TEAM_NAME, teamName);
                intent.putExtra(User.KEY_PLAYER_TYPE, User.KEY_PLAYER);
                startActivity(intent);
            }
        });

        teamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerHuntLandingActivity.this, TeamsActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                intent.putExtra(Team.KEY_TEAM_NAME, teamName);
                startActivity(intent);
            }
        });

        announcementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerHuntLandingActivity.this, AnnouncementsActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                intent.putExtra(Team.KEY_TEAM_NAME, teamName);
                startActivity(intent);
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.challenge_custom_view, null);

            // Populate list view
            ImageView imageView = view.findViewById(R.id.iconImageView);
            TextView challengeTextView = view.findViewById(R.id.challengeTextView);
            TextView challengeLocationTextView = view.findViewById(R.id.challengeLocationTextView);
            CheckBox checkBox = view.findViewById(R.id.checkBox);

            imageView.setImageResource(challengesList.get(i).getIcon());
            challengeTextView.setText(challengesList.get(i).getDescription());
            challengeLocationTextView.setText(challengesList.get(i).getLocation());
            // TODO Add points view here
            checkBox.setVisibility(View.GONE);

            return view;
        }
    }
}

