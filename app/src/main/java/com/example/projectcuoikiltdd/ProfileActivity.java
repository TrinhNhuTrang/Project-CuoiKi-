package com.example.projectcuoikiltdd;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Window window = getWindow();
        window.setStatusBarColor(Color.WHITE);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        View navHome = findViewById(R.id.navHome);
        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            });
        }

        View navPlace = findViewById(R.id.navPlace);
        if (navPlace != null) {
            navPlace.setOnClickListener(v -> {
                Intent intent = new Intent(this, TimKiemActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0);
            });
        }

        View navItinerary = findViewById(R.id.navItinerary);
        if (navItinerary != null) {
            navItinerary.setOnClickListener(v -> {
                startActivity(new Intent(this, LichTrinhActivity.class));
                overridePendingTransition(0, 0);
            });
        }

        View navFavorite = findViewById(R.id.navFavorite);
        if (navFavorite != null) {
            navFavorite.setOnClickListener(v -> {
                startActivity(new Intent(this, DiaDiemYeuThichActivity.class));
                overridePendingTransition(0, 0);
            });
        }
    }
}
