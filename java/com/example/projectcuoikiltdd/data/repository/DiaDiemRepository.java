package com.example.projectcuoikiltdd.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.projectcuoikiltdd.data.dao.DiaDiemDao;
import com.example.projectcuoikiltdd.data.database.AppDatabase;
import com.example.projectcuoikiltdd.data.entity.DiaDiem;

import java.util.List;

public class DiaDiemRepository {
    private final DiaDiemDao diaDiemDao;
    private final LiveData<List<DiaDiem>> allDiaDiem;

    public DiaDiemRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        diaDiemDao = db.diaDiemDao();
        allDiaDiem = diaDiemDao.getAll();
    }

    public LiveData<List<DiaDiem>> getAll() {
        return allDiaDiem;
    }

    public LiveData<List<DiaDiem>> getByDanhMuc(int idDanhMuc) {
        return diaDiemDao.getByDanhMuc(idDanhMuc);
    }

    public LiveData<List<DiaDiem>> search(String keyword) {
        return diaDiemDao.search(keyword);
    }

    public LiveData<DiaDiem> getById(int id) {
        return diaDiemDao.getById(id);
    }

    public void insert(DiaDiem diaDiem) {
        AppDatabase.databaseWriteExecutor.execute(() -> diaDiemDao.insert(diaDiem));
    }

    public void update(DiaDiem diaDiem) {
        AppDatabase.databaseWriteExecutor.execute(() -> diaDiemDao.update(diaDiem));
    }

    public void delete(DiaDiem diaDiem) {
        AppDatabase.databaseWriteExecutor.execute(() -> diaDiemDao.delete(diaDiem));
    }
}

