package com.example.remotehomeelectricalcontrolsystem.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.remotehomeelectricalcontrolsystem.Adapter.HomeAdatper;

import com.example.remotehomeelectricalcontrolsystem.Model.Floor;
import com.example.remotehomeelectricalcontrolsystem.Model.House;
import com.example.remotehomeelectricalcontrolsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    RecyclerView rec_home;
    HomeAdatper homeAdatper;
    List<Floor> floorList;

    public static HomeFragment newInstance(){
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        FragmentManager fm = this.getChildFragmentManager();
        rec_home = root.findViewById(R.id.rec_floor);
        rec_home.setLayoutManager(new GridLayoutManager(this.getContext() , 2));
        rec_home.scrollToPosition(1000);
        homeAdatper = new HomeAdatper();
        floorList = new ArrayList<>();
        //floorList.add(new Floor(1 , "Floor 1"));
        //floorList.add(new Floor(1 , "Floor 2"));
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
                        String floorId = dataFloor.getKey();
                        String floorName = dataFloor.child("name").getValue(String.class);
                        Log.i("idFloor" , floorId);
                        Log.i("nameFloor" , floorName);
                        floorList.add(new Floor(floorId , floorName));
                        homeAdatper.notifyDataSetChanged();
                    }
                    Log.i("houseId" , houseId);
                    Log.i("houseName" , houseName);
                    Log.i("emailHost" , emailHost);
                    Log.i("houseKey" , houseKey);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());

            }
        });

        //floorList.add(new Floor( "Floor 3"));
        homeAdatper.updateListFloor(floorList);
        homeAdatper.notifyDataSetChanged();
        rec_home.setAdapter(homeAdatper);
        return root;
    }

}