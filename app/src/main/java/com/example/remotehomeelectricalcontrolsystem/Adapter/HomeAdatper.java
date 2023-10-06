package com.example.remotehomeelectricalcontrolsystem.Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remotehomeelectricalcontrolsystem.FloorActivity1;
import com.example.remotehomeelectricalcontrolsystem.FloorActivity2;
import com.example.remotehomeelectricalcontrolsystem.LoginActivity;
import com.example.remotehomeelectricalcontrolsystem.Model.Floor;
import com.example.remotehomeelectricalcontrolsystem.R;
import com.example.remotehomeelectricalcontrolsystem.SignupActivity;

import org.w3c.dom.Text;

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
        holder.txtNameFloor.setText(listFloor.get(position).getNameFloor());
        //holder.txtIdFloor.setText(listFloor.get(position).getIdFloor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idFloor = listFloor.get(position).getIdFloor();
                switch (idFloor) {
                    case 1:
                        Intent intent1 = new Intent(view.getContext(), FloorActivity1.class);
                        view.getContext().startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(view.getContext(), FloorActivity2.class);
                        view.getContext().startActivity(intent2);
                        break;
                }
            }
        });
//        holder.imgFloor.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                int id = holder.getAdapterPosition();
//                int idFloor = listFloor.get(position).getIdFloor();
//                switch (idFloor) {
//                    case 1:
//                        Intent intent1 = new Intent(view.getContext(), FloorActivity1.class);
//                        view.getContext().startActivity(intent1);
//                        break;
//                    case 2:
//                        Intent intent2 = new Intent(view.getContext(), FloorActivity2.class);
//                        view.getContext().startActivity(intent2);
//                        break;
//                }
//            }
//        });
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
