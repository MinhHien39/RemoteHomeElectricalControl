package com.example.remotehomeelectricalcontrolsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remotehomeelectricalcontrolsystem.Adapter.FloorAdapter;
import com.example.remotehomeelectricalcontrolsystem.Model.Devices;
import com.example.remotehomeelectricalcontrolsystem.Model.Room;
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
  FloorAdapter floorAdapter;
  List<Room> roomList;
  List<Devices> list;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_floor);
    Intent intent = getIntent();
    if (intent != null) {
      String floorPath = intent.getStringExtra("floorPath");
      Log.d("aaa", "path" + floorPath);
      getRooms(floorPath);
    }
  }

  public void getRooms(String floorPath) {
    rec_room_floor = findViewById(R.id.rec_room_floor);
    roomList = new ArrayList<>();
    floorAdapter = new FloorAdapter(floorPath);
    rec_room_floor.setLayoutManager(new GridLayoutManager(this, 2));
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference floorRef = database.getReference(floorPath);
    floorRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        String floorName = snapshot.child("name").getValue(String.class);
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
              list.add(new Devices(endTime, nameDevice, startTime, state, imgUrlDevice));
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
    Collections.sort(roomList, new Comparator<Room>() {
      @Override
      public int compare(Room room1, Room room2) {
        return room1.getNameRoom().compareTo(room2.getNameRoom());
      }
    });
  }

  public void updateFloorView() {
    sortByRoomName();
    floorAdapter.updateListRoom(roomList);
    floorAdapter.notifyDataSetChanged();
    rec_room_floor.setAdapter(floorAdapter);
  }
}