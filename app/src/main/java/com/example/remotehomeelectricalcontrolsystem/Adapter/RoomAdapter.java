package com.example.remotehomeelectricalcontrolsystem.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.remotehomeelectricalcontrolsystem.Model.Devices;
import com.example.remotehomeelectricalcontrolsystem.R;

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
        String url = devicesList.get(position).getImgUrl();
        Glide.with(holder.itemView.getContext())
                .load(url)
                .into(holder.imgDevice);
    }

    @Override
    public int getItemCount() {
        return devicesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNameDevice;
        ImageView imgDevice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameDevice = itemView.findViewById(R.id.txtNameDevice);
            imgDevice = itemView.findViewById(R.id.imgDevice);
        }
    }
}
