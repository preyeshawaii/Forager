package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrganizerLandingActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Button createNewHuntBtn;
    private Button signOutButton;

    private TextView noCurrHuntsView;
    private TextView noPrevHuntsView;
    private ListView currentHuntsListView;
    private ListView previousHuntsListView;

    private List<String> previousHuntIDs;
    private List<String> previousHuntNames;


    private  String TAG = "OrganizerLandingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_landing);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        createNewHuntBtn = findViewById(R.id.create_new_hunt_btn);
        signOutButton = findViewById(R.id.sign_out);
        previousHuntsListView = findViewById(R.id.previous_hunts_listView);
        noPrevHuntsView = findViewById(R.id.no_prev_hunts_view);
        noCurrHuntsView = findViewById(R.id.no_curr_hunts_view);
        currentHuntsListView = findViewById(R.id.curr_hunts_listView);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(OrganizerLandingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        createNewHuntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerLandingActivity.this, CreateHuntActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        loadInfo();
    }

    private void loadInfo(){
        previousHuntNames = new ArrayList<>();

        db.collection(User.KEY_ORGANIZERS).document(mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.w(TAG, mAuth.getCurrentUser().getUid());
                        if (documentSnapshot.exists()){

                            previousHuntIDs = (List<String>)documentSnapshot.get(User.KEY_PREVIOUS_HUNT_IDS);
                            Log.w(TAG, previousHuntIDs.toString());
                            CreatePreviousListView();

                            String currentHunt = (String)documentSnapshot.get(User.KEY_CURRENT_HUNT);
                            CreateCurrentListView(currentHunt);
                        } else{
                            Log.w(TAG, "Organizer does not exist");
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

    private void CreatePreviousListView(){
        db.collection(Hunt.KEY_HUNTS).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    if (previousHuntIDs.contains(documentSnapshot.getId())){
                        Log.w(TAG, documentSnapshot.getId());


                        previousHuntNames.add((String)documentSnapshot.get(Hunt.KEY_HUNT_NAME));
                    }
                }

                if (previousHuntNames.size() == 0){
                    Log.w(TAG, "No hunts found");
                    noPrevHuntsView.setVisibility(View.VISIBLE);
                } else{
                    Log.w(TAG, previousHuntNames.size() + " hunt(s) found");
                    ArrayAdapter<String> prevHuntNamesArray = new ArrayAdapter<String >(getApplicationContext(),
                            android.R.layout.simple_list_item_1, previousHuntNames);
                    previousHuntsListView.setAdapter(prevHuntNamesArray);
                }
            }
        });



    }

    private void CreateCurrentListView(String currentHunt){
        List<String> currentHuntList = new ArrayList<String>(){};

        if (currentHunt == ""){
            noCurrHuntsView.setVisibility(View.VISIBLE);
        } else{
            currentHuntList.add(currentHunt);
            ArrayAdapter<String> prevHuntNamesArray = new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_list_item_1, currentHuntList);
            currentHuntsListView.setAdapter(prevHuntNamesArray);
        }
    }


}
