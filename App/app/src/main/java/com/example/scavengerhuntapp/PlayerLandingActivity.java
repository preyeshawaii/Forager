package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerLandingActivity extends AppCompatActivity implements TeamDialog.TeamDialogListener{

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private PlayerHuntSingleton playerHuntSingleton;

    private Button signOutButton;
    private Button huntCodeButton;
    private TextView noHuntsView;
    private ListView huntsListView;
    private EditText huntCodeEditText;

    private Map<String, Map<String, String>> hunts;
    private Hunt hunt;


    private String TAG = "PlayerLandingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_landing);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        signOutButton = findViewById(R.id.sign_out);
        huntCodeButton = findViewById(R.id.hunt_code_button);
        huntsListView = findViewById(R.id.player_hunts_listView);
        noHuntsView = findViewById(R.id.no_player_hunts_view);
        huntCodeEditText = findViewById(R.id.hunt_code_et);

        hunts = new HashMap<>();

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(PlayerLandingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        huntCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkHuntCode();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        loadInfo();
    }

    private void loadInfo(){
        hunts.clear();

        db.collection(User.KEY_PLAYERS).document(mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.w(TAG, mAuth.getCurrentUser().getUid());
                        if (documentSnapshot.exists()){

                            hunts = (Map<String, Map<String, String>>)documentSnapshot.get(User.KEY_HUNTS);
                            createHuntListView();
                        } else{
                            Log.w(TAG, "Player does not exist");
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

    private void createHuntListView(){

        if (hunts.isEmpty()){
            Log.w(TAG, "No hunts found");
            noHuntsView.setVisibility(View.VISIBLE);
        } else{
            Log.w(TAG, hunts.size() + " hunt(s) found");
            noHuntsView.setVisibility(View.GONE);
            setUpHuntsListView();
        }
    }

    private void setUpHuntsListView(){
        final List<String> huntIDs = new ArrayList<>();
        final List<String> huntNames = new ArrayList<>();
        final List<String> teamNames = new ArrayList<>();
        final List<String> teamIDs = new ArrayList<>();

        for (Map.Entry<String, Map<String, String>> entry : hunts.entrySet()) {
            huntIDs.add(entry.getKey());
            huntNames.add(entry.getValue().get(Hunt.KEY_HUNT_NAME));
            teamNames.add(entry.getValue().get(Team.KEY_TEAM_NAME));
            teamIDs.add(entry.getValue().get(Team.KEY_TEAM_ID));
        }

        ArrayAdapter<String> prevHuntNamesArray = new ArrayAdapter<>(getApplicationContext(),
                R.layout.hunt_list_custom_view, R.id.hunt_name_content, huntNames);
        huntsListView.setAdapter(prevHuntNamesArray);

        huntsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String huntName = huntNames.get(position);
                String huntID = huntIDs.get(position);
                String teamName = teamNames.get(position);
                String teamID = teamIDs.get(position);



                Toast.makeText(getApplicationContext(), huntName, Toast.LENGTH_SHORT).show();
                establishPlayerHuntSingleton(huntID, huntName, teamID, teamName);

                Intent intent = new Intent(PlayerLandingActivity.this, PlayerHuntLandingActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                intent.putExtra(Team.KEY_TEAM_NAME, teamName);
                intent.putExtra(Team.KEY_TEAM_ID, teamID);
                Log.w(TAG, huntID + ": " + huntName);
                startActivity(intent);
            }
        });
    }

    private void checkHuntCode(){
        final String huntCode = huntCodeEditText.getText().toString();

        db.collection(Hunt.KEY_HUNTS).document(huntCode).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            hunt = documentSnapshot.toObject(Hunt.class);
                            openDialog();
                        } else {
                            Toast.makeText(PlayerLandingActivity.this, "Not a valid code!", Toast.LENGTH_SHORT).show();
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



    public void openDialog () {
        TeamDialog teamDialog = new TeamDialog();
        teamDialog.show(getSupportFragmentManager(), "Team dialog");
    }

    public void getTeamName(String teamName) {
        updatePlayerHuntList(teamName);

    }

    private void updatePlayerHuntList(final String teamName){
        final String userID = mAuth.getCurrentUser().getUid();

        db.collection(User.KEY_PLAYERS).document(mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.w(TAG, "Adding hunt to player");
                        final String uniqueID = Utils.uniqueID(8);

                        User user = documentSnapshot.toObject(User.class);
                        Map<String, String> huntValues = new HashMap<>();
                        huntValues.put(Hunt.KEY_HUNT_NAME, hunt.getHuntName());
                        huntValues.put(Team.KEY_TEAM_NAME, teamName);
                        huntValues.put(Team.KEY_TEAM_ID, uniqueID);
                        user.addHunt(hunt.getHuntID(), huntValues);
                        db.collection(User.KEY_PLAYERS).document(userID).set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        updateOrganizerTeams(teamName, uniqueID);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, e.toString());
                                        Toast.makeText(getApplicationContext(), "Could not join hunt. Try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });


    }

    private void updateOrganizerTeams(final String teamName, final String uniqueID){
        String fullName = GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getDisplayName();

        String userID = mAuth.getUid();
        Team team = new Team(uniqueID, teamName, userID, fullName);

        db.collection(Hunt.KEY_HUNTS).document(hunt.getHuntID()).collection(Team.KEY_TEAMS).document(uniqueID).set(team)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        establishPlayerHuntSingleton(hunt.getHuntID(), hunt.getHuntName(), uniqueID, teamName);
                        Intent intent = new Intent(PlayerLandingActivity.this, PlayerHuntLandingActivity.class);
                        intent.putExtra(Hunt.KEY_HUNT_ID, hunt.getHuntID());
                        intent.putExtra(Hunt.KEY_HUNT_NAME, hunt.getHuntName());
                        intent.putExtra(Team.KEY_TEAM_NAME, teamName);
                        intent.putExtra(Team.KEY_TEAM_ID, uniqueID);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                        Toast.makeText(getApplicationContext(), "Error saving information. Try Again", Toast.LENGTH_SHORT).show();
                    }
                });

        db.collection(Hunt.KEY_HUNTS).document(hunt.getHuntID()).collection(Challenge.KEY_CHALLENGES).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Challenge challenge = documentSnapshot.toObject(Challenge.class);
                            db.collection(Hunt.KEY_HUNTS).document(hunt.getHuntID()).collection(Team.KEY_TEAMS)
                                    .document(uniqueID).collection(Challenge.KEY_CHALLENGES).document(challenge.getChallengeID()).set(challenge)
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

    private void establishPlayerHuntSingleton(String huntID, String huntName, String teamID, String teamName){
        playerHuntSingleton = PlayerHuntSingleton.init(getApplicationContext(), huntID, huntName, teamID, teamName);
    }
}
