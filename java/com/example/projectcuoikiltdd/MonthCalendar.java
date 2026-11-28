package com.example.projectcuoikiltdd;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.Consumer;

final class MonthCalendar {
    private static final int DAYS_PER_WEEK = 7;
    // UTC keeps date-only comparisons and durations independent of daylight saving time.
    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    private final TextView title;
    private final LinearLayout grid;
    private final Consumer<Calendar> onDaySelected;
    private final Calendar today = today();
    private final Calendar visibleMonth = (Calendar) today.clone();
    private Calendar startDate;
    private Calendar endDate;

    MonthCalendar(TextView title, LinearLayout grid, View previous, View next,
                  Consumer<Calendar> onDaySelected) {
        this.title = title;
        this.grid = grid;
        this.onDaySelected = onDaySelected;
        previous.setOnClickListener(v -> moveByMonths(-1));
        previous.setFocusable(true);
        next.setOnClickListener(v -> moveByMonths(1));
        next.setFocusable(true);
    }

    Calendar getVisibleMonth() {
        return (Calendar) visibleMonth.clone();
    }

    void show(Calendar month, Calendar startDate, Calendar endDate) {
        visibleMonth.setTimeInMillis(month.getTimeInMillis());
        visibleMonth.set(Calendar.DAY_OF_MONTH, 1);
        this.startDate = startDate;
        this.endDate = endDate;
        render();
    }

    private void moveByMonths(int months) {
        visibleMonth.add(Calendar.MONTH, months);
        render();
    }

    private void render() {
        Context context = grid.getContext();
        title.setText(context.getString(R.string.trip_calendar_month,
                visibleMonth.get(Calendar.MONTH) + 1, visibleMonth.get(Calendar.YEAR)));
        grid.removeAllViews();

        int leadingDays = (visibleMonth.get(Calendar.DAY_OF_WEEK) + 5) % DAYS_PER_WEEK;
        int daysThisMonth = visibleMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weeks = (leadingDays + daysThisMonth + DAYS_PER_WEEK - 1) / DAYS_PER_WEEK;
        Calendar date = getVisibleMonth();
        date.add(Calendar.DAY_OF_MONTH, -leadingDays);
        int cellHeight = Math.round(40 * context.getResources().getDisplayMetrics().density);

        for (int week = 0; week < weeks; week++) {
            LinearLayout row = new LinearLayout(context);
            row.setBaselineAligned(false);
            grid.addView(row, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, cellHeight));
            for (int day = 0; day < DAYS_PER_WEEK; day++) {
                row.addView(createDay((Calendar) date.clone()),
                        new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                date.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
    }

    private TextView createDay(Calendar date) {
        Context context = grid.getContext();
        boolean endpoint = date.equals(startDate) || date.equals(endDate);
        boolean inRange = startDate != null && endDate != null
                && date.after(startDate) && date.before(endDate);
        boolean isToday = date.equals(today);
        boolean inMonth = date.get(Calendar.MONTH) == visibleMonth.get(Calendar.MONTH);

        TextView cell = new TextView(context);
        cell.setGravity(Gravity.CENTER);
        cell.setTextSize(13);
        cell.setText(String.format(Locale.getDefault(), "%d", date.get(Calendar.DAY_OF_MONTH)));
        cell.setContentDescription(context.getString(R.string.trip_calendar_full_date,
                date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH) + 1,
                date.get(Calendar.YEAR)));
        int textColor = inMonth ? R.color.text_dark : R.color.text_gray;
        if (endpoint) {
            cell.setBackgroundResource(R.drawable.bg_pill_selected);
            cell.setTypeface(Typeface.DEFAULT_BOLD);
            textColor = R.color.green_dark;
        } else if (isToday) {
            cell.setBackgroundResource(R.drawable.bg_pill_today);
            textColor = R.color.white;
        } else if (inRange) {
            cell.setBackgroundResource(R.drawable.bg_range_pill);
        }
        cell.setTextColor(ContextCompat.getColor(context, textColor));
        cell.setSelected(endpoint);
        cell.setFocusable(true);
        cell.setOnClickListener(v -> onDaySelected.accept(date));
        return cell;
    }

    static Calendar today() {
        Calendar local = Calendar.getInstance();
        return date(local.get(Calendar.YEAR), local.get(Calendar.MONTH),
                local.get(Calendar.DAY_OF_MONTH));
    }

    static Calendar date(int year, int month, int day) {
        Calendar date = new GregorianCalendar(UTC, Locale.ROOT);
        date.clear();
        date.set(year, month, day);
        return date;
    }

    static Calendar restoreDate(Bundle state, String key) {
        if (state == null || !state.containsKey(key)) {
            return null;
        }
        Calendar date = new GregorianCalendar(UTC, Locale.ROOT);
        date.setTimeInMillis(state.getLong(key));
        return date;
    }

    static void saveDate(Bundle state, String key, Calendar date) {
        if (date != null) {
            state.putLong(key, date.getTimeInMillis());
        }
    }
}
