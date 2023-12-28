package com.example.remotehomeelectricalcontrolsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.remotehomeelectricalcontrolsystem.Utils.InputValidator;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class TimerActivity extends AppCompatActivity {
  TextInputEditText edtStartTime, edtEndTime;
  Button btnSave, btnDelete;
  MaterialToolbar topAppBar;
  String devicePath;
  FirebaseDatabase db;
  DatabaseReference startTimeRef, endTimeRef, deviceRef;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timer);

    init();

    Intent intent = getIntent();
    if (intent != null) {
      devicePath = intent.getStringExtra("devicePath");
      startTimeRef = db.getReference(devicePath + "/startTime");
      endTimeRef = db.getReference(devicePath + "/endTime");
      deviceRef = db.getReference(devicePath);
      deviceRef.get().addOnCompleteListener(task -> {
        if (!task.isSuccessful()) Toast.makeText(this, "Get information error", Toast.LENGTH_SHORT).show();
        else {
          DataSnapshot dataDevice = task.getResult();
          edtStartTime.setText(dataDevice.child("startTime").getValue(String.class));
          edtEndTime.setText(dataDevice.child("endTime").getValue(String.class));
        }
      });
    }

    Calendar calendar = Calendar.getInstance();
    int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
    int minuteOfDay = calendar.get(Calendar.MINUTE);
    MaterialTimePicker pickerStartTime = new MaterialTimePicker.Builder()
        .setTheme(R.style.ThemeOverlay_PChart_Material3TimePicker)
        .setTimeFormat(TimeFormat.CLOCK_12H)
        .setHour(hourOfDay)
        .setMinute(minuteOfDay)
        .setTitleText("Select start time")
        .build();
    MaterialTimePicker pickerEndTime = new MaterialTimePicker.Builder()
        .setTheme(R.style.ThemeOverlay_PChart_Material3TimePicker)
        .setTimeFormat(TimeFormat.CLOCK_12H)
        .setHour(hourOfDay)
        .setMinute(minuteOfDay)
        .setTitleText("Select end time")
        .build();
    pickerStartTime.addOnPositiveButtonClickListener(v -> edtStartTime.setText(pickerStartTime.getHour() + ":" + pickerStartTime.getMinute()));
    pickerEndTime.addOnPositiveButtonClickListener(v -> edtEndTime.setText(pickerEndTime.getHour() + ":" + pickerEndTime.getMinute()));
    edtStartTime.setOnClickListener(v -> {
      pickerStartTime.show(getSupportFragmentManager(), "tag");
      edtStartTime.setError(null);
    });
    edtEndTime.setOnClickListener(v -> {
      pickerEndTime.show(getSupportFragmentManager(), "tag");
      edtEndTime.setError(null);
    });

    btnSave.setOnClickListener(v -> handleSubmit());
    btnDelete.setOnClickListener(v -> {
      edtStartTime.setText("");
      edtEndTime.setText("");
      startTimeRef.setValue("");
      endTimeRef.setValue("");
    });
    topAppBar.setNavigationOnClickListener(v -> finish());
  }

  private void init() {
    edtStartTime = findViewById(R.id.edtStartTime);
    edtEndTime = findViewById(R.id.edtEndTime);
    btnSave = findViewById(R.id.btnSave);
    btnDelete = findViewById(R.id.btnDelete);
    topAppBar = findViewById(R.id.topAppBar);
    db = FirebaseDatabase.getInstance();
  }

  private void handleSubmit() {
    String startTime = edtStartTime.getText().toString();
    String endTime = edtEndTime.getText().toString();

    if (checkAllFields(startTime, endTime)) {
      startTimeRef.setValue(startTime);
      endTimeRef.setValue(endTime);
    }
  }

  public boolean checkAllFields(String startTime, String endTime) {
    if (startTime.isEmpty() || endTime.isEmpty()) {
      Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
      if (startTime.isEmpty()) {
        edtStartTime.setError("Please choose a start time");
        edtStartTime.requestFocus();
        return false;
      }
      if (endTime.isEmpty()) {
        edtEndTime.setError("Please choose a start time");
        edtEndTime.requestFocus();
        return false;
      }
    }
    return true;
  }
}