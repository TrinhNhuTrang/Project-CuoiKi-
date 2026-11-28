package com.example.projectcuoikiltdd.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.projectcuoikiltdd.data.dao.ChuyenDiDao;
import com.example.projectcuoikiltdd.data.database.AppDatabase;
import com.example.projectcuoikiltdd.data.entity.ChuyenDi;

import java.util.List;

public class ChuyenDiRepository {
    private final ChuyenDiDao chuyenDiDao;

    public ChuyenDiRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        chuyenDiDao = db.chuyenDiDao();
    }

    public void insert(ChuyenDi chuyenDi) {
        AppDatabase.databaseWriteExecutor.execute(() -> chuyenDiDao.insert(chuyenDi));
    }

    public void update(ChuyenDi chuyenDi) {
        AppDatabase.databaseWriteExecutor.execute(() -> chuyenDiDao.update(chuyenDi));
    }

    public void delete(ChuyenDi chuyenDi) {
        AppDatabase.databaseWriteExecutor.execute(() -> chuyenDiDao.delete(chuyenDi));
    }

    public LiveData<List<ChuyenDi>> getByNguoiDung(int idNguoiDung) {
        return chuyenDiDao.getByNguoiDung(idNguoiDung);
    }

    public LiveData<ChuyenDi> getById(int id) {
        return chuyenDiDao.getById(id);
    }
}

