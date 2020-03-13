package com.example.scavengerhuntapp.organizer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scavengerhuntapp.R;
import com.example.scavengerhuntapp.objects.Challenge;
import com.example.scavengerhuntapp.objects.Hunt;
import com.example.scavengerhuntapp.objects.Submission;
import com.example.scavengerhuntapp.objects.Team;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProcessSubmissionActivity  extends AppCompatActivity {
    private StorageReference storage;

    private FirebaseFirestore db;

    private TextView teamName;
    private TextView challengePoints;
    private TextView message;
    private TextView description;
    private TextView location;
    private ImageView imageView;
    private ImageView iconView;

    private String huntID;
    private String submissionID;
    private String teamID;
    private Challenge challenge;

    private String TAG = "ProcessSubmissionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_submission);

        db = FirebaseFirestore.getInstance();

        teamName = findViewById(R.id.process_submission_team_name);
        challengePoints = findViewById(R.id.challengePoints);
        location = findViewById(R.id.challengeLocationTextView);
        message = findViewById(R.id.process_submission_challenge_description);
        description = findViewById(R.id.challengeTextView);
        imageView = findViewById(R.id.process_submission_photo);
        iconView = findViewById(R.id.iconImageView);

        Button submitButton = findViewById(R.id.submitButtonChallenge);
        submitButton.setVisibility(View.GONE);

        View checkMarkBox = findViewById(R.id.checkBox);
        checkMarkBox.setVisibility(View.GONE);
    }


    @Override
    protected void onStart(){
        super.onStart();

        huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        submissionID = getIntent().getExtras().getString(Submission.KEY_SUBMISSION_ID);
        String challengeID = getIntent().getExtras().getString(Challenge.KEY_CHALLENGE_ID);
        String teamName = getIntent().getExtras().getString(Team.KEY_TEAM_NAME);
        teamID = getIntent().getExtras().getString(Team.KEY_TEAM_ID);
        String description = getIntent().getExtras().getString(Submission.KEY_DESCRIPTION);
        String location = getIntent().getExtras().getString(Submission.KEY_LOCATION);
        String icon = getIntent().getExtras().getString(Submission.KEY_ICON);
        String points = getIntent().getExtras().getString(Submission.KEY_POINTS);
        String teamComments = getIntent().getExtras().getString(Submission.KEY_TEAM_COMMENTS);
        String imageURI = getIntent().getExtras().getString(Submission.KEY_IMAGE_URI);
        String iconStr = getIntent().getExtras().getString(Submission.KEY_ICON);

        challenge = new Challenge(challengeID, description, location, Integer.parseInt(points), Integer.parseInt(icon));

        Log.w(TAG, "URI" + imageURI);

        this.teamName.setText(teamName);
        this.description.setText(description);

        this.challengePoints.setText(points + " Pts");
        this.location.setText(location);
        this.message.setText(teamComments);
        this.iconView.setImageResource(Integer.parseInt(iconStr));

        Picasso.get().load(Uri.parse(imageURI)).into(imageView);
    }

    public void clickedApprove(View v){
        acceptOrReject(true);

    }

    public void clickedReject(View v){
        acceptOrReject(false);
    }

    private void acceptOrReject(final Boolean isAccepted){
        db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Submission.KEY_SUBMISSIONS).document(submissionID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        final Submission sub = documentSnapshot.toObject(Submission.class);
                        if (isAccepted){
                            sub.setState(Challenge.KEY_ACCEPTED);
                        } else {
                            sub.setState(Challenge.KEY_REJECTED);
                        }
                        db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Submission.KEY_SUBMISSIONS).document(submissionID).set(sub)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        updateChallenge(isAccepted);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, e.toString());
                                    }
                                });

                        if (isAccepted){
                            db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Team.KEY_TEAMS).document(teamID).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Team team = documentSnapshot.toObject(Team.class);
                                            team.setPoints(team.getPoints() + sub.getPoints());
                                            db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Team.KEY_TEAMS).document(teamID).set(team)
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG, e.toString());
                                                        }
                                                    });
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    private void updateChallenge(Boolean isAccepted) {
        if (isAccepted){
            challenge.setState(Challenge.KEY_ACCEPTED);
            Toast.makeText(ProcessSubmissionActivity.this, "Accepted Submission", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ProcessSubmissionActivity.this, "Rejected Submission", Toast.LENGTH_SHORT).show();
            challenge.setState(Challenge.KEY_REJECTED);
        }

        db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Team.KEY_TEAMS).document(teamID).collection(Challenge.KEY_CHALLENGES).document(challenge.getChallengeID()).set(challenge)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), SubmissionsActivity.class);
                        intent.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
                        intent.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));
                        startActivity(intent);
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
