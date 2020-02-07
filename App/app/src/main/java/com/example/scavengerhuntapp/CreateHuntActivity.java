package com.example.scavengerhuntapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateHuntActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Button createHuntButton;
    private EditText huntNameEditText;

    private  String TAG = "CreateHuntActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hunt);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        huntNameEditText = findViewById(R.id.hunt_name_et);
        createHuntButton = findViewById(R.id.create_hunt_button);

        createHuntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (huntNameEditText.getText().toString().length() == 0){
                    Toast.makeText(CreateHuntActivity.this, "The hunt needs a name!", Toast.LENGTH_SHORT).show();
                }else{
                    CreateHunt();
                }
            }
        });
    }

    private void CreateHunt() {
        Hunt hunt = new Hunt(huntNameEditText.getText().toString());

        db.collection(Hunt.KEY_HUNTS).add(hunt)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String huntID = documentReference.getId();
                        UpdateUserHuntList(huntID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    private void UpdateUserHuntList(final String huntID){
        final String userID = mAuth.getCurrentUser().getUid();

        db.collection(User.KEY_ORGANIZERS).document(userID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            Log.w(TAG, "Create hunt");
                            User user = documentSnapshot.toObject(User.class);
                            user.addHunt(huntID);
                            db.collection(User.KEY_ORGANIZERS).document(userID).set(user);

                            Intent intent = new Intent(CreateHuntActivity.this, HuntLandingActivity.class);
                            startActivity(intent);

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
}
