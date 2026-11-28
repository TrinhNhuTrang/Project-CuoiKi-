package com.example.projectcuoikiltdd;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DiaDiemYeuThichActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia_diem_yeu_thich);

        findViewById(R.id.favoriteDiscover).setOnClickListener(v ->
                startActivity(new Intent(this, TimKiemActivity.class)));
        BottomNavigation.setup(this, findViewById(R.id.bottom_navigation), R.id.nav_favorite);
    }
}
