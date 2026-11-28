package com.example.projectcuoikiltdd;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectcuoikiltdd.data.entity.ChuyenDi;
import com.example.projectcuoikiltdd.utils.UserSession;
import com.example.projectcuoikiltdd.viewmodel.ChuyenDiViewModel;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TaoChuyenDiActivity extends AppCompatActivity {

    public static final String EXTRA_TRIP_ID = "extra_trip_id";

    private static final String STATE_MONTH = "calendarMonth";
    private static final String STATE_START_DATE = "startDate";
    private static final String STATE_END_DATE = "endDate";

    private ImageView btnBack, imgAiCheckbox;
    private EditText edtTripName, edtDestination, edtBudget, edtTripNote;
    private TextView chipDalat, chipNinhBinh, chipPhuQuoc, chipHaGiang, chipSapa;
    private LinearLayout boxStartDate, boxEndDate, boxAiSuggest;
    private TextView tvStartDate, tvEndDate, tvDurationBadge, tvHeaderTitle;
    private Button btnSaveTrip;

    private MonthCalendar calendar;
    private Calendar startDate;
    private Calendar endDate;
    private boolean isAiEnabled = true;

    private ChuyenDiViewModel viewModel;
    private UserSession userSession;
    private int editingTripId = -1;
    private ChuyenDi editingTrip;

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

        viewModel = new ViewModelProvider(this).get(ChuyenDiViewModel.class);
        userSession = UserSession.getInstance(this);

        anhXaView();
        xuLyChonDiemDen();
        setupCalendar(savedInstanceState);
        xuLyNutBam();

        if (getIntent() != null && getIntent().hasExtra(EXTRA_TRIP_ID)) {
            editingTripId = getIntent().getIntExtra(EXTRA_TRIP_ID, -1);
            if (editingTripId > 0) {
                setupEditMode();
            }
        }
    }

    private void anhXaView() {
        btnBack = findViewById(R.id.btn_back);
        tvHeaderTitle = findViewById(R.id.tv_start_date); // Default header text view is inside relative layout or custom
        edtTripName = findViewById(R.id.edt_trip_name);
        edtDestination = findViewById(R.id.edt_destination);
        edtBudget = findViewById(R.id.edt_budget);
        edtTripNote = findViewById(R.id.edt_trip_note);
        btnSaveTrip = findViewById(R.id.btn_save_trip);

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

    private void setupEditMode() {
        btnSaveTrip.setText("Cập Nhật Chuyến Đi");
        viewModel.getById(editingTripId).observe(this, trip -> {
            if (trip != null && editingTrip == null) {
                editingTrip = trip;
                if (edtTripName != null && trip.getTenChuyenDi() != null) {
                    edtTripName.setText(trip.getTenChuyenDi());
                }
                if (edtDestination != null && trip.getDiaChi() != null) {
                    edtDestination.setText(trip.getDiaChi());
                }
                if (edtBudget != null && trip.getChiPhiDuKien() > 0) {
                    DecimalFormat df = new DecimalFormat("#");
                    edtBudget.setText(df.format(trip.getChiPhiDuKien()));
                }
                if (edtTripNote != null && trip.getGhiChu() != null) {
                    edtTripNote.setText(trip.getGhiChu());
                }

                if (trip.getNgayBatDau() > 0) {
                    startDate = Calendar.getInstance();
                    startDate.setTimeInMillis(trip.getNgayBatDau());
                }
                if (trip.getNgayKetThuc() > 0) {
                    endDate = Calendar.getInstance();
                    endDate.setTimeInMillis(trip.getNgayKetThuc());
                }
                showDates(startDate != null ? startDate : MonthCalendar.today());
            }
        });
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

        boxAiSuggest.setOnClickListener(v -> {
            isAiEnabled = !isAiEnabled;
            if (isAiEnabled) {
                imgAiCheckbox.setImageResource(R.drawable.bg_checkbox_checked);
            } else {
                imgAiCheckbox.setImageResource(R.drawable.bg_checkbox_unchecked);
            }
        });

        if (btnSaveTrip != null) {
            btnSaveTrip.setOnClickListener(v -> saveTrip());
        }
    }

    private void saveTrip() {
        String tripName = edtTripName != null ? edtTripName.getText().toString().trim() : "";
        String destination = edtDestination != null ? edtDestination.getText().toString().trim() : "";
        String budgetRaw = edtBudget != null ? edtBudget.getText().toString().trim() : "";
        String note = edtTripNote != null ? edtTripNote.getText().toString().trim() : "";

        if (tripName.isEmpty()) {
            if (edtTripName != null) {
                edtTripName.setError("Vui lòng nhập tên chuyến đi!");
                edtTripName.requestFocus();
            }
            Toast.makeText(this, "Vui lòng nhập tên chuyến đi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (destination.isEmpty()) {
            if (edtDestination != null) {
                edtDestination.setError("Vui lòng nhập điểm đến!");
                edtDestination.requestFocus();
            }
            Toast.makeText(this, "Vui lòng nhập điểm đến / tỉnh thành!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startDate == null || endDate == null) {
            Toast.makeText(this, "Vui lòng chọn ngày bắt đầu và kết thúc!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endDate.before(startDate)) {
            Toast.makeText(this, "Ngày kết thúc không được trước ngày bắt đầu!", Toast.LENGTH_SHORT).show();
            return;
        }

        double budget = 0;
        if (!budgetRaw.isEmpty()) {
            String cleanBudget = budgetRaw.replaceAll("[^0-9.]", "");
            try {
                if (!cleanBudget.isEmpty()) {
                    budget = Double.parseDouble(cleanBudget);
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Ngân sách không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        int currentUserId = userSession.getCurrentUserId(this);

        if (editingTrip != null) {
            editingTrip.setTenChuyenDi(tripName);
            editingTrip.setDiaChi(destination);
            editingTrip.setNgayBatDau(startDate.getTimeInMillis());
            editingTrip.setNgayKetThuc(endDate.getTimeInMillis());
            editingTrip.setChiPhiDuKien(budget);
            editingTrip.setGhiChu(note);

            viewModel.update(editingTrip);
            Toast.makeText(this, "Đã cập nhật chuyến đi!", Toast.LENGTH_SHORT).show();
        } else {
            ChuyenDi newTrip = new ChuyenDi(
                    currentUserId,
                    tripName,
                    destination,
                    startDate.getTimeInMillis(),
                    endDate.getTimeInMillis(),
                    budget,
                    note
            );
            viewModel.insert(newTrip);
            Toast.makeText(this, "Đã tạo chuyến đi mới thành công!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
