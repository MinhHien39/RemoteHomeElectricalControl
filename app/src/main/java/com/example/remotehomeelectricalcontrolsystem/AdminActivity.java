package com.example.remotehomeelectricalcontrolsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.remotehomeelectricalcontrolsystem.Adapter.UserAdapter;
import com.example.remotehomeelectricalcontrolsystem.Adapter.UserHouseAdapter;
import com.example.remotehomeelectricalcontrolsystem.Model.User;
import com.example.remotehomeelectricalcontrolsystem.Model.UserHouse;
import com.example.remotehomeelectricalcontrolsystem.Utils.EncryptionUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
  ListView lv;
  ArrayList<User> users;
  ArrayList<UserHouse> listUserHouse;
  UserAdapter adapter;
  UserHouseAdapter userHouseAdapter;
  FirebaseDatabase db;
  DatabaseReference usersRef, usersHousesRef;
  FloatingActionButton fab;

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

    fab.setOnClickListener(view -> {
      Intent intent = new Intent(AdminActivity.this, UserActivity.class);
      startActivity(intent);
    });

    lv.setOnItemClickListener((adapterView, view, i, l) -> {
      Bundle bundle = new Bundle();
      String userId = users.get(i).getUserId();
      Intent intent = new Intent(AdminActivity.this, UserActivity.class);
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
    users = new ArrayList<>();
    listUserHouse = new ArrayList<>();
    db = FirebaseDatabase.getInstance();
    usersRef = db.getReference("users");
    usersHousesRef = db.getReference("usersHouses");
    fab = findViewById(R.id.fab);
  }
}