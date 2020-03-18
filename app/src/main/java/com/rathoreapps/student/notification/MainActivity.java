package com.rathoreapps.student.notification;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText titleEditText, desEditText;
    Button channel1, channel2, channel3;
    NotificationManagerCompat manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleEditText = findViewById(R.id.title_edittext);
        desEditText = findViewById(R.id.description_edittext);
        manager = NotificationManagerCompat.from(getApplicationContext());
        channel1 = findViewById(R.id.channel_1);
        channel1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                setNotification(titleEditText.getText().toString(), desEditText.getText().toString(), App.CHANNEL_1,0);
            }
        });
        channel2 = findViewById(R.id.channel_2);
        channel2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                setNotification(titleEditText.getText().toString(), desEditText.getText().toString(), App.CHANNEL_2,1);
            }
        });
        channel3 = findViewById(R.id.channel_3);
        channel3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                setNotification(titleEditText.getText().toString(), desEditText.getText().toString(), App.CHANNEL_3,2);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setNotification(String title, String description, String channel, int id){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT );
        Notification notification = new Notification.Builder(this, channel)
                .setContentTitle(title)
                .setContentText(description+" "+ channel +" "+ id)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        manager.notify(id, notification);

    }
}
