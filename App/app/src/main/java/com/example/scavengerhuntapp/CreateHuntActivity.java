package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CreateHuntActivity extends AppCompatActivity implements CustomChallengeDialog.CustomChallengeListener{
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private EditText huntNameEditText;
    private ListView challengeList;
    private Button premadeChallButton;
    private Button createHuntButton;
    private Button customChallButton;

    private CreatingHuntSingleton creatingHuntSingleton;
    private CustomAdapter pendingChallenges;

    private  String TAG = "CreateHuntActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hunt);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        huntNameEditText = findViewById(R.id.hunt_name_et);
        challengeList = findViewById(R.id.challenge_list);
        premadeChallButton = findViewById(R.id.premade_chall_button);
        createHuntButton = findViewById(R.id.create_hunt_button);
        customChallButton = findViewById(R.id.custom_chall_button);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        creatingHuntSingleton = creatingHuntSingleton.init();
        pendingChallenges = new CustomAdapter();

        premadeChallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creatingHuntSingleton.setHuntTitle(huntNameEditText.getText().toString());
                Intent intent = new Intent(CreateHuntActivity.this, PremadeHuntsActivity.class);
                startActivity(intent);
            }
        });

        createHuntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (huntNameEditText.getText().toString().length() == 0){
                    Toast.makeText(CreateHuntActivity.this, "The hunt needs a name!", Toast.LENGTH_SHORT).show();
                } else if(creatingHuntSingleton.getChallenges().isEmpty()){
                    Toast.makeText(CreateHuntActivity.this, "You need to add challenge(s) to the hunt", Toast.LENGTH_SHORT).show();
                }else{
                    CreateHunt();
                }
            }
        });

        customChallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        challengeList.setChoiceMode(challengeList.CHOICE_MODE_MULTIPLE);
        challengeList.setAdapter(pendingChallenges);
        huntNameEditText.setText(creatingHuntSingleton.getHuntTitle());
    }

    private void CreateHunt() {
        final String uniqueID = Utils.generateHuntID();
        final Hunt hunt = new Hunt(uniqueID, huntNameEditText.getText().toString());

        db.collection(Hunt.KEY_HUNTS).document(uniqueID).set(hunt)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        UpdateUserHuntList(uniqueID, hunt.getHuntName());
                        AddChallengesToHunt(uniqueID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    private void UpdateUserHuntList(final String huntID, final String huntName){
        final String userID = mAuth.getCurrentUser().getUid();

        db.collection(User.KEY_ORGANIZERS).document(userID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            Log.w(TAG, "Create hunt");
                            User user = documentSnapshot.toObject(User.class);
                            Map<String, String> huntValues = new HashMap<>();
                            huntValues.put(Hunt.KEY_HUNT_NAME, huntName);
                            user.addHunt(huntID, huntValues);
                            db.collection(User.KEY_ORGANIZERS).document(userID).set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            creatingHuntSingleton.clearHunt();
                                            Intent intent = new Intent(CreateHuntActivity.this, HuntLandingActivity.class);
                                            intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                                            intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                                            startActivity(intent);
                                        }
                                    });

                        } else{
                            Log.w(TAG, "Could not create the hunt");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    private void AddChallengesToHunt(String uniqueID){
        for (Challenge challenge : creatingHuntSingleton.getChallenges()){
            db.collection(Hunt.KEY_HUNTS).document(uniqueID).collection(Challenge.KEY_CHALLENGES)
                    .document(challenge.getChallengeID()).set(challenge)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.w(TAG, "Successfully added a challenge");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, e.toString());
;                        }
                    });
        }

    }

    public void openDialog() {
        CustomChallengeDialog customChallenge = new CustomChallengeDialog();
        customChallenge.show(getSupportFragmentManager(), "custom challenge");
    }


    public void getTexts(String challengeDes, String location, Integer points) {
        final String uniqueID = Utils.generateHuntID();
        Challenge challenge = new Challenge(uniqueID, challengeDes, location, points, R.drawable.icecream);
        creatingHuntSingleton.addChallenge(challenge);
        challengeList.setAdapter(pendingChallenges);
    }

    class CustomAdapter extends BaseAdapter {
        private List<Challenge> challenges = creatingHuntSingleton.getChallenges();

        @Override
        public int getCount() {
            return challenges.size();
        }

        @Override
        public Object getItem(int i) {
            return challenges.get(i);
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
            TextView pointsView = view.findViewById(R.id.challengePoints);
            CheckBox checkBox = view.findViewById(R.id.checkBox);

            Button submitButton = view.findViewById(R.id.submitButtonChallenge);
            submitButton.setVisibility(View.GONE);
            imageView.setImageResource(challenges.get(i).getIcon());
            challengeTextView.setText(challenges.get(i).getDescription());
            challengeLocationTextView.setText(challenges.get(i).getLocation());
            pointsView.setText(String.valueOf(challenges.get(i).getPoints()) + " pts");
            checkBox.setVisibility(View.GONE);

            return view;
        }
    }
}
