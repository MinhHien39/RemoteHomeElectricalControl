package com.example.remotehomeelectricalcontrolsystem.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remotehomeelectricalcontrolsystem.Adapter.HomeAdapter;

import com.example.remotehomeelectricalcontrolsystem.Model.Floor;
import com.example.remotehomeelectricalcontrolsystem.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

  RecyclerView rec_home;
  HomeAdapter homeAdapter;
  TextView txtTemp, txtHumid;
  List<Floor> floorList;
  FirebaseDatabase db;

  public static HomeFragment newInstance() {
    HomeFragment fragment = new HomeFragment();
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View root = inflater.inflate(R.layout.fragment_home, container, false);
    FragmentManager fm = this.getChildFragmentManager();

    init(root);

    rec_home.scrollToPosition(1000);
    homeAdapter = new HomeAdapter();
    floorList = new ArrayList<>();
    Bundle bundle = this.getArguments();
    if (bundle != null) {
      String houseId = bundle.getString("houseId");
      String pathHouse = "/test1/" + houseId;
      DatabaseReference floorsRef = db.getReference(pathHouse + "/floors");
      homeAdapter.setHousePath(pathHouse);


      floorsRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          float sumTemp = 0, sumHumi = 0;
          int count = 0;
          for (DataSnapshot dataFloor : snapshot.getChildren()) {
            for(DataSnapshot dataRoom : dataFloor.child("rooms").getChildren()) {
              for(DataSnapshot dataDHT : dataRoom.child("DHT").getChildren()) {
                float temp = dataDHT.child("temperature").getValue(Float.class);
                float humid = dataDHT.child("humidity").getValue(Float.class);
                sumTemp += temp;
                sumHumi += humid;
                count++;
              }
            }
          }

          txtTemp.setText(String.valueOf(sumTemp/count) + "°C");
          txtHumid.setText(String.valueOf(sumHumi/count) + "%");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
      });

      floorsRef.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          for (DataSnapshot dataFloor : snapshot.getChildren()) {
            String floorId = dataFloor.getKey();
            String floorName = dataFloor.child("name").getValue(String.class);
            floorList.add(new Floor(floorId, floorName));
          }
          updateListView();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
          System.out.println(error.getMessage());
        }
      });
    }


    return root;
  }

  private void init(View root) {
    rec_home = root.findViewById(R.id.rec_floor);
    rec_home.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
    txtTemp = root.findViewById(R.id.txtTemp);
    txtHumid = root.findViewById(R.id.txtHumid);
    db = FirebaseDatabase.getInstance();
  }

  public void updateListView() {
    homeAdapter.updateListFloor(floorList);
    homeAdapter.notifyDataSetChanged();
    rec_home.setAdapter(homeAdapter);
  }
}
