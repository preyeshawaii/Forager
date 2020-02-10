package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.view.View;


import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class BroadcastActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Button backButton;
    private Button submitBtn;
    private EditText message;

    private String TAG = "BroadcastActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        backButton = findViewById(R.id.broadcast_back_btn);
        submitBtn = findViewById(R.id.submit_button);
        message = findViewById(R.id.broadcast_message);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BroadcastActivity.this, HuntLandingActivity.class);
                intent.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
                intent.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));
                startActivity(intent);
            }
        });

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
    }

    private void sendBroadcastToPlayers(){
        String uniqueID = UUID.randomUUID().toString();
        String broadcastMsg = message.getText().toString();
        Broadcast broadcast = new Broadcast(uniqueID, broadcastMsg);
        String huntID = getIntent().getExtras().getString(Hunt.KEY_HUNT_ID);

        db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Broadcast.KEY_BROADCASTS).document(uniqueID).set(broadcast)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(BroadcastActivity.this, HuntLandingActivity.class);
                        intent.putExtra(Hunt.KEY_HUNT_ID, getIntent().getExtras().getString(Hunt.KEY_HUNT_ID));
                        intent.putExtra(Hunt.KEY_HUNT_NAME, getIntent().getExtras().getString(Hunt.KEY_HUNT_NAME));
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                        Toast.makeText(BroadcastActivity.this, "Error sending message!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
