package com.example.scavengerhuntapp.player;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.scavengerhuntapp.shared.MainActivity;
import com.example.scavengerhuntapp.R;
import com.example.scavengerhuntapp.objects.Broadcast;
import com.example.scavengerhuntapp.objects.Hunt;
import com.example.scavengerhuntapp.objects.Team;
import com.example.scavengerhuntapp.objects.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementsActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    private ListView announcementsListView;
    private SwipeRefreshLayout swipeContainer;

    private PlayerHuntSingleton playerHuntSingleton;
    private String huntID;
    private String huntName;
    private String teamID;
    private String teamName;
    private List<String> broadcasts;
    private ArrayAdapter<String> adapter;

    private String TAG = "AnnouncementsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        db = FirebaseFirestore.getInstance();

        announcementsListView = findViewById(R.id.announcements_list);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        swipeContainer = findViewById(R.id.swipe_container_announcements);

        playerHuntSingleton = PlayerHuntSingleton.getPlayerHuntSingleton();
        huntID = playerHuntSingleton.getHuntID();
        huntName = playerHuntSingleton.getHuntName();
        teamID = playerHuntSingleton.getTeamID();
        teamName = playerHuntSingleton.getTeamName();

        broadcasts = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.hunt_list_custom_view, R.id.hunt_name_content, broadcasts);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAnnouncements();
                swipeContainer.setRefreshing(false);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_red_dark);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_alerts:
                        break;
                    case R.id.action_challenges:
                        Intent intent = new Intent(AnnouncementsActivity.this, PlayerHuntLandingActivity.class);
                        intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                        intent.putExtra(Team.KEY_TEAM_NAME, teamName);
                        intent.putExtra(Team.KEY_TEAM_ID, teamID);
                        startActivity(intent);
                        break;
                    case R.id.action_team:
                        Intent intent1 = new Intent (AnnouncementsActivity.this, PlayerViewTeamActivity.class);
                        intent1.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent1.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                        intent1.putExtra(Team.KEY_TEAM_NAME, teamName);
                        intent1.putExtra(Team.KEY_TEAM_ID, teamID);
                        intent1.putExtra(User.KEY_PLAYER_TYPE, User.KEY_PLAYER);
                        startActivity(intent1);
                        break;
                    case R.id.action_rankings:
                        Intent intent3 = new Intent(AnnouncementsActivity.this, RankingsActivity.class);
                        intent3.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent3.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                        intent3.putExtra(Team.KEY_TEAM_NAME, teamName);
                        intent3.putExtra(Team.KEY_TEAM_ID, teamID);
                        intent3.putExtra(User.KEY_PLAYER_TYPE, User.KEY_PLAYER);
                        startActivity(intent3);
                        break;
                }

                return false;
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        loadAnnouncements();

    }

    private void loadAnnouncements(){
        broadcasts.clear();

        final String huntID = this.huntID;
        db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Broadcast.KEY_BROADCASTS).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            broadcasts.add(documentSnapshot.toObject(Broadcast.class).getMessage());
                        }

                    }
                });

        announcementsListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_all_hunts:
                Intent intent = new Intent(AnnouncementsActivity.this, PlayerLandingActivity.class);
                startActivity(intent);
                break;
            case R.id.action_sign_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(AnnouncementsActivity.this, MainActivity.class);
                startActivity(intent2);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
