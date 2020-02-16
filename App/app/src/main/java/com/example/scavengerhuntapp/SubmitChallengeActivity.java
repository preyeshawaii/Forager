package com.example.scavengerhuntapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

// later: ability to take photo directly in the application.

public class SubmitChallengeActivity extends AppCompatActivity {
    public static final int GET_FROM_GALLERY = 1;

    private StorageReference mStorageRef;
    private FirebaseFirestore db;

    private StorageTask mUploadTask;

    private Button uploadPhotoButton;
    private ImageView imageView;
    private EditText submissionComment;
    private ProgressBar mProgressBar;
    private Button submitChallengeButton;

    private String huntID;
    private String huntName;
    private Submission submission;

    private Uri mImageUri;

    private String TAG = "SubmitChallengeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_challenge_player);

        mStorageRef = FirebaseStorage.getInstance().getReference(Hunt.KEY_HUNTS);
        db = FirebaseFirestore.getInstance();

        huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        huntName = getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME);

        uploadPhotoButton = findViewById(R.id.upload_photo_button);
        imageView = findViewById(R.id.image_view);
        submissionComment = findViewById(R.id.submitChallengeText);
        mProgressBar = findViewById(R.id.progress_bar);
        submitChallengeButton = findViewById(R.id.submit_challenge_button);

        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        submitChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(SubmitChallengeActivity.this, "Submission in progress", Toast.LENGTH_SHORT).show();
                } else {
                    submitChallenge();
                }

            }
        });

    }

    public void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GET_FROM_GALLERY);
    }

    // can send the request with the corresponding values.
    public void submitChallenge(){

        if (mImageUri != null) {
            initializeSubmission();
            final StorageReference fileReference = mStorageRef.child(huntID + "/" + submission.getTeamName() + "/" + System.currentTimeMillis() + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    submission.setmImageUrl(downloadUrl.toString());
                                    submission.setTeamComments(submissionComment.getText().toString());

                                    db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Submission.KEY_SUBMISSIONS).document(submission.getSubmissionID()).set(submission)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(SubmitChallengeActivity.this, "Submission successful", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(SubmitChallengeActivity.this, PlayerHuntLandingActivity.class);
                                                    intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                                                    intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                                                    intent.putExtra(Team.KEY_TEAM_NAME, submission.getTeamName());
                                                    intent.putExtra(Team.KEY_TEAM_ID, submission.getTeamID());
                                                    startActivity(intent);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, e.toString());
                                                }
                                            });
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SubmitChallengeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void initializeSubmission(){
        String id = Utils.uniqueID(Submission.SUBMISSION_ID_LENGTH);

        String teamID = getIntent().getExtras().getString(Team.KEY_TEAM_ID);
        String teamName = getIntent().getExtras().getString(Team.KEY_TEAM_NAME);
        String icon = getIntent().getExtras().getString(Submission.KEY_ICON);
        String description = getIntent().getExtras().getString(Submission.KEY_DESCRIPTION);
        String location = getIntent().getExtras().getString(Submission.KEY_LOCATION);
        //String points = getIntent().getExtras().getString(Submission.KEY_POINTS);

        submission = new Submission(id, teamID, teamName, description, icon, location, "10");
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(imageView);
        }
    }
}
