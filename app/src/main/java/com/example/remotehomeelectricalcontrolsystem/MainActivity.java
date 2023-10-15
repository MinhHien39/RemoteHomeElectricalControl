package com.example.remotehomeelectricalcontrolsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.remotehomeelectricalcontrolsystem.Fragment.HomeFragment;
import com.example.remotehomeelectricalcontrolsystem.Fragment.ProfileFragment;
import com.example.remotehomeelectricalcontrolsystem.databinding.ActivityMainBinding;
import com.example.remotehomeelectricalcontrolsystem.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    Fragment homeFragment = new HomeFragment();

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        int i = 1;
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.frame_layout,
                    HomeFragment.class , null).commit();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout , homeFragment).commit();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_home){
                    homeFragment = HomeFragment.newInstance();
                }else if(item.getItemId() == R.id.nav_profile){
                    homeFragment = ProfileFragment.newInstance();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout , homeFragment).commit();
                return true;
            }
        });
    }


}