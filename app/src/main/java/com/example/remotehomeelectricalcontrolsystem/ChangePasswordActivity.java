package com.example.remotehomeelectricalcontrolsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.remotehomeelectricalcontrolsystem.Model.SharedUser;
import com.example.remotehomeelectricalcontrolsystem.Model.User;
import com.example.remotehomeelectricalcontrolsystem.Utils.EncryptionUtils;
import com.example.remotehomeelectricalcontrolsystem.Utils.InputValidator;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordActivity extends AppCompatActivity {
  MaterialToolbar topAppBar;
  TextInputEditText edtPassword, edtNewPassword, edtConfirmPassword;
  Button btnSave;
  private FirebaseDatabase db;
  private DatabaseReference userPasswordRef;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_change_password);

    init();
    topAppBar.setNavigationOnClickListener(view -> finish());
    btnSave.setOnClickListener(view -> handleSubmit());
  }

  private void init() {
    topAppBar = findViewById(R.id.topAppBar);
    edtPassword = findViewById(R.id.edtPassword);
    edtNewPassword = findViewById(R.id.edtNewPassword);
    edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
    btnSave = findViewById(R.id.btnSave);
    db = FirebaseDatabase.getInstance();
  }

  private void handleSubmit() {
    String password = edtPassword.getText().toString();
    String newPassword = edtNewPassword.getText().toString();
    String confirmPassword = edtConfirmPassword.getText().toString();

    User user = SharedUser.getUser();
    if (user != null) {
      String id = user.getUserId();
      userPasswordRef = db.getReference("users/" + id + "/password");

      if (checkAllFields(password, newPassword, confirmPassword)) {
        userPasswordRef.get().addOnCompleteListener(task -> {
          if (!task.isSuccessful()) {
            Toast.makeText(this, "Error getting data", Toast.LENGTH_SHORT).show();
          }
          else {
            String passEncrypt = task.getResult().getValue(String.class);
            String pass = EncryptionUtils.decrypt(passEncrypt);
            if (pass.equals(password)) {
              userPasswordRef.setValue(EncryptionUtils.encrypt(newPassword));

              finish();
              Toast.makeText(ChangePasswordActivity.this, "Password change successful!", Toast.LENGTH_SHORT).show();
            } else {
              edtPassword.setError("Password is incorrect.");
              edtPassword.requestFocus();
            }
          }
        });
      }
    }
  }

  private boolean checkAllFields(String password, String newPassword, String confirmPassword) {
    if (password.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
      Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
      return false;
    }
    if (!InputValidator.isValidPassword(password)) {
      edtPassword.setError("Password must be at least 8 characters.");
      edtPassword.requestFocus();
      return false;
    }
    if (!InputValidator.isValidPassword(newPassword)) {
      edtNewPassword.setError("Password must be at least 8 characters.");
      edtNewPassword.requestFocus();
      return false;
    }
    if (!InputValidator.isValidPassword(confirmPassword)) {
      edtConfirmPassword.setError("Password must be at least 8 characters.");
      edtConfirmPassword.requestFocus();
      return false;
    }
    if (!newPassword.equals(confirmPassword)) {
      edtConfirmPassword.setError("New password confirmation does not match.");
      edtConfirmPassword.requestFocus();
      return false;
    }
    return true;
  }
}