package com.example.scavengerhuntapp.player;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.scavengerhuntapp.R;
import com.example.scavengerhuntapp.objects.Broadcast;
import com.example.scavengerhuntapp.objects.Hunt;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import static com.example.scavengerhuntapp.shared.App.CHANNEL_ANNOUNCEMENTS;

public class PlayerHuntSingleton extends AppCompatActivity {
    private static PlayerHuntSingleton playerHuntSingleton = null;
    private static String MESSAGE_NEW_ANNOUNCEMENT = "New Announcement";
    private FirebaseFirestore db;

    private NotificationManagerCompat notificationManager;
    private ListenerRegistration registration;
    private Context context;
    private String huntID;
    private String huntName;
    private String teamID;
    private String teamName;
    private Boolean firstRun;

    private String TAG = "PlayerHuntSingleton";

    private PlayerHuntSingleton(Context context, String huntID, String huntName, String teamID, String teamName){
        this.db = FirebaseFirestore.getInstance();
        this.notificationManager = NotificationManagerCompat.from(context);
        this.registration = null;
        this.context = context;
        this.huntID = huntID;
        this.huntName = huntName;
        this.teamID = teamID;
        this.teamName = teamName;
        firstRun = true;
        loadAnnouncements();
    }

    public static synchronized PlayerHuntSingleton init(Context context, String huntID, String huntName, String teamID, String teamName) {
        if (playerHuntSingleton == null){
            playerHuntSingleton = new PlayerHuntSingleton(context, huntID, huntName, teamID, teamName);
        } else{
            playerHuntSingleton.reset(context, huntID, huntName, teamID, teamName);
        }
        return playerHuntSingleton;
    }

    public static synchronized PlayerHuntSingleton getPlayerHuntSingleton(){
        return playerHuntSingleton;
    }

    private void reset(Context context, String huntID, String huntName, String teamID, String teamName){
        this.db = FirebaseFirestore.getInstance();
        this.notificationManager = NotificationManagerCompat.from(context);
        this.context = context;
        this.huntID = huntID;
        this.huntName = huntName;
        this.teamID = teamID;
        this.teamName = teamName;
        this.firstRun = true;
        if (registration != null){
            registration.remove();
            registration =  null;
        }

        loadAnnouncements();
    }

    private void loadAnnouncements() {
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
        Intent activityIntent = new Intent(context, AnnouncementsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, activityIntent, 0);


        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ANNOUNCEMENTS)
                .setSmallIcon(R.drawable.tree)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.logo))
                .setContentTitle(MESSAGE_NEW_ANNOUNCEMENT)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(1, notification);
    }

    public String getHuntID(){
        return huntID;
    }

    public String getHuntName(){
        return huntName;
    }

    public String getTeamID(){
        return teamID;
    }

    public String getTeamName(){
        return teamName;
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


