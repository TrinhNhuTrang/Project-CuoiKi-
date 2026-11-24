package com.example.projectcuoikiltdd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class LichTrinhActivity extends AppCompatActivity {
    private static final String STATE_MONTH = "calendarMonth";
    private static final String STATE_DAY = "calendarDay";

    private MonthCalendar calendar;
    private Calendar selectedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lich_trinh);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        findViewById(R.id.btn_tao_chuyen_di).setOnClickListener(v ->
                startActivity(new Intent(this, TaoChuyenDiActivity.class)));
        findViewById(R.id.itineraryDiscover).setOnClickListener(v ->
                startActivity(new Intent(this, TimKiemActivity.class)));
        setupCalendar(savedInstanceState);
        BottomNavigation.setup(this, findViewById(R.id.bottom_navigation), R.id.nav_itinerary);
    }

    private void setupCalendar(Bundle state) {
        calendar = new MonthCalendar(findViewById(R.id.tv_month_year),
                findViewById(R.id.itineraryCalendarDays), findViewById(R.id.btn_prev_month),
                findViewById(R.id.btn_next_month), day -> {
                    selectedDay = day;
                    showCalendarSelection(day);
                });

        Calendar today = MonthCalendar.today();
        TextView todayLegend = findViewById(R.id.itineraryTodayLegend);
        todayLegend.setText(getString(R.string.trip_calendar_today,
                today.get(Calendar.DAY_OF_MONTH), today.get(Calendar.MONTH) + 1));

        selectedDay = MonthCalendar.restoreDate(state, STATE_DAY);
        Calendar month = MonthCalendar.restoreDate(state, STATE_MONTH);
        showCalendarSelection(month != null ? month : today);
    }

    private void showCalendarSelection(Calendar month) {
        calendar.show(month, selectedDay, null);
        TextView selectedLegend = findViewById(R.id.tv_legend_selected);
        TextView viewingDate = findViewById(R.id.tv_viewing_date);
        viewingDate.setVisibility(selectedDay == null ? View.GONE : View.VISIBLE);
        if (selectedDay != null) {
            int day = selectedDay.get(Calendar.DAY_OF_MONTH);
            int monthNumber = selectedDay.get(Calendar.MONTH) + 1;
            selectedLegend.setText(getString(R.string.trip_calendar_selected, day, monthNumber));
            viewingDate.setText(getString(R.string.trip_calendar_viewing_date, day, monthNumber));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MonthCalendar.saveDate(outState, STATE_MONTH, calendar.getVisibleMonth());
        MonthCalendar.saveDate(outState, STATE_DAY, selectedDay);
    }
}
