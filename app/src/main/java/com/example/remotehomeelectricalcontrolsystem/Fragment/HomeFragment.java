package com.example.remotehomeelectricalcontrolsystem.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.remotehomeelectricalcontrolsystem.Adapter.HomeAdatper;
import com.example.remotehomeelectricalcontrolsystem.Model.Floor;
import com.example.remotehomeelectricalcontrolsystem.R;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    RecyclerView rec_home;
    HomeAdatper homeAdatper;
    List<Floor> floorList;

    public static HomeFragment newInstance(){
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        FragmentManager fm = this.getChildFragmentManager();
        rec_home = root.findViewById(R.id.rec_floor);
        rec_home.setLayoutManager(new GridLayoutManager(this.getContext() , 2));
        rec_home.scrollToPosition(1000);
        homeAdatper = new HomeAdatper();
        floorList = new ArrayList<>();
        floorList.add(new Floor(1 , "Floor 1"));
        floorList.add(new Floor(2 , "Floor 2"));
        floorList.add(new Floor(3 , "Floor 3"));
        homeAdatper.updateListFloor(floorList);
        homeAdatper.notifyDataSetChanged();
        rec_home.setAdapter(homeAdatper);
        return root;
    }
}