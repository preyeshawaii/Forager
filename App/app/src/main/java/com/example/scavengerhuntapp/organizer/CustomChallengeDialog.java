package com.example.scavengerhuntapp.organizer;

import android.app.Activity;
import android.content.Intent;
import android.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.widget.AdapterView.OnItemSelectedListener;
import android.util.Log;
import android.widget.ImageView;



import com.google.firebase.auth.FirebaseAuth;

import static android.R.layout.simple_spinner_item;

import com.example.scavengerhuntapp.R;

public class CustomChallengeDialog extends AppCompatDialogFragment {
    private EditText challengeEditText;
    private EditText challengeLocationEditText;
    private EditText pointsEditText;
    private CustomChallengeListener listener;
    private Spinner spinner;
    public int icon;


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_custom_challenge_view, null);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        List<String> list1 = new ArrayList<String>();
        list1.add("train");//1
        list1.add("abc blocks");//2
        list1.add("lion");//3
        list1.add("holding hands");//4
        list1.add("octopus");//5
        list1.add("podium");//6
        list1.add("henna");//7
        list1.add("ice cream");//8
        list1.add("megaphone");//9
        list1.add("dancing");//10
        list1.add("dog");//11
        list1.add("happy");//12
        list1.add("playground");//13
        list1.add("peace");//14
        list1.add("cha cha");//15
        list1.add("dancing girls");//16
        list1.add("mannequin");//17
        list1.add("abc blocks 2");//18
        list1.add("megaphone 2");//19
        list1.add("tattoo parlor");//20
        list1.add("priest");//21
        list1.add("musician");//22
        list1.add("taxi driver");//23
        list1.add("music note");//24
        list1.add("tv");
        list1.add("president");
        list1.add("fish");
        list1.add("piggy bank");
        list1.add("tie");
        list1.add("makeover");
        list1.add("flower");
        list1.add("bus");
        list1.add("crawl");
        list1.add("car");
        list1.add("axe");
        list1.add("guitarist");
        list1.add("piano");
        list1.add("shoe");
        list1.add("dove");
        list1.add("rings");



        final ImageView image = view.findViewById(R.id.iconImageView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (view.getContext(), android.R.layout.simple_spinner_item, list1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        image.setImageResource(R.drawable.a1);
                        icon = R.drawable.a1;
                        break;
                    case 1:
                        image.setImageResource(R.drawable.a2);
                        icon = R.drawable.a2;
                        break;
                    case 2:
                        image.setImageResource(R.drawable.a3);
                        icon = R.drawable.a3;
                        break;
                    case 3:
                        image.setImageResource(R.drawable.a4);
                        icon = R.drawable.a4;
                        break;
                    case 4:
                        image.setImageResource(R.drawable.a5);
                        icon = R.drawable.a5;
                        break;
                    case 5:
                        image.setImageResource(R.drawable.a6);
                        icon = R.drawable.a6;
                        break;
                    case 6:
                        image.setImageResource(R.drawable.a7);
                        icon = R.drawable.a7;
                        break;
                    case 7:
                        image.setImageResource(R.drawable.a8);
                        icon = R.drawable.a8;
                        break;
                    case 8:
                        image.setImageResource(R.drawable.a9);
                        icon = R.drawable.a9;
                        break;
                    case 9:
                        image.setImageResource(R.drawable.a10);
                        icon = R.drawable.a10;
                        break;
                    case 10:
                        image.setImageResource(R.drawable.a11);
                        icon = R.drawable.a11;
                        break;
                    case 11:
                        image.setImageResource(R.drawable.a12);
                        icon = R.drawable.a12;
                        break;
                    case 12:
                        image.setImageResource(R.drawable.a13);
                        icon = R.drawable.a13;
                        break;
                    case 13:
                        image.setImageResource(R.drawable.a14);
                        icon = R.drawable.a14;
                        break;
                    case 14:
                        image.setImageResource(R.drawable.a15);
                        icon = R.drawable.a15;
                        break;
                    case 15:
                        image.setImageResource(R.drawable.a16);
                        icon = R.drawable.a16;
                        break;
                    case 16:
                        image.setImageResource(R.drawable.a17);
                        icon = R.drawable.a17;
                        break;
                    case 17:
                        image.setImageResource(R.drawable.a18);
                        icon = R.drawable.a18;
                        break;
                    case 18:
                        image.setImageResource(R.drawable.a19);
                        icon = R.drawable.a19;
                        break;
                    case 19:
                        image.setImageResource(R.drawable.a20);
                        icon = R.drawable.a20;
                        break;
                    case 20:
                        image.setImageResource(R.drawable.a21);
                        icon = R.drawable.a21;
                        break;
                    case 21:
                        image.setImageResource(R.drawable.a22);
                        icon = R.drawable.a2;
                        break;
                    case 22:
                        image.setImageResource(R.drawable.a23);
                        icon = R.drawable.a23;
                        break;
                    case 23:
                        image.setImageResource(R.drawable.a24);
                        icon = R.drawable.a24;
                        break;
                    case 24:
                        image.setImageResource(R.drawable.a25);
                        icon = R.drawable.a25;
                        break;
                    case 25:
                        image.setImageResource(R.drawable.a26);
                        icon = R.drawable.a26;
                        break;
                    case 26:
                        image.setImageResource(R.drawable.a27);
                        icon = R.drawable.a27;
                        break;
                    case 27:
                        image.setImageResource(R.drawable.a28);
                        icon = R.drawable.a28;
                        break;
                    case 28:
                        image.setImageResource(R.drawable.a29);
                        icon = R.drawable.a29;
                        break;
                    case 29:
                        image.setImageResource(R.drawable.a30);
                        icon = R.drawable.a30;
                        break;
                    case 30:
                        image.setImageResource(R.drawable.a31);
                        icon = R.drawable.a31;
                        break;
                    case 31:
                        image.setImageResource(R.drawable.a32);
                        icon = R.drawable.a32;
                        break;
                    case 32:
                        image.setImageResource(R.drawable.a33);
                        icon = R.drawable.a33;
                        break;
                    case 33:
                        image.setImageResource(R.drawable.a34);
                        icon = R.drawable.a34;
                        break;
                    case 34:
                        image.setImageResource(R.drawable.a35);
                        icon = R.drawable.a35;
                        break;
                    case 35:
                        image.setImageResource(R.drawable.a36);
                        icon = R.drawable.a36;
                        break;
                    case 36:
                        image.setImageResource(R.drawable.a37);
                        icon = R.drawable.a37;
                        break;
                    case 37:
                        image.setImageResource(R.drawable.a38);
                        icon = R.drawable.a38;
                        break;
                    case 38:
                        image.setImageResource(R.drawable.a39);
                        icon = R.drawable.a39;
                        break;
                    case 39:
                        image.setImageResource(R.drawable.a40);
                        icon = R.drawable.a40;
                        break;

                    default:
                        //Default image
                        image.setImageResource(R.drawable.icecream);
                        icon = R.drawable.icecream;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        builder.setView(view)
                .setTitle("Add custom challenge")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }

                })
                .setPositiveButton("Add to hunt", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String checkPoints = pointsEditText.getText().toString();
                        if (checkPoints.matches("")){
                            checkPoints = "10";
                        }


                        String challenge = challengeEditText.getText().toString();
                        String location = challengeLocationEditText.getText().toString();
                        Integer pointsInt = Integer.parseInt(checkPoints);
                        listener.getTexts(challenge, location, pointsInt, icon);
                    }
                });
        challengeEditText = view.findViewById(R.id.challengeEditTextView);
        challengeLocationEditText = view.findViewById(R.id.challengeLocationEditTextView);
        pointsEditText = view.findViewById(R.id.pointsEditText);

        return builder.create();
    }





    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (CustomChallengeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Implement CustomChallengeListener");
        }

    }

    public interface CustomChallengeListener{
        void getTexts(String challenge, String location, Integer points, int icon);
    }


}
