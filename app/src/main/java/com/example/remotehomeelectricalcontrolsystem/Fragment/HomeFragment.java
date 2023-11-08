package com.example.remotehomeelectricalcontrolsystem.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.remotehomeelectricalcontrolsystem.Adapter.HomeAdapter;

import com.example.remotehomeelectricalcontrolsystem.Model.Floor;
import com.example.remotehomeelectricalcontrolsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
  public static final String TAG = HomeFragment.class.getName();

  RecyclerView rec_home;
  HomeAdapter homeAdapter;
  List<Floor> floorList;

  public static HomeFragment newInstance() {
    HomeFragment fragment = new HomeFragment();
    return fragment;
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    Bundle bundle = this.getArguments();
    String houseId = bundle.getString("houseId");
    Log.i("houseId Bundle In Home Fragment" , houseId);
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String pathHouse = "/test1/" + houseId;
    DatabaseReference houseRef = database.getReference(pathHouse + "/floors");
    homeAdapter.setHousePath(pathHouse);
    houseRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot dataFloor : snapshot.getChildren()) {
          Log.i("Check Key In Home Fragment " , dataFloor.getKey());
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

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View root = inflater.inflate(R.layout.fragment_home, container, false);
    FragmentManager fm = this.getChildFragmentManager();
    rec_home = root.findViewById(R.id.rec_floor);
    rec_home.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
    rec_home.scrollToPosition(1000);
    homeAdapter = new HomeAdapter();
    floorList = new ArrayList<>();
    Bundle bundle = this.getArguments();
    if (bundle != null) {
      String houseId = bundle.getString("houseId");
      Log.i("houseId Bundle In Home Fragment" , houseId);
      FirebaseDatabase database = FirebaseDatabase.getInstance();
      String pathHouse = "/test1/" + houseId;
      DatabaseReference houseRef = database.getReference(pathHouse + "/floors");
      homeAdapter.setHousePath(pathHouse);
      houseRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          for (DataSnapshot dataFloor : snapshot.getChildren()) {
            Log.i("Check Key In Home Fragment " , dataFloor.getKey());
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
    //updateListView();
    return root;
  }


  public void updateListView() {
    homeAdapter.updateListFloor(floorList);
    homeAdapter.notifyDataSetChanged();
    rec_home.setAdapter(homeAdapter);
  }



  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //Log.i("Check Save State" , savedInstanceState.toString());
  }


}
