package com.example.remotehomeelectricalcontrolsystem.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remotehomeelectricalcontrolsystem.FloorActivity;

import com.example.remotehomeelectricalcontrolsystem.Model.Floor;
import com.example.remotehomeelectricalcontrolsystem.R;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    List<Floor> listFloor = new ArrayList<>();
    private String housePath;

    public HomeAdapter() {
    }

    public String getHousePath() {
        return housePath;
    }

    public void setHousePath(String housePath) {
        this.housePath = housePath;
    }

    public void updateListFloor(List<Floor> list) {
        this.listFloor = list;
        Log.d("Success Adapter" , "Have Data In Adapter");
        Log.d("Size ListMovie" , String.valueOf(list.size()));

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_floor_recycler , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
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
<<<<<<< HEAD:app/src/main/java/com/example/remotehomeelectricalcontrolsystem/Adapter/HomeAdatper.java

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
                    case "40b38a85-36ea-4a72-921a-2fbb579a2daf":
                        Log.i("Change Floor2" , "Ok");
                        Intent intent2 = new Intent(view.getContext(), FloorActivity2.class);
                        bundle.putString("Floor2" ,"40b38a85-36ea-4a72-921a-2fbb579a2daf");
                        intent2.putExtras(bundle);
                        view.getContext().startActivity(intent2);
                        break;
                }
=======
                String floorId = (listFloor.get(position).getId());
                Intent intent = new Intent(view.getContext() , FloorActivity.class);
                intent.putExtra("floorPath", housePath + "/floors/" + floorId);
                view.getContext().startActivity(intent);
>>>>>>> d642809 (C6.4 Update Code V1.3):app/src/main/java/com/example/remotehomeelectricalcontrolsystem/Adapter/HomeAdapter.java
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
