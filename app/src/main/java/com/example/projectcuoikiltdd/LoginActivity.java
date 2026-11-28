package com.example.projectcuoikiltdd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectcuoikiltdd.data.entity.NguoiDung;
import com.example.projectcuoikiltdd.data.repository.NguoiDungRepository;
import com.example.projectcuoikiltdd.utils.UserSession;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private TextView btnLogin;
    private NguoiDungRepository nguoiDungRepository;
    private UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nguoiDungRepository = new NguoiDungRepository(getApplication());
        userSession = UserSession.getInstance(this);

        edtEmail = findViewById(R.id.edtLoginEmail);
        edtPassword = findViewById(R.id.edtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);

        findViewById(R.id.tabRegister).setOnClickListener(v -> openRegistration());
        findViewById(R.id.tvRegisterNow).setOnClickListener(v -> openRegistration());

        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> handleLogin());
        }
    }

    private void handleLogin() {
        String email = edtEmail != null ? edtEmail.getText().toString().trim() : "";
        String password = edtPassword != null ? edtPassword.getText().toString().trim() : "";

        if (email.isEmpty()) {
            if (edtEmail != null) edtEmail.setError("Vui lòng nhập Email!");
            Toast.makeText(this, "Vui lòng nhập Email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            if (edtPassword != null) edtPassword.setError("Vui lòng nhập mật khẩu!");
            Toast.makeText(this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        nguoiDungRepository.dangNhap(email, password, new NguoiDungRepository.AuthCallback() {
            @Override
            public void onSuccess(NguoiDung nguoiDung) {
                runOnUiThread(() -> {
                    userSession.saveUser(LoginActivity.this, nguoiDung);
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công! Chào mừng " + nguoiDung.getHoTen(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, LichTrinhActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void openRegistration() {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
