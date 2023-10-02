package com.example.remotehomeelectricalcontrolsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity {
    EditText edtPassword;
    ImageView imgPassword;

    TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edtPassword = findViewById(R.id.edtPassword);
        imgPassword = findViewById(R.id.imgPassword);
        txtLogin = findViewById(R.id.txtLogin);
        imgPassword.setImageResource(R.drawable.download);
        imgPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgPassword.setImageResource(R.drawable.download);
                }else{
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgPassword.setImageResource(R.drawable.baseline_remove_red_eye_24);
                }
            }
        });
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this , LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}