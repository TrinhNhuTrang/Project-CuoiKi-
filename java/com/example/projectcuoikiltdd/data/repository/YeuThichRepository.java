package com.example.projectcuoikiltdd.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.projectcuoikiltdd.data.dao.YeuThichDao;
import com.example.projectcuoikiltdd.data.database.AppDatabase;
import com.example.projectcuoikiltdd.data.entity.DiaDiem;
import com.example.projectcuoikiltdd.data.entity.YeuThich;

import java.util.List;

public class YeuThichRepository {
    private final YeuThichDao yeuThichDao;

    public YeuThichRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        yeuThichDao = db.yeuThichDao();
    }

    public void themYeuThich(int idNguoiDung, int idDiadiem) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            YeuThich yt = new YeuThich(idNguoiDung, idDiadiem, System.currentTimeMillis());
            yeuThichDao.insert(yt);
        });
    }

    public void xoaYeuThich(int idNguoiDung, int idDiadiem) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            yeuThichDao.remove(idNguoiDung, idDiadiem);
        });
    }

    public LiveData<Boolean> isYeuThich(int idNguoiDung, int idDiadiem) {
        return yeuThichDao.isYeuThich(idNguoiDung, idDiadiem);
    }

    public LiveData<List<DiaDiem>> getDanhSachYeuThich(int idNguoiDung) {
        return yeuThichDao.getDanhSachYeuThich(idNguoiDung);
    }
}

