package com.example.projectcuoikiltdd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.projectcuoikiltdd.data.entity.DanhMuc;
import com.example.projectcuoikiltdd.data.entity.DiaDiem;
import com.example.projectcuoikiltdd.databinding.ActivityMainBinding;
import com.example.projectcuoikiltdd.viewmodel.DanhMucViewModel;
import com.example.projectcuoikiltdd.viewmodel.DiaDiemViewModel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<DanhMuc> danhMucList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WindowCompat.getInsetsController(getWindow(), binding.getRoot())
                .setAppearanceLightStatusBars(true);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Điều hướng Profile & BottomNavigation
        binding.homeProfileButton.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
        BottomNavigation.setup(this, binding.bottomNavigation, R.id.nav_home);
        binding.bottomNavigation.setItemActiveIndicatorEnabled(false);

        // Thanh tìm kiếm & nút "Tất cả" / "Xem thêm" -> Mở TimKiemActivity
        binding.homeSearch.setOnClickListener(v ->
                startActivity(new Intent(this, TimKiemActivity.class)));
        binding.homeAllCategories.setOnClickListener(v ->
                startActivity(new Intent(this, TimKiemActivity.class)));
        binding.homeMoreFeatured.setOnClickListener(v ->
                startActivity(new Intent(this, TimKiemActivity.class)));

        // Banner Vịnh Hạ Long -> Tìm kiếm địa điểm Hạ Long
        binding.homeBannerDiscover.setOnClickListener(v -> {
            Intent intent = new Intent(this, TimKiemActivity.class);
            intent.putExtra(TimKiemActivity.EXTRA_SEARCH_KEYWORD, "Hạ Long");
            startActivity(intent);
        });

        // Quan sát danh mục để lọc đúng ID danh mục khi bấm từ Trang chủ
        DanhMucViewModel danhMucViewModel = new ViewModelProvider(this).get(DanhMucViewModel.class);
        danhMucViewModel.getAll().observe(this, list -> {
            if (list != null) danhMucList = list;
        });

        binding.homeIslands.setOnClickListener(v -> openSearchByCategory("Nghỉ dưỡng biển"));
        binding.homeMountains.setOnClickListener(v -> openSearchByCategory("Thiên nhiên"));
        binding.homeHeritage.setOnClickListener(v -> openSearchByCategory("Di tích văn hóa"));
        binding.homeDining.setOnClickListener(v -> openSearchByCategory("Ẩm thực"));

        // Đổ dữ liệu địa điểm nổi bật & phổ biến từ Room Database
        DiaDiemAdapter featuredAdapter = new DiaDiemAdapter(true, this::openDetail);
        DiaDiemAdapter popularAdapter = new DiaDiemAdapter(false, this::openDetail);
        binding.featuredPlaces.setAdapter(featuredAdapter);
        binding.popularPlaces.setAdapter(popularAdapter);

        DiaDiemViewModel diaDiemViewModel = new ViewModelProvider(this).get(DiaDiemViewModel.class);
        diaDiemViewModel.getAll().observe(this, list -> {
            if (list != null && !list.isEmpty()) {
                binding.homeFeaturedPlaceholder.setVisibility(View.GONE);
                binding.homePopularPlaceholder.setVisibility(View.GONE);
                featuredAdapter.setData(list);
                popularAdapter.setData(list);
            } else {
                binding.homeFeaturedPlaceholder.setVisibility(View.VISIBLE);
                binding.homePopularPlaceholder.setVisibility(View.VISIBLE);
            }
        });
    }

    private void openSearchByCategory(String categoryName) {
        Intent intent = new Intent(this, TimKiemActivity.class);
        for (DanhMuc dm : danhMucList) {
            if (dm.getTenDanhMuc().equalsIgnoreCase(categoryName)) {
                intent.putExtra(TimKiemActivity.EXTRA_FILTER_CATEGORY_ID, dm.getIdDanhMuc());
                break;
            }
        }
        startActivity(intent);
    }

    private void openDetail(DiaDiem diaDiem) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_PLACE_ID, diaDiem.getIdDiadiem());
        startActivity(intent);
    }
}