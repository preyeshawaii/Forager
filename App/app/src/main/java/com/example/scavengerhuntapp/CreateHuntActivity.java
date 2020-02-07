package com.example.scavengerhuntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateHuntActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hunt);
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        final Context context = this;
        Button premadeChallButton = (Button) findViewById(R.id.premade_chall_button);
        premadeChallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PremadeHuntsActivity.class);
                startActivity(intent);
            }
        });
    }

}
