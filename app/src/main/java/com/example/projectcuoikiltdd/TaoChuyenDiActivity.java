package com.example.projectcuoikiltdd;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TaoChuyenDiActivity extends AppCompatActivity {

    private static final String STATE_MONTH = "calendarMonth";
    private static final String STATE_START_DATE = "startDate";
    private static final String STATE_END_DATE = "endDate";

    private ImageView btnBack, imgAiCheckbox;
    private EditText edtDestination;
    private TextView chipDalat, chipNinhBinh, chipPhuQuoc, chipHaGiang, chipSapa;
    private LinearLayout boxStartDate, boxEndDate, boxAiSuggest;
    private TextView tvStartDate, tvEndDate, tvDurationBadge;

    private MonthCalendar calendar;
    private Calendar startDate;
    private Calendar endDate;
    private boolean isAiEnabled = true; // Mặc định bật gợi ý AI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tao_chuyen_di);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        anhXaView();
        xuLyChonDiemDen();
        setupCalendar(savedInstanceState);
        xuLyNutBam();
    }

    private void anhXaView() {
        btnBack = findViewById(R.id.btn_back);
        edtDestination = findViewById(R.id.edt_destination);

        chipDalat = findViewById(R.id.chip_dalat);
        chipNinhBinh = findViewById(R.id.chip_ninhbinh);
        chipPhuQuoc = findViewById(R.id.chip_phuquoc);
        chipHaGiang = findViewById(R.id.chip_hagiang);
        chipSapa = findViewById(R.id.chip_sapa);

        boxStartDate = findViewById(R.id.box_start_date);
        boxEndDate = findViewById(R.id.box_end_date);
        tvStartDate = findViewById(R.id.tv_start_date);
        tvEndDate = findViewById(R.id.tv_end_date);
        tvDurationBadge = findViewById(R.id.tv_duration_badge);

        boxAiSuggest = findViewById(R.id.box_ai_suggest);
        imgAiCheckbox = findViewById(R.id.img_ai_checkbox);
    }

    private void xuLyChonDiemDen() {
        chipDalat.setOnClickListener(v -> chonChip(chipDalat, "Đà Lạt", "Đà Lạt, Lâm Đồng"));
        chipNinhBinh.setOnClickListener(v -> chonChip(chipNinhBinh, "Ninh Bình", "Tràng An, Ninh Bình"));
        chipPhuQuoc.setOnClickListener(v -> chonChip(chipPhuQuoc, "Phú Quốc", "Đảo Phú Quốc, Kiên Giang"));
        chipHaGiang.setOnClickListener(v -> chonChip(chipHaGiang, "Hà Giang", "Đồng Văn, Hà Giang"));
        chipSapa.setOnClickListener(v -> chonChip(chipSapa, "Sa Pa", "Thị xã Sa Pa, Lào Cai"));
    }

    private void chonChip(TextView selectedChip, String shortName, String fullLocation) {
        TextView[] allChips = {chipDalat, chipNinhBinh, chipPhuQuoc, chipHaGiang, chipSapa};
        String[] labels = {"Đà Lạt", "Ninh Bình", "Phú Quốc", "Hà Giang", "Sa Pa"};

        for (int i = 0; i < allChips.length; i++) {
            allChips[i].setBackgroundResource(R.drawable.bg_chip_unselected);
            allChips[i].setTextColor(Color.parseColor("#424242"));
            allChips[i].setText(labels[i]);
        }

        selectedChip.setBackgroundResource(R.drawable.bg_chip_selected);
        selectedChip.setTextColor(Color.WHITE);
        selectedChip.setText("✓ " + shortName);
        edtDestination.setText(fullLocation);
    }

    private void setupCalendar(Bundle state) {
        calendar = new MonthCalendar(findViewById(R.id.tv_cal_month),
                findViewById(R.id.createTripCalendarDays), findViewById(R.id.btn_cal_prev),
                findViewById(R.id.btn_cal_next), this::selectCalendarDay);

        startDate = MonthCalendar.restoreDate(state, STATE_START_DATE);
        endDate = MonthCalendar.restoreDate(state, STATE_END_DATE);
        Calendar month = MonthCalendar.restoreDate(state, STATE_MONTH);
        showDates(month != null ? month : MonthCalendar.today());

        boxStartDate.setOnClickListener(v -> openDatePicker(true));
        boxEndDate.setOnClickListener(v -> openDatePicker(false));
    }

    private void selectCalendarDay(Calendar day) {
        if (startDate == null || endDate != null || day.before(startDate)) {
            startDate = day;
            endDate = null;
        } else {
            endDate = day;
        }
        showDates(day);
    }

    private void openDatePicker(boolean isStartDate) {
        Calendar initial = isStartDate ? startDate : endDate;
        if (initial == null) {
            initial = !isStartDate && startDate != null ? startDate : MonthCalendar.today();
        }

        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, day) -> {
            Calendar selected = MonthCalendar.date(year, month, day);
            if (isStartDate) {
                startDate = selected;
                if (endDate != null && endDate.before(startDate)) {
                    endDate = null;
                }
            } else {
                endDate = selected;
            }
            showDates(selected);
        }, initial.get(Calendar.YEAR), initial.get(Calendar.MONTH),
                initial.get(Calendar.DAY_OF_MONTH));

        if (!isStartDate && startDate != null) {
            Calendar minimum = Calendar.getInstance();
            minimum.clear();
            minimum.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH),
                    startDate.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMinDate(minimum.getTimeInMillis());
        }
        dialog.show();
    }

    private void showDates(Calendar month) {
        calendar.show(month, startDate, endDate);
        tvStartDate.setText(formatDate(startDate));
        tvEndDate.setText(formatDate(endDate));

        boolean rangeSelected = startDate != null && endDate != null;
        tvDurationBadge.setVisibility(rangeSelected ? View.VISIBLE : View.GONE);
        if (rangeSelected) {
            long nights = TimeUnit.MILLISECONDS.toDays(
                    endDate.getTimeInMillis() - startDate.getTimeInMillis());
            tvDurationBadge.setText(getString(R.string.trip_duration_value, nights + 1, nights));
        }
    }

    private String formatDate(Calendar date) {
        if (date == null) {
            return null;
        }
        return String.format(Locale.getDefault(), "%02d/%02d/%d",
                date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH) + 1,
                date.get(Calendar.YEAR));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MonthCalendar.saveDate(outState, STATE_MONTH, calendar.getVisibleMonth());
        MonthCalendar.saveDate(outState, STATE_START_DATE, startDate);
        MonthCalendar.saveDate(outState, STATE_END_DATE, endDate);
    }

    private void xuLyNutBam() {
        btnBack.setOnClickListener(v -> finish());

        // Bấm bật/tắt Trợ lý AI Việt Hành
        boxAiSuggest.setOnClickListener(v -> {
            isAiEnabled = !isAiEnabled;
            if (isAiEnabled) {
                imgAiCheckbox.setImageResource(R.drawable.bg_checkbox_checked);
            } else {
                imgAiCheckbox.setImageResource(R.drawable.bg_checkbox_unchecked);
            }
        });
    }
}
