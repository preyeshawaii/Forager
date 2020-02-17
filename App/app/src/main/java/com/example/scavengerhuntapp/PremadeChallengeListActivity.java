package com.example.scavengerhuntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.example.scavengerhuntapp.CreatingHuntSingleton.CHALLENGES;

public class PremadeChallengeListActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ListView premadeChallengesList;
    private Button addToHuntButton;

    private List<Challenge> challengesToAdd;
    private CustomAdapter customAdapter;
    private CreatingHuntSingleton creatingHuntSingleton;

    private  String TAG = "PremadeChallengeListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premade_challenge_list);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        premadeChallengesList = findViewById(R.id.premade_challenge_list);
        addToHuntButton = findViewById(R.id.add_to_hunt_button);

        challengesToAdd = new ArrayList<>();
        customAdapter = new CustomAdapter();
        creatingHuntSingleton = creatingHuntSingleton.init();

        addToHuntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < premadeChallengesList.getCount(); i++) {
                    CheckBox checkedBox = premadeChallengesList.getChildAt(i).findViewById(R.id.checkBox);
                    
                    if (checkedBox.isChecked() == true){
                        addHunt(i);
                    }
                }
                Intent intent = new Intent(PremadeChallengeListActivity.this, CreateHuntActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addHunt(int i){
        final String uniqueID = UUID.randomUUID().toString();
        String description = creatingHuntSingleton.CHALLENGES[i];
        String location = creatingHuntSingleton.LOCATIONS[i];
        int icon = creatingHuntSingleton.ICONS[i];
        Integer points = 10;
        Challenge challenge = new Challenge(uniqueID, description, location, points, icon);
        creatingHuntSingleton.addChallenge(challenge);
    }

    @Override
    protected void onStart(){
        super.onStart();

        premadeChallengesList.setChoiceMode(premadeChallengesList.CHOICE_MODE_MULTIPLE);
        premadeChallengesList.setAdapter(customAdapter);
    }

    class CustomAdapter extends BaseAdapter{
        private List<Boolean> bools;

        @Override
        public int getCount() {
            return creatingHuntSingleton.CHALLENGES.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public Boolean isChecked(int i){
           return bools.get(i);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.challenge_custom_view,null);

            bools = Collections.nCopies(CHALLENGES.length, false);

            // Populate list view
            ImageView imageView = view.findViewById(R.id.iconImageView);
            TextView challengeTextView = view.findViewById(R.id.challengeTextView);
            TextView challengeLocationTextView = view.findViewById(R.id.challengeLocationTextView);
            CheckBox checkBox = view.findViewById(R.id.checkBox);

            imageView.setImageResource(creatingHuntSingleton.ICONS[i]);
            challengeTextView.setText(creatingHuntSingleton.CHALLENGES[i]);
            challengeLocationTextView.setText(creatingHuntSingleton.LOCATIONS[i]);
            checkBox.setChecked(bools.get(i));

            return view;
        }
    }
}
