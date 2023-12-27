package com.example.remotehomeelectricalcontrolsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;

public class ContactActivity extends AppCompatActivity {
  TextView tvPhoneHien, tvPhoneDinh, tvMailHien, tvMailDinh;
  MaterialToolbar topAppBar;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contact);

    init();

    topAppBar.setNavigationOnClickListener(view -> finish());
    tvPhoneHien.setOnClickListener(view -> handlePhoneClick(tvPhoneHien.getText().toString()));
    tvPhoneDinh.setOnClickListener(view -> handlePhoneClick(tvPhoneDinh.getText().toString()));
    tvMailHien.setOnClickListener(view -> handleMailClick(tvMailHien.getText().toString()));
    tvMailDinh.setOnClickListener(view -> handleMailClick(tvMailDinh.getText().toString()));
  }

  private void handlePhoneClick(String phone){
    String phoneNumber = "tel:" + phone;
    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
    startActivity(dialIntent);
  }

  private void handleMailClick(String mail) {
    String email = "mailto:" + mail;
    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(email));
    startActivity(emailIntent);
  }

  private void init() {
    topAppBar = findViewById(R.id.topAppBar);
    tvPhoneHien = findViewById(R.id.tvPhoneHien);
    tvPhoneDinh = findViewById(R.id.tvPhoneDinh);
    tvMailHien = findViewById(R.id.tvMailHien);
    tvMailDinh = findViewById(R.id.tvMailDinh);
  }
}