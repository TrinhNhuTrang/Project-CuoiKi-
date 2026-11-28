package com.example.projectcuoikiltdd;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail;
    private final Account account = new Account();
    private final Customer currentCustomer = new Customer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!account.isLoggedIn()) {
            goLogin();
            return;
        }

        setContentView(R.layout.activity_profile);

        Window window = getWindow();
        window.setStatusBarColor(Color.WHITE);
        WindowCompat.getInsetsController(window, window.getDecorView())
                .setAppearanceLightStatusBars(true);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);

        // Bấm vào thẻ thông tin hồ sơ để sửa
        findViewById(R.id.profileCard).setOnClickListener(v -> showEditDialog());

        // Tải thông tin từ Firestore
        loadProfile();

        // Đăng xuất
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            account.logout();
            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            goLogin();
        });

        setupBottomNav();
    }

    private void loadProfile() {
        // viewMyProfile tự điền dữ liệu vào currentCustomer
        currentCustomer.viewMyProfile(new User.ProfileCallback() {
            @Override
            public void onSuccess(User user) {
                showInfo(user.getFullName(), user.getEmail());
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showInfo(String name, String email) {
        if (name != null && !name.trim().isEmpty()) {
            tvName.setText(name.trim());
            tvName.setVisibility(View.VISIBLE);
        }
        if (email != null && !email.trim().isEmpty()) {
            tvEmail.setText(email.trim());
            tvEmail.setVisibility(View.VISIBLE);
        }
    }

    private void showEditDialog() {
        // Hồ sơ chưa tải xong thì chưa cho sửa
        if (currentCustomer.getEmail() == null) {
            Toast.makeText(this, "Hồ sơ đang tải, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            return;
        }
        int pad = (int) (20 * getResources().getDisplayMetrics().density);

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(pad, pad / 2, pad, 0);

        EditText edtName = new EditText(this);
        edtName.setHint("Họ và tên");
        edtName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        if (currentCustomer.getFullName() != null) {
            edtName.setText(currentCustomer.getFullName());
        }
        box.addView(edtName);

        EditText edtPhone = new EditText(this);
        edtPhone.setHint("Số điện thoại");
        edtPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        if (currentCustomer.getPhone() != null) {
            edtPhone.setText(currentCustomer.getPhone());
        }
        box.addView(edtPhone);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Cập nhật hồ sơ")
                .setView(box)
                .setPositiveButton("Lưu", null)
                .setNegativeButton("Hủy", null)
                .create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String newName = edtName.getText().toString().trim();
            String newPhone = edtPhone.getText().toString().replaceAll("[\\s.\\-]", "");

            if (newName.isEmpty()) {
                edtName.setError("Vui lòng nhập họ và tên");
                return;
            }
            if (!newPhone.isEmpty() && !newPhone.matches("^(0|\\+84)\\d{9}$")) {
                edtPhone.setError("Số điện thoại không hợp lệ");
                return;
            }

            String oldName = currentCustomer.getFullName();
            String oldPhone = currentCustomer.getPhone();
            currentCustomer.setFullName(newName);
            currentCustomer.setPhone(newPhone);

            currentCustomer.updateMyProfile(new Account.Callback() {
                @Override
                public void onSuccess() {
                    showInfo(currentCustomer.getFullName(), currentCustomer.getEmail());
                    dialog.dismiss();
                    Toast.makeText(ProfileActivity.this,
                            "Đã cập nhật hồ sơ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String message) {
                    // Lưu lỗi -> trả lại dữ liệu cũ
                    currentCustomer.setFullName(oldName);
                    currentCustomer.setPhone(oldPhone);
                    Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setupBottomNav() {
        findViewById(R.id.navHome).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        findViewById(R.id.navPlace).setOnClickListener(v ->
                startActivity(new Intent(this, TimKiemActivity.class)));
        findViewById(R.id.navItinerary).setOnClickListener(v ->
                startActivity(new Intent(this, LichTrinhActivity.class)));
        findViewById(R.id.navFavorite).setOnClickListener(v ->
                startActivity(new Intent(this, DiaDiemYeuThichActivity.class)));
    }

    private void goLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}
