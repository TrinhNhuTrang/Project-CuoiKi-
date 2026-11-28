package com.example.projectcuoikiltdd;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReviewActivity extends AppCompatActivity {

    private TextView[] stars;
    private int rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        stars = new TextView[]{
                findViewById(R.id.star1), findViewById(R.id.star2),
                findViewById(R.id.star3), findViewById(R.id.star4),
                findViewById(R.id.star5)
        };

        // Bấm vào sao thứ i -> chọn i sao
        for (int i = 0; i < stars.length; i++) {
            final int value = i + 1;
            stars[i].setOnClickListener(v -> {
                rating = value;
                updateStars();
            });
        }
    }

    private void updateStars() {
        for (int i = 0; i < stars.length; i++) {
            stars[i].setTextColor(i < rating ? Color.parseColor("#F5A623") : Color.parseColor("#C8D6C8"));
        }
    }
}