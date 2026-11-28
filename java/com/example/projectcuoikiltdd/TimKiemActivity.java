package com.example.projectcuoikiltdd;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.shape.RelativeCornerSize;

public class TimKiemActivity extends AppCompatActivity {
    private static final String STATE_PROVINCE = "selectedProvince";
    private static final String STATE_PLACE_TYPE = "selectedPlaceType";
    private static final String STATE_FILTER_ROWS_VISIBLE = "filterRowsVisible";
    private static final int ALL_PROVINCES = 0;
    private static final int NO_PLACE_TYPE = -1;

    private static final int[] PROVINCES = {
            R.string.home_catalog_all,
            R.string.places_province_quang_nam,
            R.string.places_province_lam_dong,
            R.string.places_province_kien_giang
    };
    private static final int[] PLACE_TYPES = {
            R.string.places_type_nature,
            R.string.places_type_heritage,
            R.string.places_type_seaside
    };
    private static final int[] PLACE_TYPE_ICONS = {
            R.drawable.ic_leaf_sprout,
            R.drawable.ic_home_monument,
            R.drawable.ic_home_waves
    };

    private View filterRows;
    private TextView filterButton;
    private int selectedProvince = ALL_PROVINCES;
    private ChipGroup placeTypeFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem);

        EditText search = findViewById(R.id.edtSearch);
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.searchClear).setOnClickListener(v -> search.getText().clear());
        BottomNavigation.setup(this, findViewById(R.id.bottom_navigation), R.id.nav_place);

        filterRows = findViewById(R.id.placeFilterRows);
        filterButton = findViewById(R.id.btnFilter);
        placeTypeFilters = findViewById(R.id.placeTypeFilterChips);

        if (savedInstanceState != null) {
            int province = savedInstanceState.getInt(STATE_PROVINCE, ALL_PROVINCES);
            if (province >= ALL_PROVINCES && province < PROVINCES.length) {
                selectedProvince = province;
            }
        }
        int placeType = savedInstanceState == null
                ? NO_PLACE_TYPE : savedInstanceState.getInt(STATE_PLACE_TYPE, NO_PLACE_TYPE);

        addPlaceTypeChips();
        View selectedChip = placeTypeFilters.getChildAt(placeType);
        if (selectedChip != null) {
            placeTypeFilters.check(selectedChip.getId());
        }

        findViewById(R.id.provinceFilterButton).setOnClickListener(this::showProvinceMenu);
        placeTypeFilters.setOnCheckedStateChangeListener((group, checkedIds) -> updateFilterLabel());
        updateFilterLabel();

        setFiltersVisible(savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_FILTER_ROWS_VISIBLE));
        filterButton.setOnClickListener(v -> setFiltersVisible(filterRows.getVisibility() != View.VISIBLE));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_PROVINCE, selectedProvince);
        outState.putInt(STATE_PLACE_TYPE, selectedPlaceTypeIndex());
        outState.putBoolean(STATE_FILTER_ROWS_VISIBLE, filterRows.getVisibility() == View.VISIBLE);
    }

    private void showProvinceMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        Menu menu = popup.getMenu();
        for (int index = 0; index < PROVINCES.length; index++) {
            menu.add(Menu.NONE, index + 1, index, PROVINCES[index]);
        }
        menu.setGroupCheckable(Menu.NONE, true, true);
        menu.findItem(selectedProvince + 1).setChecked(true);
        popup.setOnMenuItemClickListener(item -> {
            selectedProvince = item.getItemId() - 1;
            updateFilterLabel();
            return true;
        });
        popup.show();
    }

    private void addPlaceTypeChips() {
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
        for (int index = 0; index < PLACE_TYPES.length; index++) {
            Chip chip = new Chip(this);
            chip.setId(View.generateViewId());
            chip.setText(PLACE_TYPES[index]);
            chip.setTextSize(13);
            chip.setCheckable(true);
            chip.setChipMinHeight(36 * density);
            chip.setShapeAppearanceModel(chip.getShapeAppearanceModel().toBuilder()
                    .setAllCornerSizes(new RelativeCornerSize(0.5f)).build());
            chip.setChipBackgroundColor(backgrounds);
            chip.setTextColor(labelColors);
            chip.setChipIconResource(PLACE_TYPE_ICONS[index]);
            chip.setChipIconSize(14 * density);
            chip.setChipIconTint(labelColors);
            chip.setChipIconVisible(true);
            placeTypeFilters.addView(chip);
        }
    }

    private int selectedPlaceTypeIndex() {
        return placeTypeFilters.indexOfChild(
                placeTypeFilters.findViewById(placeTypeFilters.getCheckedChipId()));
    }

    private void updateFilterLabel() {
        int count = (selectedProvince == ALL_PROVINCES ? 0 : 1)
                + (selectedPlaceTypeIndex() != NO_PLACE_TYPE ? 1 : 0);
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
