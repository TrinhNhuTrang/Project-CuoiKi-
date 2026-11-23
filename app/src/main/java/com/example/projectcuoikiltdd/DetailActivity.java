package com.example.projectcuoikiltdd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    private boolean liked = true; // Mặc định tim đỏ như ảnh gốc

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Nút tim: đổi giữa tim đỏ đặc và tim viền
        ImageView heart = findViewById(R.id.btnHeart);
        heart.setOnClickListener(v -> {
            liked = !liked;
            heart.setImageResource(liked ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
        });

        // Tab "Đánh giá" -> mở màn Đánh giá
        findViewById(R.id.tabReview).setOnClickListener(v ->
                startActivity(new Intent(DetailActivity.this, ReviewActivity.class)));

        findViewById(R.id.btnAddPlan).setOnClickListener(v ->
                Toast.makeText(this, "Đã thêm vào lịch trình", Toast.LENGTH_SHORT).show());
    }
}