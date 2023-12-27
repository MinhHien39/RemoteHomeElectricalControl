package com.example.remotehomeelectricalcontrolsystem;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.remotehomeelectricalcontrolsystem.Model.SharedUser;
import com.example.remotehomeelectricalcontrolsystem.Model.User;
import com.example.remotehomeelectricalcontrolsystem.Utils.EncryptionUtils;
import com.example.remotehomeelectricalcontrolsystem.Utils.InputValidator;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {
  MaterialToolbar topAppBar;
  TextInputEditText edtFullName, edtEmail, edtTelephone, edtPassword;
  Button btnSave;
  private FirebaseDatabase db;
  private DatabaseReference userRef;
  String userId;
  User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

    init();
    user = SharedUser.getUser();
    if (user != null) {
      userId = user.getUserId();
      userRef = db.getReference("users/" + userId);
      userRef.get().addOnCompleteListener(task -> {
        if (!task.isSuccessful()) {
          Toast.makeText(this, "Error getting data", Toast.LENGTH_SHORT).show();
        } else {
          if (task.getResult().getValue() != null) {
            DataSnapshot data = task.getResult();
            edtFullName.setText(data.child("name").getValue(String.class));
            edtEmail.setText(data.child("email").getValue(String.class));
            edtTelephone.setText(data.child("tel").getValue(String.class));
            edtPassword.setText(EncryptionUtils.decrypt(data.child("password").getValue(String.class)));
          }
        }
      });
    }


    topAppBar.setNavigationOnClickListener(view -> finish());
    btnSave.setOnClickListener(view -> handleSubmit());
  }

  private void init() {
    topAppBar = findViewById(R.id.topAppBar);
    edtFullName = findViewById(R.id.edtName);
    edtEmail = findViewById(R.id.edtEmail);
    edtPassword = findViewById(R.id.edtPassword);
    edtTelephone = findViewById(R.id.edtTel);
    btnSave = findViewById(R.id.btnSave);
    db = FirebaseDatabase.getInstance();
  }

  private void handleSubmit() {
    String fullName = edtFullName.getText().toString();
    String phoneNumber = edtTelephone.getText().toString();
    String email = edtEmail.getText().toString();
    String password = edtPassword.getText().toString();

    if (user != null) {

      if (checkAllFields(fullName, phoneNumber)) {
        userRef.get().addOnCompleteListener(task -> {
          if (!task.isSuccessful()) {
            Toast.makeText(this, "Error getting data", Toast.LENGTH_SHORT).show();
          } else {
            userRef.setValue(new User(null, fullName, email, phoneNumber, EncryptionUtils.encrypt(password)));

            finish();
            Toast.makeText(ProfileActivity.this, "Saved information successfully!", Toast.LENGTH_SHORT).show();
          }
        });
      }
    }
  }

  private boolean checkAllFields(String fullName, String phoneNumber) {
    if (fullName.isEmpty() || phoneNumber.isEmpty()) {
      Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
      return false;
    }
    if (!InputValidator.isValidFullName(fullName)) {
      edtFullName.setError("Please enter a valid full name.");
      edtFullName.requestFocus();
      return false;
    }
    if (!InputValidator.isValidPhoneNumber(phoneNumber)) {
      edtTelephone.setError("Please enter a valid phone number.");
      edtTelephone.requestFocus();
      return false;
    }
    return true;
  }
}