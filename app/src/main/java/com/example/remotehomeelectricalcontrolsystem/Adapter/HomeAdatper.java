package com.example.remotehomeelectricalcontrolsystem.Adapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remotehomeelectricalcontrolsystem.FloorActivity1;
import com.example.remotehomeelectricalcontrolsystem.FloorActivity2;

import com.example.remotehomeelectricalcontrolsystem.Model.Floor;
import com.example.remotehomeelectricalcontrolsystem.Model.House;
import com.example.remotehomeelectricalcontrolsystem.R;

import java.util.ArrayList;
import java.util.List;

public class HomeAdatper extends RecyclerView.Adapter<HomeAdatper.ViewHolder> {
    List<Floor> listFloor = new ArrayList<>();
    public HomeAdatper() {
    }
    public void updateListFloor(List<Floor> list) {
        this.listFloor = list;
        Log.d("Success Adapter" , "Have Data In Adapter");
        Log.d("Size ListMovie" , String.valueOf(list.size()));

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeAdatper.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_floor_recycler , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdatper.ViewHolder holder, int position) {
        holder.txtNameFloor.setText(listFloor.get(position).getName());
        //holder.txtIdFloor.setText(listFloor.get(position).getId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int id = holder.getAdapterPosition();
//                String uuidString = (String) holder.itemView.getTag(position);
//                UUID uuid = UUID.fromString(uuidString);
//                if (uuid.hashCode() == id){
//                    Intent intent1 = new Intent(view.getContext(), FloorActivity1.class);
//                    view.getContext().startActivity(intent1);
//
//                }
//                switch (uuid.hashCode()) {
//                    case 1:
//                        Intent intent1 = new Intent(view.getContext(), FloorActivity1.class);
//                        view.getContext().startActivity(intent1);
//                        break;
//                    case 2:
//                        Intent intent2 = new Intent(view.getContext(), FloorActivity2.class);
//                        view.getContext().startActivity(intent2);
//                        break;
//                }
            }
        });
        holder.imgFloor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //int id = holder.getAdapterPosition();
                String idFloor = (listFloor.get(position).getId());
                Bundle bundle = new Bundle();
                //Intent intent = new Intent(view.getContext() , FloorActivity1.class);
                switch (idFloor) {
                    case "e72cf36f-f9c5-4dee-b11a-951c0e3dc638":
                        Log.i("Change Floor1" , "Ok");
                        Intent intent1 = new Intent(view.getContext(), FloorActivity1.class);
                        bundle.putString("Floor1" ,"e72cf36f-f9c5-4dee-b11a-951c0e3dc638");
                        intent1.putExtras(bundle);
                        view.getContext().startActivity(intent1);
                        break;
                    case "b93700fb-e94a-4f2f-825c-b37163786597":
                        Log.i("Change Floor2" , "Ok");
                        Intent intent2 = new Intent(view.getContext(), FloorActivity2.class);
                        bundle.putString("Floor2" ,"b93700fb-e94a-4f2f-825c-b37163786597");
                        intent2.putExtras(bundle);
                        view.getContext().startActivity(intent2);
                        break;
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return listFloor.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNameFloor;
        TextView txtIdFloor;
        ImageView imgFloor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameFloor = itemView.findViewById(R.id.txtNameFloor);
            txtIdFloor = itemView.findViewById(R.id.txtIdFloor);
            imgFloor = itemView.findViewById(R.id.imgFloor);

        }
    }
}
