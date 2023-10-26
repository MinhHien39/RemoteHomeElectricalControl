package com.example.remotehomeelectricalcontrolsystem.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.remotehomeelectricalcontrolsystem.Model.Devices;
import com.example.remotehomeelectricalcontrolsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
    List<Devices> devicesList = new ArrayList<>();
    public void updateDeviceList(List<Devices> devicesList){
        this.devicesList = devicesList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoomAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_recycler , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.ViewHolder holder, int position) {
        holder.txtNameDevice.setText(devicesList.get(position).getNameDevice());
        String url;

        switch (devicesList.get(position).getNameDevice()){
            case "Fan" :
                url = "https://quattrantamanh.com/wp-content/uploads/2022/03/1330..jpg";
                Glide.with(holder.itemView.getContext())
                        .load(url)
                        .into(holder.imgDevice);
                break;
            case "Light 1":
                url = "https://bizweb.dktcdn.net/100/116/589/products/led-bulb-day-toc-rang-dong.png?v=1617004880860";
                Glide.with(holder.itemView.getContext())
                        .load(url)
                        .into(holder.imgDevice);
                break;
            case "Light 2":
                String url1= "https://bizweb.dktcdn.net/100/116/589/products/led-bulb-day-toc-rang-dong.png?v=1617004880860";
                Glide.with(holder.itemView.getContext())
                        .load(url1)
                        .into(holder.imgDevice);
                break;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("test1/floors/rooms/devices/state");

    }

    @Override
    public int getItemCount() {
        return devicesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNameDevice;
        Switch aSwitch;
        ImageView imgDevice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameDevice = itemView.findViewById(R.id.txtNameDevice);
            imgDevice = itemView.findViewById(R.id.imgDevice);

        }
    }
}
