package com.example.remotehomeelectricalcontrolsystem.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.remotehomeelectricalcontrolsystem.Adapter.UserAdapter;
import com.example.remotehomeelectricalcontrolsystem.Adapter.UserHouseAdapter;
import com.example.remotehomeelectricalcontrolsystem.AdminActivity;
import com.example.remotehomeelectricalcontrolsystem.Model.SharedUser;
import com.example.remotehomeelectricalcontrolsystem.Model.User;
import com.example.remotehomeelectricalcontrolsystem.Model.UserHouse;
import com.example.remotehomeelectricalcontrolsystem.R;
import com.example.remotehomeelectricalcontrolsystem.UserActivity;
import com.example.remotehomeelectricalcontrolsystem.Utils.EncryptionUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MembersFragment extends Fragment {
  ListView lv;
  ArrayList<User> users;
  UserAdapter adapter;
  FirebaseDatabase db;
  DatabaseReference usersRef, usersHousesRef;
  DatabaseReference userRef, userHouseRef;
  FloatingActionButton fab;

  public static MembersFragment newInstance() {
    MembersFragment fragment = new MembersFragment();
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View inflate = inflater.inflate(R.layout.fragment_members, container, false);
    init(inflate);

    Bundle bundle = this.getArguments();
    if (bundle != null) {
      String houseId = bundle.getString("houseId");
      usersHousesRef.orderByChild("houseId").equalTo(houseId).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot usersHouses) {
          users.clear();
          for (DataSnapshot userHouse : usersHouses.getChildren()) {
            String userId = userHouse.child("userId").getValue(String.class);
            String role = userHouse.child("role").getValue(String.class);
            if (role.equals("member")) {
              userRef = db.getReference("users/" + userId);
              userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                  String name = userSnapshot.child("name").getValue(String.class);
                  String email = userSnapshot.child("email").getValue(String.class);
                  users.add(new User(userId, name, email, null, null));
                  adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
              });
            }
          }
          adapter = new UserAdapter(getActivity(), R.layout.item_user, users);
          lv.setAdapter(adapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
      });
    }

    fab.setOnClickListener(view -> {
      Bundle bundle1 = new Bundle();
      bundle1.putBoolean("isAdmin", false);
      bundle1.putString("userHouseId", null);
      Intent intent = new Intent(getActivity(), UserActivity.class);
      intent.putExtras(bundle1);
      startActivity(intent);
    });

    lv.setOnItemClickListener((adapterView, view, i, l) -> {
      Bundle bundle1 = new Bundle();
      String userId = users.get(i).getUserId();
      String houseIdToCheck = bundle.getString("houseId");
      Intent intent = new Intent(getActivity(), UserActivity.class);
      bundle1.putBoolean("isAdmin", false);
      usersHousesRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          Log.d("aaa", "Count user: " + snapshot.getChildrenCount());
          String userHouseId, role, houseId;
          for (DataSnapshot userHouse : snapshot.getChildren()) {
            userHouseId = userHouse.getKey();
            houseId = userHouse.child("houseId").getValue(String.class);
            if (houseIdToCheck.equals(houseId)){
              bundle1.putString("userHouseId", userHouseId);
              intent.putExtras(bundle1);
              startActivity(intent);
            }
          }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
      });
    });

    return inflate;
  }

  public void init(View view) {
    lv = view.findViewById(R.id.lv);
    users = new ArrayList<>();
    db = FirebaseDatabase.getInstance();
    usersRef = db.getReference("users");
    usersHousesRef = db.getReference("usersHouses");
    fab = view.findViewById(R.id.fab);
  }
}