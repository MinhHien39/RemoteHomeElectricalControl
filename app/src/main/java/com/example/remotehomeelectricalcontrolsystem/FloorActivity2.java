package com.example.remotehomeelectricalcontrolsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.remotehomeelectricalcontrolsystem.Adapter.FloorAdapter;
import com.example.remotehomeelectricalcontrolsystem.Model.Devices;
import com.example.remotehomeelectricalcontrolsystem.Model.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FloorActivity2 extends AppCompatActivity {
    RecyclerView rec_room_floor2;
    FloorAdapter floorAdapter;
    List<Room> roomList;

    List<Devices> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor2);
        Bundle bundle = getIntent().getExtras();
        rec_room_floor2 = findViewById(R.id.rec_room_floor2);
        roomList = new ArrayList<>();
        floorAdapter = new FloorAdapter();
        rec_room_floor2.setLayoutManager(new GridLayoutManager(this, 2));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("test1");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    String houseId = data.getKey();
                    String houseName = data.child("name").getValue(String.class);
                    String emailHost = data.child("emailHost").getValue(String.class);
                    String houseKey = data.child("houseKey").getValue(String.class);
                    String telHost = data.child("telHost").getValue(String.class);
                    for(DataSnapshot dataFloor : data.child("floors").getChildren()){
                        String idFloor = dataFloor.getKey();
                        String nameFloor = dataFloor.child("name").getValue(String.class);
                        Log.i("keyFloor" , idFloor);
                        String idKeyFloor = bundle.getString("Floor2");
                        Log.i("keyBundle" , idKeyFloor);
                        if (bundle.getString("Floor2").equals(idFloor)) {
                            for (DataSnapshot dataRoom : dataFloor.child("rooms").getChildren()) {
                                String idRoom = dataRoom.getKey();
                                String nameRoom = dataRoom.child("name").getValue(String.class);
                                String imgUrl = dataRoom.child("imgUrl").getValue(String.class);
                                for (DataSnapshot dataDevice : dataRoom.child("devices").getChildren()) {
                                    String idDevice = dataDevice.getKey();
                                    Long listCheck = dataRoom.child("devices").getChildrenCount();
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
                                floorAdapter.notifyDataSetChanged();
                                //Log.i("URL IMAGE" , imgUrl);
                                Log.i("CheckData", idRoom + nameRoom);
                            }
                        }
                    }
//                    Log.i("houseId" , houseId);
//                    Log.i("houseName" , houseName);
//                    Log.i("emailHost" , emailHost);
//                    Log.i("houseKey" , houseKey);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());

            }
        });
        floorAdapter.updateListRoom(roomList);
        floorAdapter.notifyDataSetChanged();
        rec_room_floor2.setAdapter(floorAdapter);

    }
}