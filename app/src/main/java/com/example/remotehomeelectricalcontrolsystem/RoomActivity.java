package com.example.remotehomeelectricalcontrolsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.remotehomeelectricalcontrolsystem.Adapter.RoomAdapter;
import com.example.remotehomeelectricalcontrolsystem.Model.Devices;
import com.example.remotehomeelectricalcontrolsystem.Utils.NetworkChangeListener;
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
    TextView txtHumidity;
    TextView txtTemperature;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Intent intent = getIntent();
        if (intent != null) {
            String roomPath = intent.getStringExtra("roomPath");
            Log.d("aaa", "roomPath" + roomPath);
            getDevices(roomPath);

        }
    }
    public void getDevices(String roomPath) {
        rec_device = findViewById(R.id.rec_room);
        deviceList = new ArrayList<>();
        adapter = new RoomAdapter();
        //txtHumidity = findViewById(R.id.txtHumidity);
        txtTemperature = findViewById(R.id.txtTemperature);

        rec_device.setLayoutManager(new GridLayoutManager(this, 2));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference roomRef = database.getReference(roomPath);

        adapter.updateDeviceList(deviceList);
        rec_device.setAdapter(adapter);
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String roomName = snapshot.child("name").getValue(String.class);
                Log.d("aaa", "room name: " + roomName);
                for (DataSnapshot device : snapshot.child("devices").getChildren()) {
                    String deviceId = device.getKey();
                    String nameDevice = device.child("name").getValue(String.class);
                    int endTime = device.child("endTime").getValue(Integer.class);
                    int startTime = device.child("startTime").getValue(Integer.class);
                    int state = device.child("state").getValue(Integer.class);
                    Log.i("Check If", "Ok");
                    deviceList.add(new Devices(endTime, nameDevice, startTime, state, null));
                }
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