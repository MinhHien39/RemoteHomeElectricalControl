package com.example.remotehomeelectricalcontrolsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.remotehomeelectricalcontrolsystem.Model.SharedUser;
import com.example.remotehomeelectricalcontrolsystem.Model.User;
import com.example.remotehomeelectricalcontrolsystem.Model.UserHouse;
import com.example.remotehomeelectricalcontrolsystem.Utils.EncryptionUtils;
import com.example.remotehomeelectricalcontrolsystem.Utils.Format;
import com.example.remotehomeelectricalcontrolsystem.Utils.InputValidator;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class UserActivity extends AppCompatActivity {
  private FirebaseDatabase db;
  private DatabaseReference usersRef, housesRef, usersHousesRef;
  private DatabaseReference userRef, houseRef, userHouseRef;
  LinearLayout linearUpdateDelete, linearSubmit;
  TextInputEditText edtFullName, edtEmail, edtTelephone, edtHouseKey, edtPassword;
  MaterialToolbar topAppBar;
  Button btnAdd, btnDelete, btnUpdate;
  AutoCompleteTextView autoCompleteRole;
  String[] roles = new String[]{"admin", "host", "member"};
  ArrayAdapter<String> adapterRoles;
  String role, houseId, userId, userHouseId;
  Bundle bundle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user);

    init();

    bundle = getIntent().getExtras();

    if (bundle != null) {
      userHouseId = bundle.getString("userHouseId");
      userHouseRef = db.getReference("usersHouses/" + userHouseId);
      userHouseRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot userHouse) {
          role = userHouse.child("role").getValue(String.class);
          setRole(role);
          houseId = userHouse.child("houseId").getValue(String.class);
          userId = userHouse.child("userId").getValue(String.class);
          userRef = db.getReference("users/" + userId);
          houseRef = db.getReference("test1/" + houseId);
          userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot user) {
              edtFullName.setText(user.child("name").getValue(String.class));
              edtEmail.setText(user.child("email").getValue(String.class));
              edtTelephone.setText(user.child("tel").getValue(String.class));
              String hashPass = user.child("password").getValue(String.class);
              String password = EncryptionUtils.decrypt(hashPass);
              edtPassword.setText(password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
          });
          houseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot house) {
              String hashKey = house.child("houseKey").getValue(String.class);
              String houseKey = EncryptionUtils.decrypt(hashKey);
              edtHouseKey.setText(houseKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
          });
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
      });
      showUpdateDelete();
    } else {
      showSubmit();
    }
    topAppBar.setNavigationOnClickListener(view -> {
      finish();
    });
    btnAdd.setOnClickListener(v -> handleSubmit());
    btnUpdate.setOnClickListener(v -> handleSubmit());
    btnDelete.setOnClickListener(view -> {
      MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(UserActivity.this)
          .setTitle("Are you sure you want to delete this user?")
          .setPositiveButton("Yes", (dialogInterface, i) -> {
            usersHousesRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot usersHouses) {
                if (usersHouses.getChildrenCount() == 1) {
                  userRef.removeValue();
                }
                userHouseRef.removeValue((error, ref) -> {
                  Toast.makeText(UserActivity.this, "Delete user success!", Toast.LENGTH_SHORT).show();
                  moveScreen(UserActivity.this, AdminActivity.class);
                });
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
            });
            dialogInterface.dismiss();
          })
          .setNegativeButton("No", (dialogInterface, i) -> {
            dialogInterface.dismiss();
          });

      dialog.show();
    });
  }

  public void showUpdateDelete() {
    linearUpdateDelete.setVisibility(View.VISIBLE);
    edtEmail.setEnabled(false);
  }

  public void showSubmit() {
    linearSubmit.setVisibility(View.VISIBLE);
  }

  public void init() {
    topAppBar = findViewById(R.id.topAppBar);
    edtFullName = findViewById(R.id.edtName);
    edtEmail = findViewById(R.id.edtEmail);
    edtPassword = findViewById(R.id.edtPassword);
    edtHouseKey = findViewById(R.id.edtHouseKey);
    edtTelephone = findViewById(R.id.edtTel);
    btnAdd = findViewById(R.id.btnAdd);
    btnDelete = findViewById(R.id.btnDelete);
    btnUpdate = findViewById(R.id.btnUpdate);
    db = FirebaseDatabase.getInstance();
    usersRef = db.getReference("users");
    housesRef = db.getReference("test1");
    usersHousesRef = db.getReference("usersHouses");
    linearUpdateDelete = findViewById(R.id.linearUpdateDelete);
    linearSubmit = findViewById(R.id.linearSubmit);
    autoCompleteRole = findViewById(R.id.autoCompleteRole);
    adapterRoles = new ArrayAdapter<>(this, R.layout.list_item, roles);
    autoCompleteRole.setAdapter(adapterRoles);
    autoCompleteRole.setText(adapterRoles.getItem(2), false);
  }

  public void setRole(@NonNull String role) {
    switch (role) {
      case "admin":
        autoCompleteRole.setText(adapterRoles.getItem(0), false);
        break;
      case "host":
        autoCompleteRole.setText(adapterRoles.getItem(1), false);
        break;
      case "member":
        autoCompleteRole.setText(adapterRoles.getItem(2), false);
        break;
    }
  }

  public void handleSubmit() {
    String email = edtEmail.getText().toString();
    String password = edtPassword.getText().toString();
    String fullName = edtFullName.getText().toString();
    String houseKey = edtHouseKey.getText().toString();
    String telephone = edtTelephone.getText().toString();
    role = autoCompleteRole.getText().toString();


    if (checkAllFields(fullName, email, telephone, houseKey, password)) {
      checkRegisteredAccount(email, houseKey, fullName, telephone, password);
    }
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
                            if (bundle != null) {
                              break;
                            } else {
                              if (userIdToCheck.equals(userId) && houseIdToCheck.equals(houseId)) {
                                Toast.makeText(UserActivity.this, "Account already exists!", Toast.LENGTH_LONG).show();
                                edtHouseKey.setError("Please re-enter another house key");
                                edtHouseKey.requestFocus();
                                isRegistered = true;
                                break;
                              }
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
    if (bundle != null) {
      writeNewUser(userId, fullName, email, telephone, encryptPass);
    }
    if (userId == null) {
      userId = UUID.randomUUID().toString();
      writeNewUser(userId, fullName, email, telephone, encryptPass);
    }
    Log.d("aaa", "continueRegistration()");
    writeNewUserHouse(userId, fullName, email, telephone);
    moveScreen(UserActivity.this, AdminActivity.class);
  }

  public void writeNewUser(String userId, String name, String email, String telephone, String password) {
    name = Format.formatName(name);
    User user = new User(null, name, email, telephone, password);
    if (bundle != null) {
      DatabaseReference userRef = db.getReference("users/" + userId);
      userRef.setValue(user);
    } else {
      usersRef.child(userId).setValue(user);
    }
  }

  public void writeNewUserHouse(String userId, String name, String email, String telephone) {
    UserHouse userHouse = new UserHouse(null, userId, houseId, role);
    if (bundle != null) {
      DatabaseReference userHouseRef = db.getReference("usersHouses/" + userHouseId);
      userHouseRef.setValue(userHouse);
      Toast.makeText(UserActivity.this, "Account successfully updated!", Toast.LENGTH_LONG).show();
    } else {
      usersHousesRef.child(UUID.randomUUID().toString()).setValue(userHouse);
      String password = edtPassword.getText().toString();
      Toast.makeText(UserActivity.this, "Account successfully created!", Toast.LENGTH_LONG).show();
      User user = new User(userId, name, email, telephone, password);
      SharedUser.setUser(user);
    }
  }

  public void moveScreen(Activity currentScreen, Class<? extends Activity> nextScreenClass) {
    Intent intent = new Intent(currentScreen, nextScreenClass);
    startActivity(intent);
  }
}