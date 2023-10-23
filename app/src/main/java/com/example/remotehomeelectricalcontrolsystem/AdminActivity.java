package com.example.remotehomeelectricalcontrolsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.remotehomeelectricalcontrolsystem.Adapter.UserAdapter;
import com.example.remotehomeelectricalcontrolsystem.Model.User;
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
  UserAdapter adapter;
  FirebaseDatabase db;
  DatabaseReference usersRef;
  FloatingActionButton fab;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin);

    init();

    usersRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
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
      Toast.makeText(AdminActivity.this, users.get(i).getUserId(), Toast.LENGTH_LONG).show();
      Bundle bundle = new Bundle();
      Intent intent = new Intent(AdminActivity.this, UserActivity.class);
      bundle.putString("userId", users.get(i).getUserId());
      intent.putExtras(bundle);
      startActivity(intent);
    });
  }

  public void init() {
    lv = findViewById(R.id.lv);
    users = new ArrayList<>();
    db = FirebaseDatabase.getInstance();
    usersRef = db.getReference("users");
    fab = findViewById(R.id.fab);
  }
}