package com.example.projectcuoikiltdd;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Account {

    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";

    public interface Callback {
        void onSuccess();
        void onError(String message);
    }

    private String accountId;
    private String username;
    private String status;
    private Date createdAcc;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Getter
    public String getAccountId() { return accountId; }
    public String getUsername() { return username; }
    public String getStatus() { return status; }
    public Date getCreatedAcc() { return createdAcc; }
    // Kiểm tra đăng nhập
    public boolean isLoggedIn() {
        return auth.getCurrentUser() != null;
    }
    // Lấy ID người dùng hiện tại
    public static String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : null;
    }
    // Thông báo lỗi
    private String error(Exception e, String message) {
        android.util.Log.e("Account", message, e);
        if (e instanceof FirebaseAuthUserCollisionException) {
            return "Email này đã được đăng ký";
        }
        if (e instanceof FirebaseAuthInvalidCredentialsException
                || e instanceof FirebaseAuthInvalidUserException) {
            return "Email hoặc mật khẩu không đúng";
        }
        if (e instanceof FirebaseNetworkException) {
            return "Không có kết nối mạng";
        }
        return message + ": " + e.getMessage();
    }
    // 1. Đăng ký
    public void register(Customer customer, String password, Callback cb) {
        if (customer == null || customer.getEmail() == null
                || customer.getEmail().trim().isEmpty()) {
            cb.onError("Vui lòng nhập email");
            return;
        }
        if (password == null || password.length() < 8) {
            cb.onError("Mật khẩu tối thiểu 8 ký tự");
            return;
        }
        String email = customer.getEmail().trim();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    FirebaseUser user = result.getUser();

                    if (user == null) {
                        cb.onError("Không tạo được tài khoản");
                        return;
                    }

                    String uid = user.getUid();
                    customer.setUserId(uid);

                    Map<String, Object> data = new HashMap<>();
                    data.put("username", email);
                    data.put("status", STATUS_ACTIVE);
                    data.put("createdAcc", FieldValue.serverTimestamp());

                    WriteBatch batch = db.batch();
                    batch.set(db.collection("users").document(uid), customer);
                    batch.set(db.collection("accounts").document(uid), data);

                    batch.commit()
                            .addOnSuccessListener(v -> {
                                accountId = uid;
                                username = email;
                                status = STATUS_ACTIVE;
                                cb.onSuccess();
                            })
                            .addOnFailureListener(e ->
                                    user.delete().addOnCompleteListener(task ->
                                            cb.onError(error(e, "Không lưu được dữ liệu"))));
                })
                .addOnFailureListener(e ->
                        cb.onError(error(e, "Đăng ký thất bại")));
    }
    // 2. Đăng nhập
    public void login(String email, String password, Callback cb) {
        if (email == null || email.trim().isEmpty()
                || password == null || password.isEmpty()) {
            cb.onError("Vui lòng nhập email và mật khẩu");
            return;
        }
        String cleanEmail = email.trim();
        auth.signInWithEmailAndPassword(cleanEmail, password)
                .addOnSuccessListener(result -> {
                    FirebaseUser user = result.getUser();
                    if (user == null) {
                        cb.onError("Không tìm thấy tài khoản");
                        return;
                    }
                    String uid = user.getUid();
                    db.collection("accounts").document(uid).get()
                            .addOnSuccessListener(doc -> {
                                if (!doc.exists()) {
                                    logout();
                                    cb.onError("Không tìm thấy thông tin tài khoản");
                                    return;
                                }
                                String st = doc.getString("status");
                                if (!STATUS_ACTIVE.equals(st)) {
                                    logout();
                                    cb.onError("Tài khoản đã bị khóa");
                                    return;
                                }
                                accountId = uid;
                                username = cleanEmail;
                                status = st;
                                createdAcc = doc.getDate("createdAcc");

                                cb.onSuccess();
                            })
                            .addOnFailureListener(e -> {
                                logout();
                                cb.onError(error(e, "Lỗi kiểm tra tài khoản"));
                            });
                })
                .addOnFailureListener(e ->
                        cb.onError(error(e, "Đăng nhập thất bại")));
    }
    // 3. Đăng xuất
    public void logout() {
        auth.signOut();
        accountId = null;
        username = null;
        status = null;
        createdAcc = null;
    }
    // 4. Đổi mật khẩu
    public void changePassword(String oldPass, String newPass, Callback cb) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null || user.getEmail() == null) {
            cb.onError("Bạn chưa đăng nhập");
            return;
        }
        if (newPass == null || newPass.length() < 8) {
            cb.onError("Mật khẩu mới tối thiểu 8 ký tự");
            return;
        }
        user.reauthenticate(
                EmailAuthProvider.getCredential(user.getEmail(), oldPass))
                .addOnSuccessListener(v ->
                        user.updatePassword(newPass)
                                .addOnSuccessListener(x -> cb.onSuccess())
                                .addOnFailureListener(e ->
                                        cb.onError(error(e, "Đổi mật khẩu thất bại"))))
                .addOnFailureListener(e -> cb.onError("Mật khẩu cũ không đúng"));
    }

    // 5. Quên mật khẩu
    public void resetPassword(String email, Callback cb) {
        if (email == null || email.trim().isEmpty()) {
            cb.onError("Vui lòng nhập email");
            return;
        }

        auth.sendPasswordResetEmail(email.trim())
                .addOnSuccessListener(v -> cb.onSuccess())
                .addOnFailureListener(e -> cb.onError(
                        e instanceof FirebaseAuthInvalidUserException
                                ? "Email này chưa được đăng ký"
                                : error(e, "Gửi email thất bại")));
    }

    // 6. Xác minh email
    public void verifyEmail(Callback cb) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            cb.onError("Bạn chưa đăng nhập");
            return;
        }
        user.sendEmailVerification()
                .addOnSuccessListener(v -> cb.onSuccess())
                .addOnFailureListener(e ->
                        cb.onError(error(e, "Gửi email xác minh thất bại")));
    }

    // 7. Ngừng hoạt động tài khoản
    public void deactivateAccount(Callback cb) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            cb.onError("Bạn chưa đăng nhập");
            return;
        }
        db.collection("accounts").document(user.getUid())
                .update("status", STATUS_INACTIVE)
                .addOnSuccessListener(v -> {
                    logout();
                    cb.onSuccess();
                })
                .addOnFailureListener(e ->
                        cb.onError(error(e, "Không thể khóa tài khoản")));
    }

}
