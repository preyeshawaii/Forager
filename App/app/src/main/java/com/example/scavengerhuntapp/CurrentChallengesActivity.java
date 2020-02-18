package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
            CheckBox checkBox = view.findViewById(R.id.checkBox);

            // remove submit button here
            Button submitButton = view.findViewById(R.id.submitButtonChallenge);
            submitButton.setVisibility(View.GONE);

            imageView.setImageResource(challengesList.get(i).getIcon());
            challengeTextView.setText(challengesList.get(i).getDescription());
            challengeLocationTextView.setText(challengesList.get(i).getLocation());
            // TODO Add points view here
            checkBox.setVisibility(View.GONE);

            return view;
        }
    }
}
