package com.example.remotehomeelectricalcontrolsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.remotehomeelectricalcontrolsystem.Fragment.HomeFragment;
import com.example.remotehomeelectricalcontrolsystem.Fragment.ProfileFragment;
import com.example.remotehomeelectricalcontrolsystem.Model.SharedUser;
import com.example.remotehomeelectricalcontrolsystem.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
  private FirebaseDatabase db;
  private DatabaseReference usersRef, housesRef, usersHousesRef;
  FrameLayout frameLayout;
  Fragment homeFragment;

  BottomNavigationView bottomNavigationView;

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
            Log.d("aaa", "role: " + role);
            if (role.equals("admin")) {
              Intent intent = new Intent(MainActivity.this, AdminActivity.class);
              startActivity(intent);
              finish();
            } else {
              initViews(savedInstanceState, houseId);
            }
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
  }

  public void initViews(Bundle savedInstanceState, String houseId) {
    bottomNavigationView = findViewById(R.id.bottomNavigationView);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.frame_layout,
          HomeFragment.class, null).commit();
    }
    Bundle bundle = new Bundle();
    bundle.putString("houseId", houseId);
    homeFragment.setArguments(bundle);
    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();


    bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
          homeFragment = HomeFragment.newInstance();
        } else if (item.getItemId() == R.id.nav_profile) {
          homeFragment = ProfileFragment.newInstance();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();
        return true;
      }
    });
  }
}