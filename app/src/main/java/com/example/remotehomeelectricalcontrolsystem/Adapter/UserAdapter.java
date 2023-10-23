package com.example.remotehomeelectricalcontrolsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.remotehomeelectricalcontrolsystem.Model.User;
import com.example.remotehomeelectricalcontrolsystem.R;

import java.util.List;

public class UserAdapter extends BaseAdapter {
  Context context;
  int layout;
  List<User> users;

  public UserAdapter(Context context, int layout, List<User> users) {
    this.context = context;
    this.layout = layout;
    this.users = users;
  }

  @Override
  public int getCount() {
    return users.size();
  }

  @Override
  public Object getItem(int i) {
    return null;
  }

  @Override
  public long getItemId(int i) {
    return 0;
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    view = inflater.inflate(layout, null);
    TextView tvName = view.findViewById(R.id.tvName);
    TextView tvEmail = view.findViewById(R.id.tvEmail);
    ImageView imgNext = view.findViewById(R.id.imgNext);

    tvName.setText(users.get(i).getName());
    tvEmail.setText(users.get(i).getEmail());
    imgNext.setImageResource(R.drawable.ic_next);
    return view;
  }
}
