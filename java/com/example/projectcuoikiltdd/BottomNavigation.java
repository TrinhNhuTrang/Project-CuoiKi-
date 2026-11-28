package com.example.projectcuoikiltdd;

import android.app.Activity;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

final class BottomNavigation {
    private BottomNavigation() {}

    static void setup(Activity activity, BottomNavigationView navigation, int currentTab) {
        navigation.setItemActiveIndicatorColor(
                ContextCompat.getColorStateList(activity, R.color.green_soft));
        navigation.setSelectedItemId(currentTab);
        navigation.setOnItemSelectedListener(item -> {
            int tab = item.getItemId();
            if (tab == currentTab) {
                return true;
            }

            Class<?> screen;
            if (tab == R.id.nav_home) {
                screen = MainActivity.class;
            } else if (tab == R.id.nav_place) {
                screen = TimKiemActivity.class;
            } else if (tab == R.id.nav_itinerary) {
                screen = LichTrinhActivity.class;
            } else if (tab == R.id.nav_favorite) {
                screen = DiaDiemYeuThichActivity.class;
            } else if (tab == R.id.nav_profile) {
                screen = ProfileActivity.class;
            } else {
                return false;
            }

            Intent intent = new Intent(activity, screen);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
            return false;
        });
    }
}
