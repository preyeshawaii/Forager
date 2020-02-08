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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class OrganizerLandingActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Button createNewHuntBtn;
    private Button signOutButton;

    private TextView noCurrHuntsView;
    private TextView noPrevHuntsView;
    private ListView currentHuntsListView;
    private ListView previousHuntsListView;

    private List<String> currentHuntList;
    private Map<String, String> previousHunts;


    private  String TAG = "OrganizerLandingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_landing);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        currentHuntList = new ArrayList<>();
        previousHunts = new HashMap<>();

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
        previousHunts.clear();

        db.collection(User.KEY_ORGANIZERS).document(mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.w(TAG, mAuth.getCurrentUser().getUid());
                        if (documentSnapshot.exists()){

                            previousHunts = (Map<String, String>)documentSnapshot.get(User.KEY_PREVIOUS_HUNTS);
                            Log.w(TAG, previousHunts.toString());
                            createPreviousListView();

                            String currentHunt = (String)documentSnapshot.get(User.KEY_CURRENT_HUNT);
                            createCurrentListView(currentHunt);
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

    private void createPreviousListView(){

        if (previousHunts.isEmpty()){
            Log.w(TAG, "No hunts found");
            noPrevHuntsView.setVisibility(View.VISIBLE);
        } else{
            Log.w(TAG, previousHunts.size() + " hunt(s) found");
            noPrevHuntsView.setVisibility(View.GONE);
            setUpPreviousHuntsListView();
        }
    }

    private void setUpPreviousHuntsListView(){
        final List<String> previousHuntIDs = new ArrayList<>();
        final List<String> previousHuntNames = new ArrayList<>();

        for (Map.Entry<String, String> entry : previousHunts.entrySet()) {
            previousHuntIDs.add(entry.getKey());
            previousHuntNames.add(entry.getValue());
        }

        ArrayAdapter<String> prevHuntNamesArray = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, previousHuntNames);
        previousHuntsListView.setAdapter(prevHuntNamesArray);

        previousHuntsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String huntName = previousHuntNames.get(position);
                String huntID = previousHuntIDs.get(position);

                Toast.makeText(getApplicationContext(), huntName, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(OrganizerLandingActivity.this, HuntLandingActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                Log.w(TAG, huntID + ": " + huntName);
                startActivity(intent);
            }
        });
    }

    private void createCurrentListView(String currentHunt){
        currentHuntList.clear();

        if (currentHunt == ""){
            noCurrHuntsView.setVisibility(View.VISIBLE);
        } else{
            currentHuntList.add(currentHunt);
            ArrayAdapter<String> prevHuntNamesArray = new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_list_item_1, currentHuntList);
            currentHuntsListView.setAdapter(prevHuntNamesArray);

            currentHuntsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(OrganizerLandingActivity.this, HuntLandingActivity.class);
                    //intent.putExtra("huntID", );
                    startActivity(intent);
                }
            });
        }
    }
}
