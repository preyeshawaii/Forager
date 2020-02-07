package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private List<String> challengeNames;

    private  String TAG = "CurrentChallengesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_challenges);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        currentChallengesListView = findViewById(R.id.curr_challenges_list);

        challengesList = new ArrayList<>();
        challengeNames = new ArrayList<>();
    }

    @Override
    protected void onStart(){
        super.onStart();
        createCurrChallListView();
    }

    private void createCurrChallListView(){
        challengesList.clear();
        challengeNames.clear();

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

                        createArrayAdapter();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    private void createArrayAdapter(){
        getChallengeNames();

        ArrayAdapter<String> prevHuntNamesArray = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, challengeNames);
        currentChallengesListView.setAdapter(prevHuntNamesArray);

        // Add popup to look at details, not activity
    }

    private void getChallengeNames(){
        for (Challenge currChallenge: challengesList){
            challengeNames.add(currChallenge.getDescription());
            Log.w(TAG, currChallenge.getDescription());
        }
    }
}
