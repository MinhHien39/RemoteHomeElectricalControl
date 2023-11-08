package com.example.remotehomeelectricalcontrolsystem.Adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.remotehomeelectricalcontrolsystem.R;

import com.example.remotehomeelectricalcontrolsystem.Model.UserHouse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserHouseAdapter extends BaseAdapter {
  ArrayList<UserHouse> listUserHouse;

  public UserHouseAdapter(ArrayList<UserHouse> listUserHouse) {
    this.listUserHouse = listUserHouse;
  }

  @Override
  public int getCount() {
    return listUserHouse.size();
  }

  @Override
  public Object getItem(int i) {
    return listUserHouse.get(i);
  }

  @Override
  public long getItemId(int i) {
    return 0;
  }

  @Override
  public View getView(int i, View convertView, ViewGroup parent) {
    View view;
    if (convertView == null) {
      view = View.inflate(parent.getContext(), R.layout.item_house, null);
    } else view = convertView;

    UserHouse userHouse = (UserHouse) getItem(i);
    ((TextView) view.findViewById(R.id.tvRole)).setText(userHouse.getRole());
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference housesRef = db.getReference("test1/" + userHouse.getHouseId() + "/name");

    housesRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        // This method is called once with the initial value and again
        // whenever data at this location is updated.
        String houseName = dataSnapshot.getValue(String.class);
        ((TextView) view.findViewById(R.id.tvHouseName)).setText(houseName);
      }

      @Override
      public void onCancelled(DatabaseError error) {
        // Failed to read value
      }
    });

    return view;
  }
}
