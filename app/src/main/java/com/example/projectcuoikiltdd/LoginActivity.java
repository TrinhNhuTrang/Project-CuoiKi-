package com.example.projectcuoikiltdd;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.tabRegister).setOnClickListener(v -> openRegistration());
        findViewById(R.id.tvRegisterNow).setOnClickListener(v -> openRegistration());
    }

    private void openRegistration() {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
