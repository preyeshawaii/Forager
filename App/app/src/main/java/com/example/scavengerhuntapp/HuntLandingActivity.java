package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HuntLandingActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextView title;
    private TextView joinCode;



    private ListView teamsListView;
    private Switch viewPointSwitch;
    private Button copyButton;

    private List<Team> teams;
    private CustomAdapter customAdapter;

    private Boolean canViewPoints;

    private String TAG = "HuntLandingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt_landing);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        title = findViewById(R.id.hunt_landing_title);
        joinCode = findViewById(R.id.join_code_text);
        copyButton = findViewById(R.id.copy_button);


        teamsListView = findViewById(R.id.team_list);
        viewPointSwitch = findViewById(R.id.show_points_switch);
        teams = new ArrayList<>();
        customAdapter = new CustomAdapter();

        final String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        final String huntName = getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME);

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(Hunt.KEY_HUNT_ID, joinCode.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.organizer_bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_challenges:
                        Intent intent = new Intent(HuntLandingActivity.this, CurrentChallengesActivity.class);
                        intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                        startActivity(intent);
                        break;
                    case R.id.action_submissions:
                        Intent intent1 = new Intent(HuntLandingActivity.this, SubmissionsActivity.class);
                        intent1.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent1.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                        startActivity(intent1);
                        break;
                    case R.id.action_broadcast:
                        Intent intent2 = new Intent(HuntLandingActivity.this, BroadcastActivity.class);
                        intent2.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent2.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                        startActivity(intent2);
                        break;
                    case R.id.action_rankings:
                        Intent intent3 = new Intent(HuntLandingActivity.this, HuntLandingActivity.class);
                        intent3.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent3.putExtra(Hunt.KEY_HUNT_NAME, huntName);

                        startActivity(intent3);
                        break;
                }

                return false;
            }
        });


        viewPointSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateDB(isChecked);
            }
        });



        title.setText(huntName);
        joinCode.setText(huntID);


    }

    @Override
    protected void onStart(){
        super.onStart();
        loadRankings();
    }



    private void updateDB(final Boolean isChecked){
        final String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        db.collection(Hunt.KEY_HUNTS).document(huntID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Hunt hunt = documentSnapshot.toObject(Hunt.class);
                        hunt.setViewPoints(isChecked);

                        db.collection(Hunt.KEY_HUNTS).document(huntID).set(hunt)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        canViewPoints  = isChecked;

                                        teamsListView.setAdapter(customAdapter);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.toString();
                                    }
                                });
                    }
                });
    }

    private void loadRankings(){
        teams.clear();

        final String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        db.collection(Hunt.KEY_HUNTS).document(huntID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Hunt hunt = documentSnapshot.toObject(Hunt.class);
                        viewPointSwitch.setChecked(hunt.getViewPoints());
                        canViewPoints  = hunt.getViewPoints();

                        rankTeams(huntID);
                    }
                });
    }

    private void rankTeams(final String huntID){
        db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Team.KEY_TEAMS)
                .orderBy(Team.KEY_POINTS, Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Team team = documentSnapshot.toObject(Team.class);
                            teams.add(team);
                        }

                        setAdapter(huntID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }


    public void setAdapter(final String huntID){
        teamsListView.setAdapter(customAdapter);

        teamsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String teamName = teams.get(position).getTeamName();


                Toast.makeText(getApplicationContext(), teamName, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(HuntLandingActivity.this, TeamInfoActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                intent.putExtra(Team.KEY_TEAM_NAME, teamName);
                intent.putExtra(Team.KEY_TEAM_ID, teams.get(position).getTeamID());

               // String playerType = isPlayer ? User.KEY_PLAYER : User.KEY_ORGANIZER;
                intent.putExtra(User.KEY_PLAYER_TYPE, User.KEY_ORGANIZER);

                startActivity(intent);
            }
        });
    }
    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return teams.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.team_list_custom_view, null);
            // initialize all of the different types of views
            TextView teamName = convertView.findViewById(R.id.teamview_name);
            TextView teamPoints = convertView.findViewById(R.id.teamview_points);
            if (canViewPoints == false)   {
                teamPoints.setVisibility(View.GONE);
            }
            // NOTE: in future use getDrawable to connect to our database of images. SET DATABASE objects here

            teamName.setText(teams.get(position).getTeamName());
            teamPoints.setText(teams.get(position).getPoints() + " pts");
            return convertView;
        }
    }
}