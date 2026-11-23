package com.example.projectcuoikiltdd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.projectcuoikiltdd.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // 1. GẮN SỰ KIỆN MỞ MÀN CHI TIẾT CHO CÁC THẺ Ở TRANG CHỦ
        View.OnClickListener openDetail = v ->
                startActivity(new Intent(MainActivity.this, DetailActivity.class));

        binding.cardHero.setOnClickListener(openDetail);
        binding.cardRuong.setOnClickListener(openDetail);
        binding.cardTrangAn.setOnClickListener(openDetail);

        // Mặc định hiển thị giao diện Trang chủ khi vừa mở app
        showHomeTab();

        // 2. LẮNG NGHE KHI BẤM CÁC TAB TRÊN THANH TABBAR
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_itinerary) {
                startActivity(new Intent(this, LichTrinhActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_home) {
                showHomeTab();
                return true;
            } else if (id == R.id.nav_place) {
                loadEmptyTab("Màn hình: Địa điểm");
                return true;
            } else if (id == R.id.nav_favorite) {
                loadEmptyTab("Màn hình: Yêu thích");
                return true;
            } else if (id == R.id.nav_profile) {
                loadEmptyTab("Màn hình: Cá nhân");
                return true;
            }
            return false;
        });
    }

    // Hàm hiển thị giao diện Trang chủ của bạn và ẩn các tab trống
    private void showHomeTab() {
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (current != null) {
            getSupportFragmentManager().beginTransaction().remove(current).commit();
        }
        binding.tvPlaceholder.setVisibility(View.GONE);
        binding.scrollHome.setVisibility(View.VISIBLE);
    }

    private void loadFragment(Fragment fragment) {
        binding.scrollHome.setVisibility(View.GONE);
        binding.tvPlaceholder.setVisibility(View.GONE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void loadEmptyTab(String title) {
        // Ẩn giao diện Trang chủ khi chuyển sang các tab chưa làm
        binding.scrollHome.setVisibility(View.GONE);

        Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (current != null) {
            getSupportFragmentManager().beginTransaction().remove(current).commit();
        }
        binding.tvPlaceholder.setVisibility(View.VISIBLE);
        binding.tvPlaceholder.setText(title + "\n(Màn hình trống)");
    }
}
