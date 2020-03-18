package com.rathoreapps.student.notification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class App extends Application {

    public static final String CHANNEL_1 = "Channel 1";
    public static final String CHANNEL_2 = "Channel 2";
    public static final String CHANNEL_3 = "Channel 3";
    public static final String TAG = "ApplicationClass";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        createNotificationChannel();
        firebaseInstanceId();
        FirebaseAnalytics.getInstance(this);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1, "Channel One", NotificationManager.IMPORTANCE_DEFAULT);
            channel1.setDescription("I'm Channel 1 Description Default ");
            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2, "Channel Two", NotificationManager.IMPORTANCE_HIGH);
            channel2.setDescription("I'm Channel 2 Description High");
            NotificationChannel channel3 = new NotificationChannel(CHANNEL_3, "Channel Three", NotificationManager.IMPORTANCE_LOW);
            channel3.setDescription("I'm Channel 3 Description Low");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
            manager.createNotificationChannel(channel3);
        }
    }

    private void firebaseInstanceId() {
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        Log.d(TAG, token);
                    }
                });
    }
}
