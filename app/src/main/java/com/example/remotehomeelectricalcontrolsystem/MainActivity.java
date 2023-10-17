package com.example.remotehomeelectricalcontrolsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.remotehomeelectricalcontrolsystem.Fragment.HomeFragment;
import com.example.remotehomeelectricalcontrolsystem.Fragment.ProfileFragment;
import com.example.remotehomeelectricalcontrolsystem.Model.SharedUser;
import com.example.remotehomeelectricalcontrolsystem.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    Fragment homeFragment = new HomeFragment();

    BottomNavigationView bottomNavigationView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    User user = SharedUser.getUser();
    if (user != null) {
    } else {
      Intent intent = new Intent(MainActivity.this, LoginActivity.class);
      startActivity(intent);
      finish();
    }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
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