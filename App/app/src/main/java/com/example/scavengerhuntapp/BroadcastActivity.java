package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;


import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class BroadcastActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    private Button submitBtn;
    private EditText message;

    private String TAG = "BroadcastActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        submitBtn = findViewById(R.id.submit_button);
        message = findViewById(R.id.broadcast_message);



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(message.getText())){
                    Toast.makeText(BroadcastActivity.this, "Message Required!", Toast.LENGTH_SHORT).show();
                }else{
                    sendBroadcastToPlayers();
                }
            }
        });

        final String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);
        final String huntName = getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME);
        BottomNavigationView bottomNavigationView = findViewById(R.id.organizer_bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_challenges:
                        Intent intent = new Intent(BroadcastActivity.this, CurrentChallengesActivity.class);
                        intent.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                        startActivity(intent);
                        break;
                    case R.id.action_submissions:
                        Intent intent1 = new Intent(BroadcastActivity.this, SubmissionsActivity.class);
                        intent1.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent1.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                        startActivity(intent1);
                        break;
                    case R.id.action_broadcast:
                        Intent intent2 = new Intent(BroadcastActivity.this, BroadcastActivity.class);
                        intent2.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent2.putExtra(Hunt.KEY_HUNT_NAME, huntName);
                        startActivity(intent2);
                        break;
                    case R.id.action_rankings:
                        Intent intent3 = new Intent(BroadcastActivity.this, HuntLandingActivity.class);

                        intent3.putExtra(Hunt.KEY_HUNT_ID, huntID);
                        intent3.putExtra(Hunt.KEY_HUNT_NAME, huntName);

                        startActivity(intent3);


                        break;
                }

                return false;
            }
        });
    }

    private void sendBroadcastToPlayers(){
        String uniqueID = UUID.randomUUID().toString();
        String broadcastMsg = message.getText().toString();
        Broadcast broadcast = new Broadcast(uniqueID, broadcastMsg);
        String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);

        db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Broadcast.KEY_BROADCASTS).document(uniqueID).set(broadcast)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                        Toast.makeText(BroadcastActivity.this, "Error sending message!", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(BroadcastActivity.this, OrganizerLandingActivity.class);
                startActivity(intent);
                break;
            case R.id.action_sign_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(BroadcastActivity.this, MainActivity.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
