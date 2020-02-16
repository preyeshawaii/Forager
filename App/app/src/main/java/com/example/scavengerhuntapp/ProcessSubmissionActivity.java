package com.example.scavengerhuntapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProcessSubmissionActivity  extends AppCompatActivity {
    private StorageReference storage;
    private FirebaseFirestore db;

    private TextView teamName;
    private TextView challengePoints;
    private TextView message;
    private TextView description;
    private ImageView imageView;

    private String huntID;
    private String submissionID;

    private String TAG = "ProcessSubmissionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_submission);

        storage = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        teamName = findViewById(R.id.process_submission_team_name);
        challengePoints = findViewById(R.id.challengePoints);
        message = findViewById(R.id.process_submission_challenge_description);
        description = findViewById(R.id.challengeTextView);
        imageView = findViewById(R.id.process_submission_photo);
    }

    @Override
    protected void onStart(){
        super.onStart();

        huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        submissionID = getIntent().getExtras().getString(Submission.KEY_SUBMISSION_ID);
        String description = getIntent().getExtras().getString(Submission.KEY_DESCRIPTION);
        String teamComments = getIntent().getExtras().getString(Submission.KEY_TEAM_COMMENTS);
        String points = getIntent().getExtras().getString(Submission.KEY_POINTS);
        String teamName = getIntent().getExtras().getString(Submission.KEY_TEAM_NAME);
        String imageURI = getIntent().getExtras().getString(Submission.KEY_IMAGE_URI);

        Log.w(TAG, "URI" + imageURI);

        this.teamName.setText(teamName);
        this.description.setText(description);
        challengePoints.setText(points + " points");
        message.setText(teamComments);

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
                        Submission sub = documentSnapshot.toObject(Submission.class);
                        if (isAccepted){
                            sub.submissionAccepted();
                        } else{
                            sub.submissionRejected();
                        }


                        db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Submission.KEY_SUBMISSIONS).document(submissionID).set(sub)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (isAccepted){
                                            Toast.makeText(ProcessSubmissionActivity.this, "Accepted Submission", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ProcessSubmissionActivity.this, "Rejected Submission", Toast.LENGTH_SHORT).show();
                                        }

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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }


}
