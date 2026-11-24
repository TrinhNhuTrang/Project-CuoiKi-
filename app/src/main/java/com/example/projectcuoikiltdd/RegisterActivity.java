
package com.example.projectcuoikiltdd;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private static final int GREEN = Color.parseColor("#2E7D32");
    private static final int GOLD = Color.parseColor("#8D6E00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        styleTexts();
        findViewById(R.id.tvLoginNow).setOnClickListener(this::onLoginClick);
    }

    private void styleTexts() {
        TextView tvAppName = findViewById(R.id.tvAppName);

        SpannableString appName = new SpannableString("Việt Hành •");
        appName.setSpan(
                new ForegroundColorSpan(GREEN),
                0, 9,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        appName.setSpan(
                new ForegroundColorSpan(GOLD),
                10, 11,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        tvAppName.setText(appName);

        TextView tvTerms = findViewById(R.id.tvTerms);

        String terms = "Tôi đồng ý với Điều khoản dịch vụ và Chính sách bảo mật của Việt Hành.";
        SpannableString st = new SpannableString(terms);

        highlight(st, terms, "Điều khoản dịch vụ");
        highlight(st, terms, "Chính sách bảo mật");

        tvTerms.setText(st);
    }

    private void highlight(SpannableString s, String full, String part) {
        int start = full.indexOf(part);

        if (start < 0) return;

        int end = start + part.length();

        s.setSpan(
                new ForegroundColorSpan(GREEN),
                start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        s.setSpan(
                new StyleSpan(Typeface.BOLD),
                start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
    }

    public void onBackClick(View view) {
        finish();
    }

    public void onLoginClick(View view) {
        finish();
    }
}
