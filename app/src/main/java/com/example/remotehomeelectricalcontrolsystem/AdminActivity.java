package com.example.remotehomeelectricalcontrolsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.remotehomeelectricalcontrolsystem.Adapter.UserAdapter;
import com.example.remotehomeelectricalcontrolsystem.Adapter.UserHouseAdapter;
import com.example.remotehomeelectricalcontrolsystem.Model.SharedUser;
import com.example.remotehomeelectricalcontrolsystem.Model.SharedUserHouse;
import com.example.remotehomeelectricalcontrolsystem.Model.User;
import com.example.remotehomeelectricalcontrolsystem.Model.UserHouse;
import com.example.remotehomeelectricalcontrolsystem.Utils.EncryptionUtils;
import com.example.remotehomeelectricalcontrolsystem.Utils.NetworkChangeListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
  ListView lv;
  ImageButton imgMore;
  ArrayList<User> users;
  ArrayList<UserHouse> listUserHouse;
  UserAdapter adapter;
  UserHouseAdapter userHouseAdapter;
  FirebaseDatabase db;
  DatabaseReference usersRef, usersHousesRef;
  FloatingActionButton fab;
  NetworkChangeListener networkChangeListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin);

    init();

    usersRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        users.clear();
        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
          String userId = userSnapshot.getKey();
          String name = userSnapshot.child("name").getValue(String.class);
          String email = userSnapshot.child("email").getValue(String.class);
          String tel = userSnapshot.child("tel").getValue(String.class);
          String hashPassword = userSnapshot.child("password").getValue(String.class);
          String password = EncryptionUtils.decrypt(hashPassword);
          users.add(new User(userId, name, email, tel, password));
        }
        adapter = new UserAdapter(AdminActivity.this, R.layout.item_user, users);
        lv.setAdapter(adapter);
      }


      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });

    imgMore.setOnClickListener(view -> showPopup());

    fab.setOnClickListener(view -> {
      Intent intent = new Intent(AdminActivity.this, UserActivity.class);
      startActivity(intent);
    });

    lv.setOnItemClickListener((adapterView, view, i, l) -> {
      Bundle bundle = new Bundle();
      String userId = users.get(i).getUserId();
      Intent intent = new Intent(AdminActivity.this, UserActivity.class);
      bundle.putBoolean("isAdmin", true);
      usersHousesRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          Log.d("aaa", "Count user: " + snapshot.getChildrenCount());
          String userHouseId, role, houseId;
          listUserHouse.clear();
          for (DataSnapshot userHouse : snapshot.getChildren()) {
            userHouseId = userHouse.getKey();
            role = userHouse.child("role").getValue(String.class);
            houseId = userHouse.child("houseId").getValue(String.class);
            listUserHouse.add(new UserHouse(userHouseId, userId, houseId, role));
          }
          if (snapshot.getChildrenCount() > 1) {
            Dialog houseSelectionDialog = new Dialog(AdminActivity.this);
            houseSelectionDialog.setContentView(R.layout.selection_dialog);
            houseSelectionDialog.show();
            ListView lvHouse = houseSelectionDialog.findViewById(R.id.lvHouse);
            userHouseAdapter = new UserHouseAdapter(listUserHouse);
            lvHouse.setAdapter(userHouseAdapter);
            lvHouse.setOnItemClickListener((adapterView, view, i, l) -> {
              String userHouseIdSelected = listUserHouse.get(i).getUserHouseId();
              bundle.putString("userHouseId", userHouseIdSelected);
              houseSelectionDialog.dismiss();
              intent.putExtras(bundle);
              startActivity(intent);
            });
          } else {
            userHouseId = listUserHouse.get(0).getUserHouseId();
            bundle.putString("userHouseId", userHouseId);
            intent.putExtras(bundle);
            startActivity(intent);
          }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
      });
    });
  }

  public void init() {
    lv = findViewById(R.id.lv);
    imgMore = findViewById(R.id.imgBtn);
    users = new ArrayList<>();
    listUserHouse = new ArrayList<>();
    db = FirebaseDatabase.getInstance();
    usersRef = db.getReference("users");
    usersHousesRef = db.getReference("usersHouses");
    fab = findViewById(R.id.fab);
    networkChangeListener = new NetworkChangeListener();
  }

  public void showPopup() {
    PopupMenu popup = new PopupMenu(this, imgMore);
    popup.getMenuInflater().inflate(R.menu.top_app_bar, popup.getMenu());
    popup.setOnMenuItemClickListener(menuItem -> {
      int itemId = menuItem.getItemId();
      if (itemId == R.id.action_profile) {
        Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
      } else if (itemId == R.id.action_contact) {
        Intent intent = new Intent(AdminActivity.this, ContactActivity.class);
        startActivity(intent);
      }
      else if (itemId == R.id.action_change_password) {
        Intent intent = new Intent(AdminActivity.this, ChangePasswordActivity.class);
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
    MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(AdminActivity.this).setTitle("Confirm logout").setMessage("Are you sure to log out?").setPositiveButton("Log out", (dialogInterface, i) -> {
      clearData();
      Toast.makeText(AdminActivity.this, "Log out of account successfully!", Toast.LENGTH_SHORT).show();
      Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      startActivity(intent);
      finish();
    }).setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
    dialog.show();
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