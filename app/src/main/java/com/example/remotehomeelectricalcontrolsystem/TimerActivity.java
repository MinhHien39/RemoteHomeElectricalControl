package com.example.remotehomeelectricalcontrolsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class TimerActivity extends AppCompatActivity {
  TextInputEditText edtStartTime, edtEndTime;
  Button btnSave;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timer);

    init();
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
    pickerStartTime.addOnPositiveButtonClickListener(v -> edtStartTime.setText(pickerStartTime.getHour() + " : " + pickerStartTime.getMinute()));
    pickerEndTime.addOnPositiveButtonClickListener(v -> edtEndTime.setText(pickerEndTime.getHour() + " : " + pickerEndTime.getMinute()));
    edtStartTime.setOnClickListener(v -> {
      pickerStartTime.show(getSupportFragmentManager(), "tag");
    });
    edtEndTime.setOnClickListener(v -> {
      pickerEndTime.show(getSupportFragmentManager(), "tag");
    });

    btnSave.setOnClickListener(v -> handleSubmit());
  }

  private void init() {
    edtStartTime = findViewById(R.id.edtStartTime);
    edtEndTime = findViewById(R.id.edtEndTime);
    btnSave = findViewById(R.id.btnSave);
  }

  private void handleSubmit() {

  }
}