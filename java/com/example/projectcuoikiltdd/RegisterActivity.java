package com.example.projectcuoikiltdd;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText etFullName, etEmail, etPhone, etPassword, etConfirmPass;
    private CheckBox cbAgree;
    private TextView btnRegister;
    private boolean passwordVisible = false;
    private final Account account = new Account();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.edtName);
        etEmail = findViewById(R.id.edtEmail);
        etPhone = findViewById(R.id.edtPhone);
        etPassword = findViewById(R.id.edtPassword);
        etConfirmPass = findViewById(R.id.edtConfirm);
        cbAgree = findViewById(R.id.cbAgree);
        btnRegister = findViewById(R.id.btnRegister);


        ImageView btnToggle = findViewById(R.id.btnTogglePassword);
        btnToggle.setImageResource(R.drawable.ic_visibility_off);
        btnToggle.setOnClickListener(v -> {
            passwordVisible = !passwordVisible;
            TransformationMethod tm = passwordVisible
                    ? HideReturnsTransformationMethod.getInstance()
                    : PasswordTransformationMethod.getInstance();
            etPassword.setTransformationMethod(tm);
            etConfirmPass.setTransformationMethod(tm);
            etPassword.setSelection(etPassword.getText().length());
            btnToggle.setImageResource(passwordVisible
                    ? R.drawable.ic_visibility : R.drawable.ic_visibility_off);
        });

        findViewById(R.id.tvTerms).setOnClickListener(v -> cbAgree.setChecked(!cbAgree.isChecked()));
        btnRegister.setOnClickListener(v -> registerUser());

        // Quay lại màn Đăng nhập
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.tabLogin).setOnClickListener(v -> finish());
        findViewById(R.id.tvLoginNow).setOnClickListener(v -> finish());

        // Google / Apple: chưa tích hợp
        View.OnClickListener soon = v ->
                Toast.makeText(this, "Tính năng sẽ sớm ra mắt", Toast.LENGTH_SHORT).show();
        findViewById(R.id.btnGoogle).setOnClickListener(soon);
        findViewById(R.id.btnApple).setOnClickListener(soon);
    }
    private void registerUser() {
        String name = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().replaceAll("[\\s.\\-]", "");
        String pass = etPassword.getText().toString();
        String confirm = etConfirmPass.getText().toString().trim();

        if (name.isEmpty()) {
            etFullName.setError("Vui lòng nhập họ tên");
            etFullName.requestFocus();
            return;
        }
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
        if (!phone.isEmpty() && !phone.matches("^(0|\\+84)\\d{9}$")) {
            etPhone.setError("Số điện thoại không hợp lệ");
            etPhone.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return;
        }
        if (pass.length() < 8 || !pass.matches(".*[A-Za-z].*") || !pass.matches(".*\\d.*")) {
            etPassword.setError("Mật khẩu tối thiểu 8 ký tự, gồm cả chữ và số");
            etPassword.requestFocus();
            return;
        }
        if (!pass.equals(confirm)) {
            etConfirmPass.setError("Mật khẩu nhập lại không khớp");
            etConfirmPass.requestFocus();
            return;
        }
        if (!cbAgree.isChecked()) {
            Toast.makeText(this, "Vui lòng đồng ý điều khoản dịch vụ", Toast.LENGTH_SHORT).show();
            return;
        }
        Customer customer = new Customer();
        customer.setFullName(name);
        customer.setEmail(email);
        customer.setPhone(phone);

        btnRegister.setEnabled(false);
        account.register(customer, pass, new Account.Callback() {
            @Override
            public void onSuccess() {
                btnRegister.setEnabled(true);
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }

            @Override
            public void onError(String message) {
                btnRegister.setEnabled(true);
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
