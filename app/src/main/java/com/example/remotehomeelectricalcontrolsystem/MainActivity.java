package com.example.remotehomeelectricalcontrolsystem;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;

import com.example.remotehomeelectricalcontrolsystem.Adapter.UserHouseAdapter;
import com.example.remotehomeelectricalcontrolsystem.Fragment.HomeFragment;
import com.example.remotehomeelectricalcontrolsystem.Fragment.MembersFragment;
import com.example.remotehomeelectricalcontrolsystem.Model.SharedUser;
import com.example.remotehomeelectricalcontrolsystem.Model.SharedUserHouse;
import com.example.remotehomeelectricalcontrolsystem.Model.User;
import com.example.remotehomeelectricalcontrolsystem.Model.UserHouse;
import com.example.remotehomeelectricalcontrolsystem.Utils.NetworkChangeListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
  Fragment homeFragment, membersFragment;
  ImageButton imgMore;
  MaterialToolbar topAppBar;
  ArrayList<UserHouse> listUserHouse;
  UserHouseAdapter userHouseAdapter;
  BottomNavigationView bottomNavigation;
  NetworkChangeListener networkChangeListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    init();

    UserHouse userHouse = SharedUserHouse.getUserHouse();
    if (userHouse != null) {
      if (userHouse.getRole().equals("admin")) {
        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
        startActivity(intent);
        finish();
      } else {
        initViews(savedInstanceState, userHouse.getHouseId(), userHouse.getRole());
      }
    } else {
      User user = SharedUser.getUser();
      if (user != null) {
        usersHousesRef.orderByChild("userId").equalTo(user.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot userHouse : snapshot.getChildren()) {
              String userHouseId = userHouse.getKey();
              String role = userHouse.child("role").getValue(String.class).toString();
              String houseId = userHouse.child("houseId").getValue(String.class).toString();

              showHouseName(houseId);

              if (snapshot.getChildrenCount() > 1) {
                showHouseSelectionDialog(user.getUserId());
                break;
              } else {
                if (role.equals("admin")) {
                  Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                  startActivity(intent);
                  finish();
                } else {
                  initViews(savedInstanceState, houseId, role);
                  SharedUserHouse.setUserHouse(new UserHouse(userHouseId, user.getUserId(), houseId, role));
                  if (role.equals("host")) {
                    Intent serviceIntent = new Intent(MainActivity.this, BackgroundService.class);
                    startService(serviceIntent);
                  }
                }
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
    imgMore.setOnClickListener(view -> showPopup());
  }


  public void init() {
    db = FirebaseDatabase.getInstance();
    usersHousesRef = db.getReference("usersHouses");
    homeFragment = HomeFragment.newInstance();
    membersFragment = MembersFragment.newInstance();
    imgMore = findViewById(R.id.imgBtn);
    topAppBar = findViewById(R.id.topAppBar);
    listUserHouse = new ArrayList<>();
    networkChangeListener = new NetworkChangeListener();
  }

  private void showHouseName(String houseId) {
    DatabaseReference houseNameRef = db.getReference("test1/" + houseId + "/name");
    houseNameRef.get().addOnCompleteListener(task -> {
      if (!task.isSuccessful()) {
        Toast.makeText(MainActivity.this, "Get data error", Toast.LENGTH_SHORT).show();
      } else {
        String houseName = task.getResult().getValue(String.class);
        topAppBar.setTitle(houseName);
      }
    });
  }

  public void showPopup() {
    PopupMenu popup = new PopupMenu(this, imgMore);
    popup.getMenuInflater().inflate(R.menu.top_app_bar, popup.getMenu());
    popup.setOnMenuItemClickListener(menuItem -> {
      int itemId = menuItem.getItemId();
      if (itemId == R.id.action_profile) {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
      } else if (itemId == R.id.action_contact) {
        Intent intent = new Intent(MainActivity.this, ContactActivity.class);
        startActivity(intent);
      }
      else if (itemId == R.id.action_change_password) {
        Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
      }
      else if (itemId == R.id.action_logout) {
        handleLogout();
      }
      return false;
    });
    popup.show();
  }

  private void clearData() {
    SharedUserHouse.setUserHouse(null);
    SharedUser.setUser(null);
    SharedPreferences sharedPreferences = getSharedPreferences("house", MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.clear();
    editor.apply();
  }

  private void handleLogout() {
    MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(MainActivity.this).setBackground(new ColorDrawable(Color.parseColor("#FF003549"))).setTitle("Confirm logout").setMessage("Are you sure to log out?").setPositiveButton("Log out", (dialogInterface, i) -> {
      clearData();
      Toast.makeText(MainActivity.this, "Log out of account successfully!", Toast.LENGTH_SHORT).show();
      Intent intent = new Intent(MainActivity.this, LoginActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      startActivity(intent);
      finish();
    }).setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
    dialog.show();
  }


  private void showHouseSelectionDialog(String userId) {
    usersHousesRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        for (DataSnapshot userHouse : snapshot.getChildren()) {
          String userHouseId = userHouse.getKey();
          String role = userHouse.child("role").getValue(String.class).toString();
          String houseId = userHouse.child("houseId").getValue(String.class).toString();
          listUserHouse.add(new UserHouse(userHouseId, userId, houseId, role));
        }
        Dialog houseSelectionDialog = new Dialog(MainActivity.this);
        houseSelectionDialog.setContentView(R.layout.selection_dialog);
        houseSelectionDialog.show();
        ListView lvHouse = houseSelectionDialog.findViewById(R.id.lvHouse);
        userHouseAdapter = new UserHouseAdapter(listUserHouse);
        lvHouse.setAdapter(userHouseAdapter);
        lvHouse.setOnItemClickListener((adapterView, view, i, l) -> {
          String userHouseId = listUserHouse.get(i).getUserHouseId();
          String houseId = listUserHouse.get(i).getHouseId();
          String role = listUserHouse.get(i).getRole();
          SharedUserHouse.setUserHouse(new UserHouse(userHouseId, userId, houseId, role));
          initViews(null, houseId, role);
          houseSelectionDialog.dismiss();
        });
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
  }

  public void initViews(Bundle savedInstanceState, String houseId, String userRole) {
    bottomNavigation = findViewById(R.id.bottomNavigation);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.frame_layout, HomeFragment.class, null).commit();
    }
    Bundle bundle = new Bundle();
    bundle.putString("houseId", houseId);
    homeFragment.setArguments(bundle);
    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();

    Bundle bundle1 = getIntent().getExtras();
    if (bundle1 != null) {
      Boolean hasChange = bundle1.getBoolean("hasChange");
      if (hasChange) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, membersFragment).commit();
        membersFragment.setArguments(bundle);
        topAppBar.setTitle("Manage members");
      }
    }

    // Check the user's role
    if ("member".equals(userRole)) {
      // If the user is a member, hide the bottom navigation
      bottomNavigation.setVisibility(View.GONE);
    } else {
      // If the user is not a member, show the bottom navigation
      bottomNavigation.setVisibility(View.VISIBLE);

      // Set up the bottom navigation items and listener
      bottomNavigation.setOnItemSelectedListener(item -> {
        if (item.getItemId() == R.id.nav_home) {
          getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();
          homeFragment.setArguments(bundle);
          topAppBar.setTitle("Home");
          return true;
        } else if (item.getItemId() == R.id.nav_members) {
          getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, membersFragment).commit();
          membersFragment.setArguments(bundle);
          topAppBar.setTitle("Manage members");
          return true;
        } else {
          return false;
        }
      });
    }
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