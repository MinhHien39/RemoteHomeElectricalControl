package com.example.remotehomeelectricalcontrolsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remotehomeelectricalcontrolsystem.Model.SharedUser;
import com.example.remotehomeelectricalcontrolsystem.Model.UserHouse;
import com.example.remotehomeelectricalcontrolsystem.Utils.EncryptionUtils;
import com.example.remotehomeelectricalcontrolsystem.Utils.Format;
import com.example.remotehomeelectricalcontrolsystem.Utils.InputValidator;
import com.example.remotehomeelectricalcontrolsystem.Model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class SignupActivity extends AppCompatActivity {
  private FirebaseDatabase db;
  private DatabaseReference usersRef, housesRef, usersHousesRef;
  TextInputEditText edtFullName, edtEmail, edtTelephone, edtHouseKey, edtPassword;
  TextView txtLogin;
  Button btnSignUp;
  String role, houseId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);

    init();

    btnSignUp.setOnClickListener(v -> {
      String email = edtEmail.getText().toString();
      String password = edtPassword.getText().toString();
      String fullName = edtFullName.getText().toString();
      String houseKey = edtHouseKey.getText().toString();
      String telephone = edtTelephone.getText().toString();


      if (checkAllFields(fullName, email, telephone, houseKey, password)) {
        checkRegisteredAccount(email, houseKey, fullName, telephone, password);
      }
    });

    txtLogin.setOnClickListener(v -> {
      Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
      startActivity(intent);
    });
  }

  public void init() {
    edtFullName = findViewById(R.id.edtName);
    edtEmail = findViewById(R.id.edtEmail);
    edtPassword = findViewById(R.id.edtPassword);
    edtHouseKey = findViewById(R.id.edtHouseKey);
    edtTelephone = findViewById(R.id.edtTelephone);
    txtLogin = findViewById(R.id.txtLogin);
    btnSignUp = findViewById(R.id.btnSignUp);
    db = FirebaseDatabase.getInstance();
    usersRef = db.getReference("users");
    housesRef = db.getReference("test1");
    usersHousesRef = db.getReference("usersHouses");
  }

  public boolean checkAllFields(String name, String email, String telephone, String houseKey, String password) {
    if (name.isEmpty() || telephone.isEmpty() || email.isEmpty() || password.isEmpty() || houseKey.isEmpty()) {
      Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
      return false;
    }
    if (!InputValidator.isValidFullName(name)) {
      edtFullName.setError("Please enter a valid full name.");
      edtFullName.requestFocus();
      return false;
    }
    if (!InputValidator.isValidPhoneNumber(telephone)) {
      edtTelephone.setError("Please enter a valid phone number.");
      edtTelephone.requestFocus();
      return false;
    }
    if (!InputValidator.isValidEmail(email)) {
      edtEmail.setError("Please enter a valid email address.");
      edtEmail.requestFocus();
      return false;
    }
    if (!InputValidator.isValidPassword(password)) {
      edtPassword.setError("Password must be at least 8 characters.");
      edtPassword.requestFocus();
      return false;
    }
    return true;
  }

  public void checkRegisteredAccount(String emailToCheck, String houseKeyToCheck, String fullName, String telephone, String password) {
    String encryptHouseKey = EncryptionUtils.encrypt(houseKeyToCheck);

    housesRef.orderByChild("houseKey").equalTo(encryptHouseKey).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists()) {
          for (DataSnapshot house : snapshot.getChildren()) {
            houseId = house.getKey();
            String emailHost = house.child("emailHost").getValue(String.class);
            role = emailToCheck.equals(emailHost) ? "host" : "member";
            usersRef.orderByChild("email").equalTo(emailToCheck).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                  for (DataSnapshot user : snapshot.getChildren()) {
                    String userId = user.getKey();
                    usersHousesRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                          boolean isRegistered = false;
                          for (DataSnapshot userHouse : snapshot.getChildren()) {
                            String userIdToCheck = userHouse.child("userId").getValue(String.class).toString();
                            String houseIdToCheck = userHouse.child("houseId").getValue(String.class).toString();
                            if (userIdToCheck.equals(userId) && houseIdToCheck.equals(houseId)) {
                              Toast.makeText(SignupActivity.this, "Account already exists!", Toast.LENGTH_LONG).show();
                              edtHouseKey.setError("Please re-enter another house key");
                              edtHouseKey.requestFocus();
                              isRegistered = true;
                              break;
                            }
                          }
                          if (!isRegistered) {
                            continueRegistration(userId, fullName, emailToCheck, telephone, password);
                          }
                        } else {
                          continueRegistration(userId, fullName, emailToCheck, telephone, password);
                        }
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {

                      }
                    });
                  }
                } else {
                  continueRegistration(null, fullName, emailToCheck, telephone, password);
                }
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {
                // Handle database errors here
              }
            });
          }
        } else {
          edtHouseKey.setError("House key does not exist!");
          edtHouseKey.requestFocus();
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
      }
    });
  }

  private void continueRegistration(String userId, String fullName, String email, String telephone, String password) {
    String encryptPass = EncryptionUtils.encrypt(password);

    if (userId == null) {
      userId = UUID.randomUUID().toString();
      writeNewUser(userId, fullName, email, telephone, encryptPass);
    }
    Log.d("aaa", "continueRegistration()");
    writeNewUserHouse(userId, fullName, email, telephone);
    moveScreen(SignupActivity.this, LoginActivity.class);
  }

  public void writeNewUser(String userId, String name, String email, String telephone, String password) {
    name = Format.formatName(name);
    User user = new User(null, name, email, telephone, password);
    usersRef.child(userId).setValue(user);
  }

  public void writeNewUserHouse(String userId, String name, String email, String telephone) {
    UserHouse userHouse = new UserHouse(null, userId, houseId, role);
    usersHousesRef.child(UUID.randomUUID().toString()).setValue(userHouse);
    Toast.makeText(SignupActivity.this, "Successful account registration!", Toast.LENGTH_LONG).show();
    String password = edtPassword.getText().toString();
    User user = new User(userId, name, email, telephone, password);
    SharedUser.setUser(user);
  }

  public void moveScreen(Activity currentScreen, Class<? extends Activity> nextScreenClass) {
    Intent intent = new Intent(currentScreen, nextScreenClass);
    startActivity(intent);
  }
}