package com.example.projectcuoikiltdd.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.projectcuoikiltdd.data.database.AppDatabase;
import com.example.projectcuoikiltdd.data.entity.NguoiDung;

public class UserSession {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final int DEFAULT_USER_ID = 1;

    private static UserSession instance;
    private final SharedPreferences prefs;

    private UserSession(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized UserSession getInstance(Context context) {
        if (instance == null) {
            instance = new UserSession(context);
        }
        return instance;
    }

    public int getCurrentUserId(Context context) {
        int userId = prefs.getInt(KEY_USER_ID, DEFAULT_USER_ID);
        ensureUserExistsInDb(context, userId);
        return userId;
    }

    public void saveUser(Context context, NguoiDung user) {
        if (user != null) {
            prefs.edit()
                    .putInt(KEY_USER_ID, user.getIdNguoiDung())
                    .putString(KEY_USER_NAME, user.getHoTen())
                    .putString(KEY_USER_EMAIL, user.getEmail())
                    .apply();
        }
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "Người dùng Việt Hành");
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, "user@viethanh.com");
    }

    public boolean isLoggedIn() {
        return prefs.contains(KEY_USER_ID);
    }

    public void logout() {
        prefs.edit().clear().apply();
    }

    private void ensureUserExistsInDb(Context context, int userId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(context);
            if (db.nguoiDungDao().getByEmail("user@viethanh.com") == null) {
                NguoiDung defaultUser = new NguoiDung("Tín - Việt Hành User", "user@viethanh.com", "123456", "0901234567");
                defaultUser.setIdNguoiDung(DEFAULT_USER_ID);
                db.nguoiDungDao().insert(defaultUser);
            }
        });
    }
}
