package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    static final String ORGANIZER = "organizer";
    static final String PLAYER = "player";


    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private  String TAG = "MainActivity";
    private int RC_SIGN_IN = 1;

    private String userType;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInButton = findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioGroup userType = findViewById(R.id.radioGroup_userType);
                if (userType.getCheckedRadioButtonId() == -1)
                    Toast.makeText(MainActivity.this, "Select User Type", Toast.LENGTH_SHORT).show();
                else {
                    signIn();
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            loadOrCreateNewUser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Sign in Failed", Toast.LENGTH_LONG).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void loadOrCreateNewUser(FirebaseUser fUser){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {

            String userID = mAuth.getCurrentUser().getUid();
            final DocumentReference documentReference = db.collection(userType + "s").document(userID);

            db.collection(userType + "s").document(userID).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                                Log.w(TAG, "Loading user information");
                                user = documentSnapshot.toObject(User.class);
                                loadUser();

                            } else{
                                Log.w(TAG, "Creating new user");
                                createNewUser(documentReference);
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
    }

    public void loadUser(){
        Log.d(TAG, "Loaded user: " + user.getUserID());
        Toast.makeText(getApplicationContext(), "Logged in as an " + userType, Toast.LENGTH_SHORT).show();

        if (userType == ORGANIZER){
            openOrganizerGamesListActivity();
        } else{
            openPlayerGamesListActivity();
        }
    }

    public void createNewUser(DocumentReference documentReference){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        String userID = mAuth.getCurrentUser().getUid();

        String personName = acct.getDisplayName();
        //String personGivenName = acct.getGivenName();
        //String personFamilyName = acct.getFamilyName();
        String personEmail = acct.getEmail();
        //String personId = acct.getId();
        //Uri personPhoto = acct.getPhotoUrl();
        //List<String> emptyHuntsList = new ArrayList<String>();

        user = new User(userID, personName, personEmail, userType);

        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Created profile successfully for user: " + user.getUserID());

                Toast.makeText(getApplicationContext(), "Created an " + userType, Toast.LENGTH_SHORT).show();

                if (userType == ORGANIZER){
                    openOrganizerGamesListActivity();
                } else{
                    openPlayerGamesListActivity();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
    }

    public void openOrganizerGamesListActivity(){
        Intent intent = new Intent(this, OrganizerLandingActivity.class);
        startActivity(intent);
    }

    public void openPlayerGamesListActivity(){
        Intent intent = new Intent(this, PlayerLandingActivity.class);
        startActivity(intent);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_organizer:
                if (checked)
                    userType = ORGANIZER;
                break;
            case R.id.radio_player:
                if (checked)
                    userType = PLAYER;
                break;
        }
    }
}
