package com.example.remotehomeelectricalcontrolsystem;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.remotehomeelectricalcontrolsystem.Model.Room;
import com.example.remotehomeelectricalcontrolsystem.Model.SharedUserHouse;
import com.example.remotehomeelectricalcontrolsystem.Model.UserHouse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class BackgroundService extends Service {
  UserHouse userHouse = SharedUserHouse.getUserHouse();

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d("aaa", "restart");
    if (getKey("houseId") != null) {
      String housePath = "test1/" + getKey("houseId");
      listenChange(housePath);
    }
    if (userHouse != null) {
      String houseId = userHouse.getHouseId();
      saveHouse("houseId", houseId);
      String housePath = "test1/" + houseId;
      listenChange(housePath);
    }

    // Return START_STICKY to restart the service if it gets terminated
    return START_STICKY;
  }

  private void saveHouse(String key, String houseId) {
    SharedPreferences sharedPreferences = getSharedPreferences("house", MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(key, houseId);
    editor.apply();
  }

  private String getKey(String key) {
    SharedPreferences sharedPreferences = getSharedPreferences("house", MODE_PRIVATE);
    return sharedPreferences.getString(key, "");
  }

  private void listenChange(String housePath) {
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference houseRef = db.getReference(housePath);
    houseRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot house) {
        for (DataSnapshot floor : house.child("floors").getChildren()) {
          String floorId = floor.getKey();
          String floorName = floor.child("name").getValue(String.class);
          for (DataSnapshot room : floor.child("rooms").getChildren()) {
            String roomId = room.getKey();
            String roomName = room.child("name").getValue(String.class);
            for (DataSnapshot dht : room.child("DHT").getChildren()) {
              Float temp = dht.child("temperature").getValue(Float.class);
              Log.d("aaa", "temp: " + temp);
              if (temp > 50) {
                saveHouse("floorId", floorId);
                saveHouse("roomId", roomId);
                sendNotification(floorName, roomName, temp);
              }
            }
            for (DataSnapshot device : room.child("devices").getChildren()) {
            }
            for (DataSnapshot otherSensor : room.child("otherSensors").getChildren()) {
              String name = otherSensor.child("name").getValue(String.class);
              int state = otherSensor.child("state").getValue(Integer.class);
              if (state > 500) {
                saveHouse("floorId", floorId);
                saveHouse("roomId", roomId);
                switch (name) {
                  case "Gas sensor":
                    sendNotification(floorName, roomName, "Possible gas leak detected at your home.");
                    break;
                  case "Rain sensor":
                    sendNotification(floorName, roomName, "Rain detected at your home.");
                    break;
                }
              }
            }
          }
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }

  public void sendNotification(String floorName, String roomName, Float value) {
    Intent resultIntent = new Intent(this, RoomActivity.class);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    stackBuilder.addNextIntentWithParentStack(resultIntent);
    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(getNotificationId(),
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    NotificationCompat.Builder builder = new NotificationCompat.Builder(BackgroundService.this, MyApplication.CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notify)
        .setContentTitle("Warning (" + floorName + " - " + roomName + ")")
        .setContentText("Current temperature is: " + value)
        .setAutoCancel(true)
        .setContentIntent(resultPendingIntent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(BackgroundService.this);
    if (ActivityCompat.checkSelfPermission(BackgroundService.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    notificationManager.notify(getNotificationId(), builder.build());
  }

  public void sendNotification(String floorName, String roomName, String contentState) {
    Intent resultIntent = new Intent(this, RoomActivity.class);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    stackBuilder.addNextIntentWithParentStack(resultIntent);
    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(getNotificationId(),
        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    NotificationCompat.Builder builder = new NotificationCompat.Builder(BackgroundService.this, MyApplication.CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notify)
        .setContentTitle("Warning (" + floorName + " - " + roomName + ")")
        .setContentText(contentState)
        .setAutoCancel(true)
        .setContentIntent(resultPendingIntent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(BackgroundService.this);
    if (ActivityCompat.checkSelfPermission(BackgroundService.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    notificationManager.notify(getNotificationId(), builder.build());
  }

  private int getNotificationId() {
    return (int) new Date().getTime();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
