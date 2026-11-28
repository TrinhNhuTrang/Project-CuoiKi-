package com.example.projectcuoikiltdd;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private TextView btnLogin;
    private boolean passwordVisible = false;
    private final Account account = new Account();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Còn phiên đăng nhập -> vào thẳng trang chính
        if (account.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.edtLoginEmail);
        etPassword = findViewById(R.id.edtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        ImageView btnToggle = findViewById(R.id.loginPasswordVisibility);

        // Hiện / ẩn mật khẩu (đang ẩn -> icon "mắt gạch")
        btnToggle.setImageResource(R.drawable.ic_visibility_off);
        btnToggle.setOnClickListener(v -> {
            passwordVisible = !passwordVisible;
            etPassword.setTransformationMethod(passwordVisible
                    ? HideReturnsTransformationMethod.getInstance()
                    : PasswordTransformationMethod.getInstance());
            etPassword.setSelection(etPassword.getText().length());
            btnToggle.setImageResource(passwordVisible
                    ? R.drawable.ic_visibility : R.drawable.ic_visibility_off);
        });

        btnLogin.setOnClickListener(v -> loginUser());

        // Quên mật khẩu
        findViewById(R.id.tvForgot).setOnClickListener(v -> {
            Intent i = new Intent(this, ForgotPasswordActivity.class);
            i.putExtra("email", etEmail.getText().toString().trim());
            startActivity(i);
        });

        findViewById(R.id.tvRegisterNow).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
        findViewById(R.id.tabRegister).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        // Google / Apple: chưa tích hợp
        View.OnClickListener soon = v ->
                Toast.makeText(this, "Tính năng sẽ sớm ra mắt", Toast.LENGTH_SHORT).show();
        findViewById(R.id.btnGoogle).setOnClickListener(soon);
        findViewById(R.id.btnApple).setOnClickListener(soon);
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString();
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
        if (pass.isEmpty()) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return;
        }

        btnLogin.setEnabled(false);
        account.login(email, pass, new Account.Callback() {
            @Override
            public void onSuccess() {
                if (isFinishing() || isDestroyed()) return;
                btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }

            @Override
            public void onError(String message) {
                if (isFinishing() || isDestroyed()) return;
                btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
