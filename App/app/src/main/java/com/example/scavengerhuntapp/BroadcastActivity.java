package com.example.scavengerhuntapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Context;
import android.content.Intent;

import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;


import android.view.KeyEvent;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.Toast;

public class BroadcastActivity extends AppCompatActivity {

    Button button;
    private EditText edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        addListenerOnButton();
        addKeyListener();
    }

    public void addListenerOnButton() {

        final Context context = this;

        button = (Button) findViewById(R.id.button1);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);

            }

        });

    }

    public void addKeyListener() {

        // get edittext component
        edittext = (EditText) findViewById(R.id.myText);

        // add a keylistener to monitor the keaybord avitvity...
        edittext.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                // if the users pressed a button and that button was "0"
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_0)) {

                    // display the input text....
                    Toast.makeText(BroadcastActivity.this,edittext.getText(), Toast.LENGTH_LONG).show();
                    return true;

                    // if the users pressed a button and that button was "9"
                } else if ((event.getAction() == KeyEvent.ACTION_DOWN)  && (keyCode == KeyEvent.KEYCODE_9)) {

                    // display message
                    Toast.makeText(BroadcastActivity.this, "Number 9 is pressed!", Toast.LENGTH_LONG).show();
                    return true;
                }

                return false;
            }
        });
    }
}
