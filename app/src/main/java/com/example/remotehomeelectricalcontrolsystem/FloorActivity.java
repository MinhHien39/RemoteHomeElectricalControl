package com.example.remotehomeelectricalcontrolsystem;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remotehomeelectricalcontrolsystem.Adapter.FloorAdapter;
import com.example.remotehomeelectricalcontrolsystem.Model.Devices;
import com.example.remotehomeelectricalcontrolsystem.Model.Room;
import com.example.remotehomeelectricalcontrolsystem.Utils.NetworkChangeListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FloorActivity extends AppCompatActivity {
  RecyclerView rec_room_floor;
  MaterialToolbar topAppBar;
  FloorAdapter floorAdapter;
  List<Room> roomList;
  List<Devices> list;
  NetworkChangeListener networkChangeListener = new NetworkChangeListener();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_floor);

    init();

    Intent intent = getIntent();

    if (intent != null) {
      String floorPath = intent.getStringExtra("floorPath");
      if (floorPath == null) {
        SharedPreferences sharedPreferences = getSharedPreferences("house", MODE_PRIVATE);
        String houseId = sharedPreferences.getString("houseId", "");
        String floorId = sharedPreferences.getString("floorId", "");
        floorPath = "test1/" + houseId + "/floors/" + floorId;
      }
      Log.d("aaa", "floorPath" + floorPath);
      getRooms(floorPath);
    }

    topAppBar.setNavigationOnClickListener(view -> finish());
  }

  private void init() {
    rec_room_floor = findViewById(R.id.rec_room_floor);
    roomList = new ArrayList<>();
    topAppBar = findViewById(R.id.topAppBar);
  }

  public void getRooms(String floorPath) {
    floorAdapter = new FloorAdapter(floorPath);
    rec_room_floor.setLayoutManager(new GridLayoutManager(this, 2));
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference floorRef = database.getReference(floorPath);
    floorRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        String floorName = snapshot.child("name").getValue(String.class);
        topAppBar.setTitle(floorName);
        Log.d("aaa", "floor name: " + floorName);
        for (DataSnapshot room : snapshot.child("rooms").getChildren()) {
          String idRoom = room.getKey();
          String nameRoom = room.child("name").getValue(String.class);
          String imgUrl = room.child("imgUrl").getValue(String.class);
          for (DataSnapshot dataDevice : room.child("devices").getChildren()) {
            String idDevice = dataDevice.getKey();
            Long listCheck = room.child("devices").getChildrenCount();
            list = new ArrayList<>();
            for (int i = 0; i < listCheck; i++) {
              String nameDevice = dataDevice.child("name").getValue(String.class);
              int endTime = dataDevice.child("endTime").getValue(Integer.class);
              int startTime = dataDevice.child("startTime").getValue(Integer.class);
              int state = dataDevice.child("state").getValue(Integer.class);
              String imgUrlDevice = dataDevice.child("imgUrl").getValue(String.class);
              list.add(new Devices(idDevice, endTime, nameDevice, startTime, state, imgUrlDevice));
            }

            System.out.println("Check List " + list.size());
          }
          roomList.add(new Room(idRoom, nameRoom, imgUrl, list));
        }
        updateFloorView();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
        System.out.println(error.getMessage());
      }
    });

  }

  public void sortByRoomName() {
    Collections.sort(roomList, Comparator.comparing(Room::getNameRoom));
  }

  public void updateFloorView() {
    sortByRoomName();
    floorAdapter.updateListRoom(roomList);
    floorAdapter.notifyDataSetChanged();
    rec_room_floor.setAdapter(floorAdapter);
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