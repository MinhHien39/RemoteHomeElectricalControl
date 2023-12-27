package com.example.remotehomeelectricalcontrolsystem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remotehomeelectricalcontrolsystem.Adapter.RoomAdapter;
import com.example.remotehomeelectricalcontrolsystem.Model.Devices;
import com.example.remotehomeelectricalcontrolsystem.Utils.NetworkChangeListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity {
  List<Devices> deviceList;
  RoomAdapter adapter;
  RecyclerView rec_device;
  MaterialToolbar topAppBar;
  TextView txtHumid, txtTemp, txtGas;
  MaterialCardView cardViewDHT, cardViewGas;
  LinearLayout linearLayout;
  NetworkChangeListener networkChangeListener = new NetworkChangeListener();
  String roomPath;
  FirebaseDatabase db;
  DatabaseReference roomRef;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_room);

    init();

    Intent intent = getIntent();
    if (intent != null) {
      roomPath = intent.getStringExtra("roomPath");
      if (roomPath == null) {
        SharedPreferences sharedPreferences = getSharedPreferences("house", MODE_PRIVATE);
        String houseId = sharedPreferences.getString("houseId", "");
        String floorId = sharedPreferences.getString("floorId", "");
        String roomId = sharedPreferences.getString("roomId", "");
        roomPath = "test1/" + houseId + "/floors/" + floorId + "/rooms/" + roomId;
      }
      Log.d("aaa", "roomPath" + roomPath);
      getDevices(roomPath);
    }
    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(view -> {
      Intent intent1 = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
      intent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
      intent1.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN");
      intent1.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!");
      // starting intent for result
      activityResultLauncher.launch(intent1);
    });
    topAppBar.setNavigationOnClickListener(view -> finish());
  }

  public void init() {
    rec_device = findViewById(R.id.rec_room);
    deviceList = new ArrayList<>();
    topAppBar = findViewById(R.id.topAppBar);
    txtHumid = findViewById(R.id.txtHumid);
    txtTemp = findViewById(R.id.txtTemp);
    txtGas = findViewById(R.id.txtGas);
    linearLayout = findViewById(R.id.linearLayout);
    cardViewDHT = findViewById(R.id.cardViewDHT);
    cardViewGas = findViewById(R.id.cardViewGas);
  }

  ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
      ArrayList<String> d = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//      Toast.makeText(RoomActivity.this, d.get(0), Toast.LENGTH_SHORT).show();
      controlBySpeech(d.get(0));
    }
  });

  private void controlBySpeech(String text) {
    if (text.toLowerCase().contains("bật tất cả đèn") || text.toLowerCase().contains("mở tất cả đèn")) {
      Toast.makeText(this, text.toLowerCase(), Toast.LENGTH_SHORT).show();
      roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          String roomName = snapshot.child("name").getValue(String.class);
          Log.d("aaa", "room name: " + roomName);
          for (DataSnapshot device : snapshot.child("devices").getChildren()) {
            String deviceId = device.getKey();
            String deviceName = device.child("name").getValue(String.class);
            String devicePath = roomPath + "/devices/" + deviceId + "/state";
            DatabaseReference deviceRef = db.getReference(devicePath);
            if (deviceName.contains("Light"))
              deviceRef.setValue(1);
            else {
              deviceRef.setValue(2);
            }
          }
          deviceList.clear();
          getDevices(roomPath);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
      });
    } else if (text.toLowerCase().contains("tắt tất cả đèn")) {
      Toast.makeText(this, text.toLowerCase(), Toast.LENGTH_SHORT).show();
      roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          String roomName = snapshot.child("name").getValue(String.class);
          Log.d("aaa", "room name: " + roomName);
          for (DataSnapshot device : snapshot.child("devices").getChildren()) {
            String deviceId = device.getKey();
            String devicePath = roomPath + "/devices/" + deviceId + "/state";
            DatabaseReference deviceRef = db.getReference(devicePath);
            deviceRef.setValue(0);
          }
          deviceList.clear();
          getDevices(roomPath);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
      });
    }
  }

  public void getDevices(String roomPath) {
    adapter = new RoomAdapter(roomPath);

    rec_device.setLayoutManager(new GridLayoutManager(this, 2));
    db = FirebaseDatabase.getInstance();
    roomRef = db.getReference(roomPath);

    adapter.updateDeviceList(deviceList);
    rec_device.setAdapter(adapter);
    roomRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        deviceList.clear();
        String roomName = snapshot.child("name").getValue(String.class);
        topAppBar.setTitle(roomName);
        boolean isDHT = false, isGas = false;
        for (DataSnapshot device : snapshot.child("devices").getChildren()) {
          String deviceId = device.getKey();
          String nameDevice = device.child("name").getValue(String.class);
          int endTime = device.child("endTime").getValue(Integer.class);
          int startTime = device.child("startTime").getValue(Integer.class);
          int state = device.child("state").getValue(Integer.class);
          Log.i("Check If", "Ok");
          deviceList.add(new Devices(deviceId, endTime, nameDevice, startTime, state, null));
        }
        for (DataSnapshot DHT : snapshot.child("DHT").getChildren()) {
          float temp = DHT.child("temperature").getValue(Float.class);
          float humid = DHT.child("humidity").getValue(Float.class);
          txtTemp.setText(temp + "°C");
          txtHumid.setText(humid + "%");
          isDHT = true;
        }
        for (DataSnapshot sensor : snapshot.child("otherSensors").getChildren()) {
          int state = sensor.child("state").getValue(Integer.class);
          String txtState;
          if (state > 500) {
            txtGas.setTextColor(Color.RED);
            txtState = "High";
          } else {
            txtGas.setTextColor(Color.BLACK);
            txtState = "Normal";
          }
          txtGas.setText(txtState);
          isGas = true;
        }

        if (!isDHT) linearLayout.removeView(cardViewDHT);
        if (!isGas) linearLayout.removeView(cardViewGas);

        updateDeviceView();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
      }
    });
  }

  public void updateDeviceView() {
    adapter.updateDeviceList(deviceList);
    adapter.notifyDataSetChanged();
  }

  @Override
  protected void onStart() {
    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    registerReceiver(networkChangeListener, filter);
    super.onStart();
  }

  @Override
  protected void onStop() {
    unregisterReceiver(networkChangeListener);
    super.onStop();
  }
}