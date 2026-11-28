package com.example.projectcuoikiltdd;

import android.util.Patterns;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class User {

    public interface ProfileCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    protected String userId;
    protected String fullName;
    protected String email;
    protected String phone;
    protected Date dateOfBirth;

    public User() {}

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    /** Họ tên không rỗng, email đúng dạng, số điện thoại (không bắt buộc) đúng dạng nếu có nhập. */
    public boolean validateInformation() {
        boolean validName = fullName != null && !fullName.trim().isEmpty();
        boolean validEmail = email != null && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
        boolean validPhone = phone == null || phone.trim().isEmpty()
                || phone.trim().matches("^(0|\\+84)\\d{9}$");
        return validName && validEmail && validPhone;
    }

    public void viewMyProfile(ProfileCallback cb) {
        String uid = Account.getCurrentUserId();
        if (uid == null) {
            cb.onError("Chưa đăng nhập");
            return;
        }
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) {
                        cb.onError("Không tìm thấy hồ sơ");
                        return;
                    }
                    fillFromDoc(doc);
                    cb.onSuccess(this);
                })
                .addOnFailureListener(e -> cb.onError("Lỗi tải hồ sơ"));
    }

    public void updateMyProfile(Account.Callback cb) {
        if (!validateInformation()) {
            cb.onError("Thông tin chưa hợp lệ");
            return;
        }
        String uid = userId != null ? userId : Account.getCurrentUserId();
        if (uid == null) {
            cb.onError("Chưa đăng nhập");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("fullName", fullName.trim());
        data.put("email", email.trim());
        data.put("phone", phone != null ? phone.trim() : "");
        if (dateOfBirth != null) {
            data.put("dateOfBirth", dateOfBirth);
        }

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(unused -> cb.onSuccess())
                .addOnFailureListener(e -> cb.onError("Cập nhật hồ sơ thất bại"));
    }

    protected void fillFromDoc(DocumentSnapshot doc) {
        this.userId = doc.getId();
        this.fullName = doc.getString("fullName");
        this.email = doc.getString("email");
        this.phone = doc.getString("phone");
        this.dateOfBirth = doc.getDate("dateOfBirth");
    }

    public abstract String displayRole();
}
