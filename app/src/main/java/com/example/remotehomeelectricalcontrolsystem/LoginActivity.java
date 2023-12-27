package com.example.remotehomeelectricalcontrolsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remotehomeelectricalcontrolsystem.Model.SharedUser;
import com.example.remotehomeelectricalcontrolsystem.Model.User;
import com.example.remotehomeelectricalcontrolsystem.Utils.EncryptionUtils;
import com.example.remotehomeelectricalcontrolsystem.Utils.InputValidator;
import com.example.remotehomeelectricalcontrolsystem.Utils.NetworkChangeListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
  private FirebaseDatabase db;
  private DatabaseReference usersRef;
  TextView txtSignup;
  TextInputEditText edtEmail, edtPassword;
  CheckBox cbRememberMe;
  Button btnLogin;
  User user = null;
  NetworkChangeListener networkChangeListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    init();

    autoFillCredentials();
    autoFillLoginForm();

    btnLogin.setOnClickListener(view -> {
      handleLogin();
    });

    txtSignup.setOnClickListener(v -> moveScreen(LoginActivity.this, SignupActivity.class));
  }

  private void handleLogin() {
    String email = edtEmail.getText().toString();
    String password = edtPassword.getText().toString();
    if (checkAllFields(email, password))
      isUserExist(email, password);
  }

  public void init() {
    txtSignup = findViewById(R.id.txtSignup);
    edtEmail = findViewById(R.id.edtEmail);
    edtPassword = findViewById(R.id.edtPassword);
    cbRememberMe = findViewById(R.id.cbRememberMe);
    btnLogin = findViewById(R.id.btnLogin);
    db = FirebaseDatabase.getInstance();
    usersRef = db.getReference("users");
    networkChangeListener = new NetworkChangeListener();
  }

  public void saveCredentials(String email, String password) {
    SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();

    if (cbRememberMe.isChecked()) {
      editor.putString("email", email);
      editor.putString("password", password);
      editor.putBoolean("rememberMe", true);
    } else {
      editor.clear();
    }
    editor.apply();
  }

  public void autoFillCredentials() {
    SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

    boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);

    if (rememberMe) {
      String savedUsername = sharedPreferences.getString("email", "");
      String savedPassword = sharedPreferences.getString("password", "");

      // Fill in the saved username and password
      edtEmail.setText(savedUsername);
      edtPassword.setText(savedPassword);
      cbRememberMe.setChecked(true);
//      handleLogin();
    }
  }

  public void clearCredentials() {
    SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.clear();
    editor.apply();
  }

  public boolean checkAllFields(String email, String password) {
    if (email.isEmpty() || password.isEmpty()) {
      Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
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

  public void isUserExist(String emailToCheck, String passwordToCheck) {
    btnLogin.setEnabled(false);
    String encryptPass = EncryptionUtils.encrypt(passwordToCheck);
    usersRef.orderByChild("email").equalTo(emailToCheck).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
          for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
            String userId = userSnapshot.getKey();
            String name = userSnapshot.child("name").getValue(String.class);
            String email = userSnapshot.child("email").getValue(String.class);
            String password = userSnapshot.child("password").getValue(String.class);
            if (!password.equals(encryptPass)) {
              Toast.makeText(LoginActivity.this, "Email or password is incorrect.", Toast.LENGTH_LONG).show();
            } else {
              user = new User(userId, name, email, "", "");
              Log.d("aaa", user.toString());
              if (cbRememberMe.isChecked()) {
                saveCredentials(email, passwordToCheck);
              } else {
                clearCredentials();
              }
              Toast.makeText(LoginActivity.this, "Successful login", Toast.LENGTH_SHORT).show();
              moveScreen(LoginActivity.this, MainActivity.class, user);
            }
          }
        } else {
          Toast.makeText(LoginActivity.this, "Email or password is incorrect", Toast.LENGTH_LONG).show();
        }
        btnLogin.setEnabled(true);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        // Handle any database errors
      }
    });
  }

  public void moveScreen(Activity currentScreen, Class<? extends Activity> nextScreenClass) {
    Intent intent = new Intent(currentScreen, nextScreenClass);
    startActivity(intent);
    finish();
  }

  public void moveScreen(Activity currentScreen, Class<? extends Activity> nextScreenClass, User user) {
    SharedUser.setUser(user);
    Intent intent = new Intent(currentScreen, nextScreenClass);
    startActivity(intent);
    finish();
  }

  public void autoFillLoginForm() {
    user = SharedUser.getUser();
    if (user != null) {
      edtEmail.setText(user.getEmail());
      edtPassword.setText(user.getPassword());
    }
  }
  @Override
  protected void onStart() {
    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    registerReceiver(networkChangeListener, filter);
    super.onStart();
  }
  @Override
  protected void onStop() {
    unregisterReceiver(networkChangeListener);
    super.onStop();
  }
}