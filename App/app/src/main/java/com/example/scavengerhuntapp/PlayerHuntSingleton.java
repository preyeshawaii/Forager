package com.example.scavengerhuntapp;

import android.app.Notification;
import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.scavengerhuntapp.App.CHANNEL_ANNOUNCEMENTS;
import static com.example.scavengerhuntapp.App.CHANNEL_SUBMISSION;

public class PlayerHuntSingleton extends AppCompatActivity {
    private static PlayerHuntSingleton playerHuntSingleton = null;
    private static String MESSAGE_NEW_ANNOUNCEMENT = "New Announcement";
    private FirebaseFirestore db;

    private NotificationManagerCompat notificationManager;
    private ListenerRegistration registration;
    private Context context;
    private String huntID;
    private Boolean firstRun;

    private String TAG = "PlayerHuntSingleton";

    private PlayerHuntSingleton(Context context, String huntID){
        this.db = FirebaseFirestore.getInstance();
        this.notificationManager = NotificationManagerCompat.from(context);
        this.registration = null;
        this.context = context;
        this.huntID = huntID;
        firstRun = true;
        loadAnnouncements();
    }

    public static synchronized PlayerHuntSingleton init(Context context, String huntID) {
        if (playerHuntSingleton == null){
            playerHuntSingleton = new PlayerHuntSingleton(context, huntID);
        } else{
            playerHuntSingleton.reset(context, huntID);
        }
        return playerHuntSingleton;
    }

    private void reset(Context context, String huntID){
        this.db = FirebaseFirestore.getInstance();
        this.notificationManager = NotificationManagerCompat.from(context);
        this.context = context;
        this.huntID = huntID;
        this.firstRun = true;
        if (registration != null){
            registration.remove();
            registration =  null;
        }

        loadAnnouncements();
    }

    private void loadAnnouncements() {
        Log.w(TAG, "PRINT: " + huntID);
        registration = db.collection(Hunt.KEY_HUNTS).document(huntID).collection(Broadcast.KEY_BROADCASTS)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null){
                            Log.e(TAG, e.toString());
                            return;
                        }
                        for (DocumentChange dc: queryDocumentSnapshots.getDocumentChanges()){
                            DocumentSnapshot documentSnapshot = dc.getDocument();
                            Broadcast broadcast = documentSnapshot.toObject(Broadcast.class);
                            String id = broadcast.getBroadcastID();


                            if (!firstRun && dc.getType() == DocumentChange.Type.ADDED){
                                Log.w(TAG, "PRINT: " + broadcast.getMessage());
                                sendOnAnnouncementChannel(broadcast.getMessage());
                            }

                            int oldIndex = dc.getOldIndex();
                            int newIndex = dc.getNewIndex();
                        }

                        firstRun = false;
                    }
                });
    }



    private void sendOnAnnouncementChannel(String message) {
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ANNOUNCEMENTS)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(MESSAGE_NEW_ANNOUNCEMENT)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }

    /**public void sendOnChannel2() {
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_SUBMISSION)
                .setSmallIcon(R.drawable.ic_two)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        notificationManager.notify(2, notification);
    }**/
}


