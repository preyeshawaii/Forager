package com.example.scavengerhuntapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AnnouncementsActivity extends AppCompatActivity {

    private ListView announcementsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        announcementsListView = findViewById(R.id.announcements_list);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


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
                        intent.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
                        intent.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));
                        intent.putExtra(Team.KEY_TEAM_NAME, getIntent().getExtras().getString(Team.KEY_TEAM_NAME));
                        intent.putExtra(Team.KEY_TEAM_ID, getIntent().getExtras().getString(Team.KEY_TEAM_ID));
                        startActivity(intent);
                        break;
                    case R.id.action_team:
                        Intent newIntent = new Intent (AnnouncementsActivity.this, playerViewTeamActivity.class);
                        startActivity(newIntent);
                        break;
                    case R.id.action_rankings:
                        Intent intent3 = new Intent(AnnouncementsActivity.this, RankingsActivity.class);
                        intent3.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
                        intent3.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));
                        intent3.putExtra(Team.KEY_TEAM_NAME, getIntent().getExtras().getString(Team.KEY_TEAM_NAME));
                        intent3.putExtra(Team.KEY_TEAM_ID, getIntent().getExtras().getString(Team.KEY_TEAM_ID));
                        intent3.putExtra(User.KEY_PLAYER_TYPE, User.KEY_PLAYER);
                        startActivity(intent3);
                        break;
                }

                return false;
            }
        });


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
