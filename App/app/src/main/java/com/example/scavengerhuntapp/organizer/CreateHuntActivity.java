package com.example.scavengerhuntapp.organizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scavengerhuntapp.R;
import com.example.scavengerhuntapp.shared.Utils;
import com.example.scavengerhuntapp.objects.Challenge;
import com.example.scavengerhuntapp.objects.Hunt;
import com.example.scavengerhuntapp.objects.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateHuntActivity extends AppCompatActivity implements CustomChallengeDialog.CustomChallengeListener {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private EditText huntNameEditText;
    private ListView challengeList;
    private Button deleteChallenge;
    private Button premadeChallButton;
    private Button createHuntButton;
    private Button customChallButton;

    private CreatingHuntSingleton creatingHuntSingleton;
    private CustomAdapter pendingChallenges;
    private int iconInt;

    private  String TAG = "CreateHuntActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hunt);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        huntNameEditText = findViewById(R.id.hunt_name_et);
        challengeList = findViewById(R.id.challenge_list);
        deleteChallenge = findViewById(R.id.delete_challenges);
        premadeChallButton = findViewById(R.id.premade_chall_button);
        createHuntButton = findViewById(R.id.create_hunt_button);
        customChallButton = findViewById(R.id.custom_chall_button);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        creatingHuntSingleton = creatingHuntSingleton.init();
        pendingChallenges = new CustomAdapter();

        challengeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openEditDialog(position);
            }
        });

        deleteChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteChallenges();
            }
        });

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

    private void deleteChallenges(){
        List<Challenge> challenges = creatingHuntSingleton.getChallenges();
        List<Integer> challengesToDelete = new ArrayList<>();
        if (challenges.isEmpty()){
            Toast.makeText(CreateHuntActivity.this, "There are no challenges present!", Toast.LENGTH_SHORT).show();
        } else{
            for (int i = 0; i < challengeList.getCount(); i++) {
                CheckBox checkedBox = challengeList.getChildAt(i).findViewById(R.id.checkBox_edit);

                if (checkedBox.isChecked() == true){
                    challengesToDelete.add(i);
                }
            }

            if (challengesToDelete.isEmpty()){
                Toast.makeText(CreateHuntActivity.this, "There are no challenges to delete!", Toast.LENGTH_SHORT).show();
            } else{
                for (int i = 0; i < challengesToDelete.size(); i++){
                    challenges.remove(challengesToDelete.get(i) - i);
                }
                creatingHuntSingleton.updateList(challenges);

                pendingChallenges = new CustomAdapter();
                challengeList.setAdapter(pendingChallenges);
            }
        }
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

    public void openEditDialog(final int position) {
        LayoutInflater inflater = LayoutInflater.from(CreateHuntActivity.this);
        final View view = inflater.inflate(R.layout.dialog_custom_challenge_view, null);

        final EditText challenge = view.findViewById(R.id.challengeEditTextView);
        final EditText location = view.findViewById(R.id.challengeLocationEditTextView);
        final EditText points = view.findViewById(R.id.pointsEditText);
        final ImageView icon = view.findViewById(R.id.iconImageView);
        Spinner spinner = view.findViewById(R.id.spinner);

        challenge.setHint("Challenge Description");
        location.setHint("Location");

        challenge.setText(creatingHuntSingleton.getChallenges().get(position).getDescription());
        location.setText(creatingHuntSingleton.getChallenges().get(position).getLocation());
        points.setText(String.valueOf(creatingHuntSingleton.getChallenges().get(position).getPoints()));
        icon.setImageResource(creatingHuntSingleton.getChallenges().get(position).getIcon());

        List<String> iconNames = creatingHuntSingleton.getIconNameList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (view.getContext(), android.R.layout.simple_spinner_item, iconNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iconInt = creatingHuntSingleton.getSpinnerIcon(position);
                icon.setImageResource(iconInt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AlertDialog dialog = new AlertDialog.Builder(CreateHuntActivity.this)
                .setTitle("Edit Challenge")
                .setView(view)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        creatingHuntSingleton.getChallenges().get(position).setDescription(challenge.getText().toString());
                        creatingHuntSingleton.getChallenges().get(position).setLocation(location.getText().toString());
                        creatingHuntSingleton.getChallenges().get(position).setIcon(iconInt);

                        String checkPoints = points.getText().toString();
                        if (checkPoints.matches("")){
                            checkPoints = "0";
                        }

                        creatingHuntSingleton.getChallenges().get(position).setPoints(Integer.parseInt(checkPoints));
                    }
                })
                .setNegativeButton("Cancel", null).create();
        dialog.show();
    }


    public void getTexts(String challengeDes, String location, Integer points, int icon) {
        final String uniqueID = Utils.generateHuntID();
        Challenge challenge = new Challenge(uniqueID, challengeDes, location, points, icon);
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.edit_create_hunts_custom_view, null);

            // Populate list view
            ImageView imageView = view.findViewById(R.id.iconImageView_edit);
            final TextView challenge = view.findViewById(R.id.challengeTextView_edit);
            final TextView location = view.findViewById(R.id.challengeLocationTextView_edit);
            final TextView points = view.findViewById(R.id.challengePoints_edit);

            imageView.setImageResource(challenges.get(i).getIcon());
            challenge.setText(challenges.get(i).getDescription());
            location.setText(challenges.get(i).getLocation());
            points.setText(String.valueOf(challenges.get(i).getPoints()));

            return view;
        }
    }
}
