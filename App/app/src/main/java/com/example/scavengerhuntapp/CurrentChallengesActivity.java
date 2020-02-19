package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.MenuInflater;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CurrentChallengesActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ListView currentChallengesListView;
    private List<Challenge> challengesList;

    private CreatingHuntSingleton creatingHuntSingleton;

    private CustomAdapter huntNamesArray;

    private  String TAG = "CurrentChallengesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_challenges);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        currentChallengesListView = findViewById(R.id.curr_challenges_list);

        creatingHuntSingleton = creatingHuntSingleton.init();
        challengesList = new ArrayList<>();
        huntNamesArray = new CustomAdapter();



        final String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        final String huntName = getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME);



        BottomNavigationView bottomNavigationView = findViewById(R.id.organizer_bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_challenges:
                        Intent intent = new Intent(CurrentChallengesActivity.this, CurrentChallengesActivity.class);
                        intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                        startActivity(intent);
                        break;
                    case R.id.action_submissions:
                        Intent intent1 = new Intent(CurrentChallengesActivity.this, SubmissionsActivity.class);
                        intent1.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent1.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                        startActivity(intent1);
                        break;
                    case R.id.action_broadcast:
                        Intent intent2 = new Intent(CurrentChallengesActivity.this, BroadcastActivity.class);
                        intent2.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent2.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                        startActivity(intent2);
                        break;
                    case R.id.action_rankings:
                        Intent intent3 = new Intent(CurrentChallengesActivity.this, HuntLandingActivity.class);
                        intent3.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent3.putExtra(Hunt.KEY_HUNT_NAME, huntName);

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
                        currentChallengesListView.setAdapter(huntNamesArray);
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
            TextView points = view.findViewById(R.id.challengePoints);
            CheckBox checkBox = view.findViewById(R.id.checkBox);

            // remove submit button here
            Button submitButton = view.findViewById(R.id.submitButtonChallenge);
            submitButton.setVisibility(View.GONE);

            imageView.setImageResource(challengesList.get(i).getIcon());
            challengeTextView.setText(challengesList.get(i).getDescription());
            challengeLocationTextView.setText(challengesList.get(i).getLocation());
            points.setText(challengesList.get(i).getPoints() + " Pts");
            checkBox.setVisibility(View.GONE);

            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_all_hunts:
                Intent intent = new Intent(CurrentChallengesActivity.this, OrganizerLandingActivity.class);
                startActivity(intent);
                break;
            case R.id.action_sign_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(CurrentChallengesActivity.this, MainActivity.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
