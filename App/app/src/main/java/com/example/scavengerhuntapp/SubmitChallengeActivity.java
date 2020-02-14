package com.example.scavengerhuntapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

// later: ability to take photo directly in the application.



public class SubmitChallengeActivity extends AppCompatActivity {
    public static final int GET_FROM_GALLERY = 3;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_challenge_player);
        Button submitButton = findViewById(R.id.submitButtonChallenge);
        submitButton.setVisibility(View.GONE);

    }

    public void uploadPhotoClicked(View v){
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    // can send the request with the corresponding values.
    public void submitChallengeClicked(View v){
        String messageText = ((EditText)findViewById(R.id.submitChallengeText)).getText().toString();

    }
}
