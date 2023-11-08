package com.example.remotehomeelectricalcontrolsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.remotehomeelectricalcontrolsystem.Adapter.UserHouseAdapter;
import com.example.remotehomeelectricalcontrolsystem.Fragment.HomeFragment;
import com.example.remotehomeelectricalcontrolsystem.Fragment.ProfileFragment;
import com.example.remotehomeelectricalcontrolsystem.Model.SharedUser;
import com.example.remotehomeelectricalcontrolsystem.Model.User;
import com.example.remotehomeelectricalcontrolsystem.Model.UserHouse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  private FirebaseDatabase db;
  private DatabaseReference usersRef, housesRef, usersHousesRef;
  FrameLayout frameLayout;
  Fragment homeFragment, profileFragment;
  ArrayList<UserHouse> listUserHouse;
  UserHouseAdapter userHouseAdapter;
  BottomNavigationView bottomNavigation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    init();

    User user = SharedUser.getUser();


    if (user != null) {
      usersHousesRef.orderByChild("userId").equalTo(user.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          for (DataSnapshot userHouse : snapshot.getChildren()) {
            String role = userHouse.child("role").getValue(String.class).toString();
            String houseId = userHouse.child("houseId").getValue(String.class).toString();
            if (snapshot.getChildrenCount() > 1) {
              showHouseSelectionDialog(user.getUserId());
              break;
            } else {
              if (role.equals("admin")) {
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(intent);
                finish();
              } else {
                initViews(savedInstanceState, houseId);
              }
            }
            Log.d("aaa", "count: " + snapshot.getChildrenCount());
          }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
      });
    } else {
      Intent intent = new Intent(MainActivity.this, LoginActivity.class);
      startActivity(intent);
      finish();
    }
  }

  public void init() {
    db = FirebaseDatabase.getInstance();
    usersHousesRef = db.getReference("usersHouses");
    homeFragment = HomeFragment.newInstance();
    listUserHouse = new ArrayList<>();
  }

  private void showHouseSelectionDialog(String userId) {
    usersHousesRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot userHouse : snapshot.getChildren()) {
          String role = userHouse.child("role").getValue(String.class).toString();
          String houseId = userHouse.child("houseId").getValue(String.class).toString();
          listUserHouse.add(new UserHouse(null, userId, houseId, role));
        }
        Dialog houseSelectionDialog = new Dialog(MainActivity.this);
        houseSelectionDialog.setContentView(R.layout.selection_dialog);
        houseSelectionDialog.show();
        ListView lvHouse = houseSelectionDialog.findViewById(R.id.lvHouse);
        userHouseAdapter = new UserHouseAdapter(listUserHouse);
        lvHouse.setAdapter(userHouseAdapter);
        lvHouse.setOnItemClickListener((adapterView, view, i, l) -> {
          String houseId = listUserHouse.get(i).getHouseId();
          initViews(null, houseId);
          houseSelectionDialog.dismiss();
        });
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }

  public void initViews(Bundle savedInstanceState, String houseId) {
    bottomNavigation = findViewById(R.id.bottomNavigationView);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.frame_layout,
          HomeFragment.class, null).commit();
    }
    Bundle bundle = new Bundle();
    bundle.putString("houseId", houseId);
    homeFragment.setArguments(bundle);
    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();


<<<<<<< HEAD
    bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
          homeFragment = HomeFragment.newInstance();
          Log.i("Home Fragment" , "Check Home");
        } else if (item.getItemId() == R.id.nav_profile) {
          homeFragment = ProfileFragment.newInstance();
          Log.i("Profile Fragment" , "Check Profile");
        }
=======
    bottomNavigation.setOnItemSelectedListener(item -> {
      if (item.getItemId() == R.id.nav_home) {
        homeFragment = HomeFragment.newInstance();
>>>>>>> 5ad39a7495a5bd68af94ca94b32c5dd740edeae1
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();
        homeFragment.setArguments(bundle);
        return true;
      } else if (item.getItemId() == R.id.nav_profile) {
        profileFragment = ProfileFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, profileFragment).commit();
        return true;
      } else
        return false;
    });
  }
}