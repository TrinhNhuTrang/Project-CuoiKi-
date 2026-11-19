
package com.example.projectcuoikiltdd;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private static final int GREEN = Color.parseColor("#2E7D32");
    private static final int GOLD = Color.parseColor("#8D6E00");

    private EditText edtName, edtEmail, edtPhone, edtPassword, edtConfirm;
    private CheckBox cbAgree;
    private ImageButton btnTogglePassword;
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Thanh trạng thái cùng màu nền, icon tối
        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#EAF7EA"));
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        );

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirm = findViewById(R.id.edtConfirm);
        cbAgree = findViewById(R.id.cbAgree);
        btnTogglePassword = findViewById(R.id.btnTogglePassword);

        styleTexts();
        setupLoginLink();
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

    // Tạo liên kết Đăng nhập ngay
    private void setupLoginLink() {
        TextView tv = findViewById(R.id.tvLoginNow);

        String link = "Đăng nhập ngay";
        String current = tv.getText().toString();

        String full = current.contains("Đã có tài khoản")
                ? current : link;

        SpannableString sp = new SpannableString(full);

        int start = full.indexOf(link);

        if (start < 0) return;

        int end = start + link.length();

        sp.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                onLoginClick(widget);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(GREEN);
                ds.setFakeBoldText(true);
                ds.setUnderlineText(false);
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        sp.setSpan(
                new StyleSpan(Typeface.BOLD),
                start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        tv.setText(sp);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setHighlightColor(Color.TRANSPARENT);
        tv.setOnClickListener(null);
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
        // TODO: startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void onTogglePasswordClick(View view) {
        passwordVisible = !passwordVisible;

        int type = passwordVisible
                ? InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                : InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD;

        edtPassword.setInputType(type);
        edtPassword.setSelection(edtPassword.getText().length());

        btnTogglePassword.setImageResource(
                passwordVisible
                        ? R.drawable.ic_visibity_off
                        : R.drawable.ic_visibility
        );
    }

    public void onRegisterClick(View view) {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString();
        String confirm = edtConfirm.getText().toString();

        if (TextUtils.isEmpty(name)) {
            edtName.setError("Vui lòng nhập họ và tên");
            edtName.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
            return;
        }

        if (!TextUtils.isEmpty(phone)
                && !phone.replace(" ", "").matches("0\\d{9}")) {
            edtPhone.setError("Số điện thoại không hợp lệ");
            edtPhone.requestFocus();
            return;
        }

        if (!isValidPassword(password)) {
            edtPassword.setError("Tối thiểu 8 ký tự, gồm cả chữ và số");
            edtPassword.requestFocus();
            return;
        }

        if (!password.equals(confirm)) {
            edtConfirm.setError("Mật khẩu xác nhận không khớp");
            edtConfirm.requestFocus();
            return;
        }

        if (!cbAgree.isChecked()) {
            Toast.makeText(
                    this,
                    "Bạn cần đồng ý với điều khoản",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // TODO: lưu vào DatabaseHelper / gọi API
        Toast.makeText(
                this,
                "Đăng ký thành công!",
                Toast.LENGTH_SHORT
        ).show();
    }

    public void onGoogleClick(View view) {
        Toast.makeText(
                this,
                "Đăng ký với Google",
                Toast.LENGTH_SHORT
        ).show();
    }

    public void onAppleClick(View view) {
        Toast.makeText(
                this,
                "Đăng ký với Apple",
                Toast.LENGTH_SHORT
        ).show();
    }

    // Tối thiểu 8 ký tự, có chữ và số
    private boolean isValidPassword(String p) {
        return p.length() >= 8
                && p.matches(".*[A-Za-z].*")
                && p.matches(".*\\d.*");
    }
}
