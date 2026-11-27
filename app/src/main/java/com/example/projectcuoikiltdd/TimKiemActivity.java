package com.example.projectcuoikiltdd;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectcuoikiltdd.data.entity.DanhMuc;
import com.example.projectcuoikiltdd.data.entity.DiaDiem;
import com.example.projectcuoikiltdd.viewmodel.DanhMucViewModel;
import com.example.projectcuoikiltdd.viewmodel.DiaDiemViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.shape.RelativeCornerSize;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class TimKiemActivity extends AppCompatActivity {
    public static final String EXTRA_FILTER_CATEGORY_ID = "filter_category_id";
    public static final String EXTRA_SEARCH_KEYWORD = "search_keyword";

    private static final String STATE_PROVINCE = "selectedProvinceName";
    private static final String STATE_PLACE_TYPE = "selectedCategoryId";
    private static final String STATE_FILTER_ROWS_VISIBLE = "filterRowsVisible";
    private static final int NO_CATEGORY = -1;

    private View filterRows;
    private TextView filterButton;
    private TextView searchResultSummary;
    private View searchResultsPlaceholder;
    private RecyclerView searchPlacesRecycler;
    private EditText edtSearch;
    private ChipGroup placeTypeFilters;

    private String selectedProvinceName = "";
    private int selectedCategoryId = NO_CATEGORY;
    private List<DiaDiem> allPlaces = new ArrayList<>();
    private List<DanhMuc> allCategories = new ArrayList<>();
    private DiaDiemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem);

        edtSearch = findViewById(R.id.edtSearch);
        searchResultSummary = findViewById(R.id.searchResultSummary);
        searchResultsPlaceholder = findViewById(R.id.searchResultsPlaceholder);
        searchPlacesRecycler = findViewById(R.id.searchPlaces);
        filterRows = findViewById(R.id.placeFilterRows);
        filterButton = findViewById(R.id.btnFilter);
        placeTypeFilters = findViewById(R.id.placeTypeFilterChips);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.searchClear).setOnClickListener(v -> edtSearch.getText().clear());
        BottomNavigation.setup(this, findViewById(R.id.bottom_navigation), R.id.nav_place);

        if (savedInstanceState != null) {
            selectedProvinceName = savedInstanceState.getString(STATE_PROVINCE, "");
            selectedCategoryId = savedInstanceState.getInt(STATE_PLACE_TYPE, NO_CATEGORY);
        } else {
            selectedCategoryId = getIntent().getIntExtra(EXTRA_FILTER_CATEGORY_ID, NO_CATEGORY);
            String initialQuery = getIntent().getStringExtra(EXTRA_SEARCH_KEYWORD);
            if (initialQuery != null) {
                edtSearch.setText(initialQuery);
            }
        }

        // Thiết lập RecyclerView & mở chi tiết theo ID
        adapter = new DiaDiemAdapter(false, diaDiem -> {
            Intent intent = new Intent(TimKiemActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_PLACE_ID, diaDiem.getIdDiadiem());
            startActivity(intent);
        });
        searchPlacesRecycler.setAdapter(adapter);

        // Quan sát dữ liệu Danh mục & Địa điểm từ Room
        DanhMucViewModel danhMucViewModel = new ViewModelProvider(this).get(DanhMucViewModel.class);
        danhMucViewModel.getAll().observe(this, danhMucs -> {
            allCategories = danhMucs != null ? danhMucs : new ArrayList<>();
            renderCategoryChips();
            applyFilters();
        });

        DiaDiemViewModel diaDiemViewModel = new ViewModelProvider(this).get(DiaDiemViewModel.class);
        diaDiemViewModel.getAll().observe(this, diaDiems -> {
            allPlaces = diaDiems != null ? diaDiems : new ArrayList<>();
            applyFilters();
        });

        // Tìm kiếm realtime theo tên hoặc địa chỉ (tất cả tỉnh thành)
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        findViewById(R.id.provinceFilterButton).setOnClickListener(this::showProvinceMenu);
        setFiltersVisible((savedInstanceState != null && savedInstanceState.getBoolean(STATE_FILTER_ROWS_VISIBLE))
                || selectedCategoryId != NO_CATEGORY);
        filterButton.setOnClickListener(v -> setFiltersVisible(filterRows.getVisibility() != View.VISIBLE));
        updateFilterLabel();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_PROVINCE, selectedProvinceName);
        outState.putInt(STATE_PLACE_TYPE, selectedCategoryId);
        outState.putBoolean(STATE_FILTER_ROWS_VISIBLE, filterRows.getVisibility() == View.VISIBLE);
    }

    private void showProvinceMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        Menu menu = popup.getMenu();

        // Lấy danh sách tỉnh thành động từ dữ liệu địa điểm trong nước + các tỉnh mặc định
        Set<String> provinces = new LinkedHashSet<>();
        provinces.add("Tất cả tỉnh thành");
        provinces.add("Ninh Bình");
        provinces.add("Yên Bái");
        provinces.add("Quảng Nam");
        provinces.add("Lâm Đồng");
        provinces.add("Kiên Giang");
        provinces.add("Quảng Ninh");
        provinces.add("Lào Cai");
        provinces.add("Hồ Chí Minh");

        for (DiaDiem dd : allPlaces) {
            if (dd.getDiaChi() != null && dd.getDiaChi().contains(",")) {
                String[] parts = dd.getDiaChi().split(",");
                String lastPart = parts[parts.length - 1].trim();
                lastPart = lastPart.replace("Tỉnh ", "").replace("Thành phố ", "").trim();
                if (!lastPart.isEmpty()) {
                    provinces.add(lastPart);
                }
            }
        }

        List<String> provinceList = new ArrayList<>(provinces);
        for (int i = 0; i < provinceList.size(); i++) {
            menu.add(Menu.NONE, i + 1, i, provinceList.get(i));
        }

        popup.setOnMenuItemClickListener(item -> {
            int idx = item.getItemId() - 1;
            selectedProvinceName = (idx == 0) ? "" : provinceList.get(idx);
            TextView btnProv = findViewById(R.id.provinceFilterButton);
            btnProv.setText(selectedProvinceName.isEmpty()
                    ? getString(R.string.places_filter_province_dropdown)
                    : selectedProvinceName + " ▾");
            updateFilterLabel();
            applyFilters();
            return true;
        });
        popup.show();
    }

    private void renderCategoryChips() {
        placeTypeFilters.removeAllViews();
        int[][] states = {{android.R.attr.state_checked}, {}};
        ColorStateList backgrounds = new ColorStateList(states, new int[]{
                ContextCompat.getColor(this, R.color.green_primary),
                ContextCompat.getColor(this, R.color.home_icon_surface)
        });
        ColorStateList labelColors = new ColorStateList(states, new int[]{
                ContextCompat.getColor(this, R.color.white),
                ContextCompat.getColor(this, R.color.text_dark)
        });
        float density = getResources().getDisplayMetrics().density;

        for (DanhMuc dm : allCategories) {
            Chip chip = new Chip(this);
            chip.setId(View.generateViewId());
            chip.setTag(dm.getIdDanhMuc());
            chip.setText(dm.getTenDanhMuc());
            chip.setTextSize(13);
            chip.setCheckable(true);
            chip.setChipMinHeight(36 * density);
            chip.setShapeAppearanceModel(chip.getShapeAppearanceModel().toBuilder()
                    .setAllCornerSizes(new RelativeCornerSize(0.5f)).build());
            chip.setChipBackgroundColor(backgrounds);
            chip.setTextColor(labelColors);
            chip.setChipIconResource(R.drawable.ic_leaf_sprout);
            chip.setChipIconSize(14 * density);
            chip.setChipIconTint(labelColors);
            chip.setChipIconVisible(true);

            placeTypeFilters.addView(chip);
            if (dm.getIdDanhMuc() == selectedCategoryId) {
                placeTypeFilters.check(chip.getId());
            }
        }

        placeTypeFilters.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                selectedCategoryId = NO_CATEGORY;
            } else {
                View checkedView = group.findViewById(checkedIds.get(0));
                selectedCategoryId = checkedView != null ? (int) checkedView.getTag() : NO_CATEGORY;
            }
            updateFilterLabel();
            applyFilters();
        });
    }

    private void applyFilters() {
        String keyword = edtSearch.getText().toString().trim().toLowerCase(Locale.ROOT);
        String provinceFilter = selectedProvinceName.toLowerCase(Locale.ROOT);
        List<DiaDiem> filtered = new ArrayList<>();

        for (DiaDiem dd : allPlaces) {
            boolean matchCategory = (selectedCategoryId == NO_CATEGORY) || (dd.getIdDanhMuc() == selectedCategoryId);
            String name = dd.getTenDiaDiem() != null ? dd.getTenDiaDiem().toLowerCase(Locale.ROOT) : "";
            String addr = dd.getDiaChi() != null ? dd.getDiaChi().toLowerCase(Locale.ROOT) : "";

            boolean matchKeyword = keyword.isEmpty() || name.contains(keyword) || addr.contains(keyword);
            boolean matchProvince = provinceFilter.isEmpty() || addr.contains(provinceFilter);

            if (matchCategory && matchKeyword && matchProvince) {
                filtered.add(dd);
            }
        }

        adapter.setData(filtered);

        // Xử lý trạng thái danh sách trống
        if (filtered.isEmpty()) {
            searchResultsPlaceholder.setVisibility(View.VISIBLE);
            searchPlacesRecycler.setVisibility(View.GONE);
            searchResultSummary.setText("Không tìm thấy địa điểm nào phù hợp");
        } else {
            searchResultsPlaceholder.setVisibility(View.GONE);
            searchPlacesRecycler.setVisibility(View.VISIBLE);
            searchResultSummary.setText("Tìm thấy " + filtered.size() + " địa điểm du lịch phù hợp tại Việt Nam");
        }
    }

    private void updateFilterLabel() {
        int count = (selectedProvinceName.isEmpty() ? 0 : 1) + (selectedCategoryId != NO_CATEGORY ? 1 : 0);
        if (count == 0) {
            filterButton.setText(R.string.places_filter_label);
        } else {
            filterButton.setText(getString(R.string.places_filter_active, count));
        }
    }

    private void setFiltersVisible(boolean visible) {
        filterRows.setVisibility(visible ? View.VISIBLE : View.GONE);
        filterButton.setSelected(visible);
    }
}