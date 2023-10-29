package com.example.remotehomeelectricalcontrolsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.remotehomeelectricalcontrolsystem.Adapter.RoomAdapter;
import com.example.remotehomeelectricalcontrolsystem.Model.Devices;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Bundle bundle = getIntent().getExtras();
        rec_device = findViewById(R.id.rec_room);
        deviceList = new ArrayList<>();
        adapter = new RoomAdapter();
        //txtHumidity = findViewById(R.id.txtHumidity);
        txtTemperature = findViewById(R.id.txtTemperature);

        rec_device.setLayoutManager(new GridLayoutManager(this, 2));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("test1");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    String houseId = data.getKey();
                    for(DataSnapshot dataFloor : data.child("floors").getChildren()){
                        String idFloor = dataFloor.getKey();
                        String nameFloor = dataFloor.child("name").getValue(String.class);
                        String idKeyFloor = bundle.getString("Floor1");
                        String idKeyFloor2 = bundle.getString("Floor2");
                        if ("e72cf36f-f9c5-4dee-b11a-951c0e3dc638".equals(idFloor)){
                            for(DataSnapshot dataRoom : dataFloor.child("rooms").getChildren()){
                                String keyRoom = bundle.getString("keyRoom");
                                String idRoom = dataRoom.getKey();
                                String nameRoom = dataRoom.child("name").getValue(String.class);
                                String imgUrl = dataRoom.child("imgUrl").getValue(String.class);
                                String idDHTCheck = dataRoom.child("rooms/DHT").getChildren().toString();
                                    for(DataSnapshot dataDevice : dataRoom.child("devices").getChildren()){
                                        String idDevice = dataDevice.getKey();
                                        Long listCheck = dataRoom.child("devices").getChildrenCount();
                                        Log.i("List Device" , String.valueOf(listCheck));

                                        String nameDevice = dataDevice.child("name").getValue(String.class);
                                        int endTime = dataDevice.child("endTime").getValue(Integer.class);
                                        int startTime = dataDevice.child("startTime").getValue(Integer.class);
                                        int state = dataDevice.child("state").getValue(Integer.class);
                                        String imgUrlDevice  = dataDevice.child("imgUrl").getValue(String.class);
                                        System.out.println("Check Key Room " + keyRoom + "" + idRoom);
                                        if (keyRoom.equals(idRoom)) {
                                            Log.i("Check If", "Ok");
                                            deviceList.add(new Devices(endTime, nameDevice, startTime, state , imgUrlDevice));
                                            adapter.updateDeviceList(deviceList);
                                            adapter.notifyDataSetChanged();
                                        }
                                        Log.i("Get Data Room" , "Ok");
                                        Log.i("keyRoom" , keyRoom +" @ "+ idRoom);
                                    }
                                //}
                                Log.i("CheckData" , idRoom + nameRoom);
                            }
                        }
                        if ("40b38a85-36ea-4a72-921a-2fbb579a2daf".equals(idFloor)){
                            for(DataSnapshot dataRoom : dataFloor.child("rooms").getChildren()){
                                String keyRoom = bundle.getString("keyRoom");
                                String idRoom = dataRoom.getKey();
                                String nameRoom = dataRoom.child("name").getValue(String.class);
                                String imgUrl = dataRoom.child("imgUrl").getValue(String.class);
                                String idDHTCheck = dataRoom.child("rooms/DHT").getChildren().toString();
                                for(DataSnapshot dataDevice : dataRoom.child("devices").getChildren()){
                                    String idDevice = dataDevice.getKey();
                                    Long listCheck = dataRoom.child("devices").getChildrenCount();
                                    Log.i("List Device" , String.valueOf(listCheck));

                                    String nameDevice = dataDevice.child("name").getValue(String.class);
                                    int endTime = dataDevice.child("endTime").getValue(Integer.class);
                                    int startTime = dataDevice.child("startTime").getValue(Integer.class);
                                    int state = dataDevice.child("state").getValue(Integer.class);
                                    String imgUrlDevice  = dataDevice.child("imgUrl").getValue(String.class);
                                    System.out.println("Check Key Room " + keyRoom + "" + idRoom);
                                    if (keyRoom.equals(idRoom)) {
                                        Log.i("Check If", "Ok");
                                        deviceList.add(new Devices(endTime, nameDevice, startTime, state , imgUrlDevice));
                                        adapter.updateDeviceList(deviceList);
                                        adapter.notifyDataSetChanged();
                                    }
                                    Log.i("Get Data Room" , "Ok");
                                    Log.i("keyRoom" , keyRoom +" @ "+ idRoom);
                                }
                                //}
                                Log.i("CheckData" , idRoom + nameRoom);
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter.updateDeviceList(deviceList);
        rec_device.setAdapter(adapter);
    }
}