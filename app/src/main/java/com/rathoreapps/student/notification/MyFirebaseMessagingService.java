package com.rathoreapps.student.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.protobuf.LazyStringArrayList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "NEW_TOKEN" + s);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: ");
        sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("content"), App.CHANNEL_1, 0);
        Calendar calendar = Calendar.getInstance();
        Date reciveTime = calendar.getTime();
        long longReceiveTime = reciveTime.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);
        //String timeDiff = formatter.format(findDiffrence(remoteMessage.getSentTime(), longReceiveTime));
        String sentTime = formatter.format(new Date(remoteMessage.getSentTime()));
        String recivetime = formatter.format(reciveTime);
        sendUserData(sentTime, recivetime, findDiffrence(remoteMessage.getSentTime(), longReceiveTime));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification(String title, String description, String channel, int id) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Notification notification = new Notification.Builder(this, channel)
                .setContentTitle(title)
                .setContentText(description + " " + channel + " " + id)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        manager.notify(id, notification);

    }

    private void sendUserData(String sentTime, String receiveTime, String timeDifference){
        Map<String, String> userData = new HashMap<>();
        userData.put("sent time", sentTime);
        userData.put("receive time", receiveTime);
        userData.put("time Difference", timeDifference);
        db.collection("users")
                .add(userData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MyFirebaseMessagingService.this, "DataSent", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onSuccess: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ");
                    }
                });
    }

    private String findDiffrence(long startTime, long endTime){
        Log.d(TAG, "findDiffrence: "+(endTime-startTime));
        long different = endTime-startTime;

        return String.valueOf(different+" ms");
    }
}
