package com.example.remotehomeelectricalcontrolsystem.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remotehomeelectricalcontrolsystem.Model.Floor;
import com.example.remotehomeelectricalcontrolsystem.Model.Room;
import com.example.remotehomeelectricalcontrolsystem.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class FloorAdapter extends RecyclerView.Adapter<FloorAdapter.ViewHolder>{

    List<Room> roomList = new ArrayList<>();
    public void updateListRoom(List<Room> list) {
        this.roomList = list;
        Log.d("Success Adapter" , "Have Data In Adapter");
        Log.d("Size ListMovie" , String.valueOf(list.size()));

        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public FloorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FloorAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_recycler , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull FloorAdapter.ViewHolder holder, int position) {
        holder.txtNameRoom.setText(roomList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNameRoom;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameRoom = itemView.findViewById(R.id.txtNameRoom);
        }
    }
}
