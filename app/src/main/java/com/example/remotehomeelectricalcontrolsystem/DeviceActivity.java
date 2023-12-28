package com.example.remotehomeelectricalcontrolsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeviceActivity extends AppCompatActivity {
  MaterialSwitch aSwitch;
  MaterialToolbar topAppBar;
  Slider slider;
  TextView tvCount, tvUsages;
  ImageView imgDevice;
  MaterialCardView cardViewTimer;
  String devicePath;
  FirebaseDatabase db;
  DatabaseReference stateDeviceRef, countDeviceRef, usagesDeviceRef, deviceRef;
  float count = 0, usages = 0;
  private Handler handler = new Handler();
  private final int UPDATE_INTERVAL = 1000;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_device);

    init();

    Intent intent = getIntent();
    if (intent != null) {
      devicePath = intent.getStringExtra("devicePath");
      Log.d("aaa", "devicePath" + devicePath);
      deviceRef = db.getReference(devicePath);
      stateDeviceRef = db.getReference(devicePath + "/state");
      countDeviceRef = db.getReference(devicePath + "/count");
      usagesDeviceRef = db.getReference(devicePath + "/usages");
      deviceRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          String title = snapshot.child("name").getValue(String.class);
          int state = snapshot.child("state").getValue(Integer.class);
          count = snapshot.child("count").getValue(Float.class);
          usages = snapshot.child("usages").getValue(Float.class);
          tvCount.setText(String.format("%.0f", count) + (count > 1 ? " times" : " time"));
          tvUsages.setText(formatTime(usages));
          if (title.contains("Fan")) {
            slider.setVisibility(View.VISIBLE);
            slider.setValue(state);
            aSwitch.setVisibility(View.GONE);
            imgDevice.setImageResource(R.drawable.img_fan);
            Toast.makeText(DeviceActivity.this, "The fan is running at level: " + state, Toast.LENGTH_SHORT).show();
          } else {
            slider.setVisibility(View.GONE);
            aSwitch.setVisibility(View.VISIBLE);
            if (state == 0) {
              aSwitch.setChecked(false);
              aSwitch.setText("OFF");
              imgDevice.setImageResource(R.drawable.led_off);
            } else {
              aSwitch.setChecked(true);
              aSwitch.setText("ON");
              imgDevice.setImageResource(R.drawable.led_on);
            }
          }
          topAppBar.setTitle(title);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
      });
    }

    aSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
      if (isChecked) {
        stateDeviceRef.setValue(1);
        imgDevice.setImageResource(R.drawable.led_on);
        aSwitch.setText("ON");
        Toast.makeText(this, "The light is \"ON\"", Toast.LENGTH_SHORT).show();
        startUsagesUpdateTask();
      } else {
        stopUsagesUpdateTask();
        stateDeviceRef.setValue(0);
        imgDevice.setImageResource(R.drawable.led_off);
        aSwitch.setText("OFF");
        Toast.makeText(this, "The light is \"OFF\"", Toast.LENGTH_SHORT).show();
        countDeviceRef.setValue(count + 1);
      }
    });

    slider.addOnChangeListener((slider1, value, fromUser) -> {
      stateDeviceRef.setValue(value);
    });

    topAppBar.setNavigationOnClickListener(view -> finish());
    cardViewTimer.setOnClickListener(v -> {
      Intent intent1 = new Intent(DeviceActivity.this, TimerActivity.class);
      intent1.putExtra("devicePath", devicePath);
      startActivity(intent1);
    });
  }

  public void init() {
    aSwitch = findViewById(R.id.sw);
    db = FirebaseDatabase.getInstance();
    topAppBar = findViewById(R.id.topAppBar);
    imgDevice = findViewById(R.id.imgDevice);
    slider = findViewById(R.id.slider);
    tvCount = findViewById(R.id.tvCount);
    tvUsages = findViewById(R.id.tvUsages);
    cardViewTimer = findViewById(R.id.cardViewTimer);
  }

  private void startUsagesUpdateTask() {
    handler.postDelayed(usagesUpdateRunnable, UPDATE_INTERVAL);
  }

  private void stopUsagesUpdateTask() {
    handler.removeCallbacks(usagesUpdateRunnable);
  }

  private Runnable usagesUpdateRunnable = new Runnable() {
    @Override
    public void run() {
      usagesDeviceRef.setValue(usages + 1);
      handler.postDelayed(this, UPDATE_INTERVAL);
    }
  };

  private String formatTime(float seconds) {
    if (seconds < 60) {
      return String.format("%.0f", seconds) + "s";
    } else if (seconds < 3600) {
      float minutes = seconds / 60;
      return String.format("%.1f", minutes) + "m";
    } else {
      float hours = seconds / 3600;
      return String.format("%.1f", hours) + "h";
    }
  }
}