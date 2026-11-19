package com.example.projectcuoikiltdd.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.projectcuoikiltdd.data.dao.NguoiDungDao;
import com.example.projectcuoikiltdd.data.database.AppDatabase;
import com.example.projectcuoikiltdd.data.entity.NguoiDung;

public class NguoiDungRepository {
    private final NguoiDungDao nguoiDungDao;

    public interface AuthCallback {
        void onSuccess(NguoiDung nguoiDung);
        void onError(String message);
    }

    public NguoiDungRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        nguoiDungDao = db.nguoiDungDao();
    }

    public void dangKy(NguoiDung nguoiDung, AuthCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            NguoiDung existing = nguoiDungDao.getByEmail(nguoiDung.getEmail());
            if (existing != null) {
                callback.onError("Email này đã được đăng ký!");
            } else {
                long id = nguoiDungDao.insert(nguoiDung);
                nguoiDung.setIdNguoiDung((int) id);
                callback.onSuccess(nguoiDung);
            }
        });
    }

    public void dangNhap(String email, String matKhau, AuthCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            NguoiDung nguoiDung = nguoiDungDao.login(email, matKhau);
            if (nguoiDung != null) {
                callback.onSuccess(nguoiDung);
            } else {
                callback.onError("Email hoặc mật khẩu không chính xác!");
            }
        });
    }

    public LiveData<NguoiDung> getById(int id) {
        return nguoiDungDao.getById(id);
    }
}

