package com.example.projectcuoikiltdd;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/** Trang Quên mật khẩu: nhập email -> gửi liên kết đặt lại mật khẩu (Firebase). */
public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private TextView btnSend, tvResult;
    private final Account account = new Account();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isFinishing() || isDestroyed()) return;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.edtForgotEmail);
        btnSend = findViewById(R.id.btnSendReset);
        tvResult = findViewById(R.id.tvResult);

        // Nhận sẵn email người dùng đã gõ ở trang Đăng nhập
        String email = getIntent().getStringExtra("email");
        if (email != null && !email.isEmpty()) {
            etEmail.setText(email);
            etEmail.setSelection(email.length());
        }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.tvBackToLogin).setOnClickListener(v -> finish());
        btnSend.setOnClickListener(v -> sendReset());
    }

    private void sendReset() {
        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Vui lòng nhập email");
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            etEmail.requestFocus();
            return;
        }

        tvResult.setVisibility(View.GONE);
        btnSend.setEnabled(false);
        account.resetPassword(email, new Account.Callback() {
            @Override
            public void onSuccess() {
                if (isFinishing() || isDestroyed()) return;
                btnSend.setEnabled(true);
                tvResult.setText("Nếu " + email + " đã được đăng ký, bạn sẽ nhận được "
                        + "liên kết đặt lại mật khẩu. Hãy kiểm tra hộp thư (kể cả thư rác).");
                tvResult.setVisibility(View.VISIBLE);
                Toast.makeText(ForgotPasswordActivity.this,
                        "Đã gửi email đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                if (isFinishing() || isDestroyed()) return;
                btnSend.setEnabled(true);
                Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
