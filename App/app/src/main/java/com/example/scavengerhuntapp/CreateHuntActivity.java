package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class CreateHuntActivity extends AppCompatActivity implements CustomChallengeDialog.CustomChallengeListener {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private EditText huntNameEditText;
    private Button premadeChallButton;
    private Button createHuntButton;
    private Button customChallButton;

    private  String TAG = "CreateHuntActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hunt);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        huntNameEditText = findViewById(R.id.hunt_name_et);
        premadeChallButton = findViewById(R.id.premade_chall_button);
        createHuntButton = findViewById(R.id.create_hunt_button);
        customChallButton = findViewById(R.id.custom_chall_button);

        premadeChallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateHuntActivity.this, PremadeHuntsActivity.class);
                startActivity(intent);
            }
        });

        createHuntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (huntNameEditText.getText().toString().length() == 0){
                    Toast.makeText(CreateHuntActivity.this, "The hunt needs a name!", Toast.LENGTH_SHORT).show();
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

    private void CreateHunt() {
        final String uniqueID = UUID.randomUUID().toString();
        final Hunt hunt = new Hunt(uniqueID, huntNameEditText.getText().toString());

        db.collection(Hunt.KEY_HUNTS).document(uniqueID).set(hunt)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        UpdateUserHuntList(uniqueID, hunt.getHuntName());
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
                            user.addHunt(huntID, huntName);
                            db.collection(User.KEY_ORGANIZERS).document(userID).set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
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

    public void openDialog() {
        CustomChallengeDialog customChallenge = new CustomChallengeDialog();
        customChallenge.show(getSupportFragmentManager(), "custom challenge");
    }

    @Override
    public void getTexts(String challenge, String location, Integer points) {
        // TODO(@Jorge) Access strings here for the database
        System.out.println("Challenge: " + challenge
                + ", Location: " + location + ", Points: " + points.toString());
    }
}
