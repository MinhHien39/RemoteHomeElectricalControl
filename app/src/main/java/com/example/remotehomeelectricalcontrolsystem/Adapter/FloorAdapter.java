package com.example.remotehomeelectricalcontrolsystem.Adapter;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
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
import com.example.remotehomeelectricalcontrolsystem.Model.Floor;
import com.example.remotehomeelectricalcontrolsystem.Model.Room;
import com.example.remotehomeelectricalcontrolsystem.R;
import com.example.remotehomeelectricalcontrolsystem.RoomActivity;

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
        holder.txtNameRoom.setText(roomList.get(position).getNameRoom());
        holder.txtCountRoom.setText(String.valueOf(roomList.get(position).getList().size()) + " " +"Device");
        //Log.i("txtCount" , String.valueOf(roomList.get(position).getList().size()));


        String url;
        switch (roomList.get(position).getNameRoom()){
            case "Living room":
                url = "https://saigonanhkiet.com/wp-content/uploads/2018/08/thiet-ke-noi-that-kieu-nhat-khong-kho.jpg";
                Glide.with(holder.itemView.getContext())
                        .load(url)
                        .into(holder.imgRoom);
                break;
            case "Kitchen room":
                url = "https://tubepthongminh.com/wp-content/uploads/2019/06/noi-that-nha-bep-nhat-1.jpg";
                Glide.with(holder.itemView.getContext())
                        .load(url)
                        .into(holder.imgRoom);
                break;
            case "Room 1":
                url = "https://www.cleanipedia.com/images/5iwkm8ckyw6v/V0dmJdHrd2rWg0ucsXlB7/f953ad2d6713fb56ec303f253eb9e56f/dGhpZXQta2UtcGhvbmctbmd1LWtpZXUtbmhhdC03LmpwZw/990w-660h/thi%E1%BA%BFt-k%E1%BA%BF-ph%C3%B2ng-ng%E1%BB%A7-ki%E1%BB%83u-nh%C3%A2t.jpg";
                Glide.with(holder.itemView.getContext())
                        .load(url)
                        .into(holder.imgRoom);
                break;
            case "Bathroom":
                url = "https://viphouse.vn/viphouse/images/2015/t11/thiet%20ke%20phong%20tam%20phong%20cach%20nhat%20ban/Thiet_ke_phong_tam_phong_cach_nhat_ban_viphouse_vn%20(5).jpg";
                Glide.with(holder.itemView.getContext())
                        .load(url)
                        .into(holder.imgRoom);
                break;
            case "Room 2":
                String url1 = "https://www.cleanipedia.com/images/5iwkm8ckyw6v/V0dmJdHrd2rWg0ucsXlB7/f953ad2d6713fb56ec303f253eb9e56f/dGhpZXQta2UtcGhvbmctbmd1LWtpZXUtbmhhdC03LmpwZw/990w-660h/thi%E1%BA%BFt-k%E1%BA%BF-ph%C3%B2ng-ng%E1%BB%A7-ki%E1%BB%83u-nh%C3%A2t.jpg";
                Glide.with(holder.itemView.getContext())
                        .load(url1)
                        .into(holder.imgRoom);
                break;
            case "Yard":
                url = "https://sanvuonadong.vn/wp-content/uploads/2020/07/san-vuon-nhat-ban-01-san-vuon-a-dong.jpg";
                Glide.with(holder.itemView.getContext())
                        .load(url)
                        .into(holder.imgRoom);

        }
        holder.imgRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Id Room " , String.valueOf(roomList.get(position).getIdRoom()));
                Log.i("txtCount" , String.valueOf(roomList.get(position).getList().size()));
                Bundle bundle = new Bundle();
                bundle.putString("keyRoom" , roomList.get(position).getIdRoom());
                Intent intent = new Intent(v.getContext() , RoomActivity.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNameRoom;
        TextView txtIdRoom;
        ImageView imgRoom;
        TextView txtCountRoom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameRoom = itemView.findViewById(R.id.txtNameRoom);
            txtIdRoom = itemView.findViewById(R.id.txtIdRoom);

            imgRoom = itemView.findViewById(R.id.imgRoom);
            txtCountRoom = itemView.findViewById(R.id.txtCountRoom);

        }
    }
}
