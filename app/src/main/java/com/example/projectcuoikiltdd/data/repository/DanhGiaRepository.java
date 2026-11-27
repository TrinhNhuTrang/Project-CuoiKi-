package com.example.projectcuoikiltdd.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.projectcuoikiltdd.data.dao.DanhGiaDao;
import com.example.projectcuoikiltdd.data.database.AppDatabase;
import com.example.projectcuoikiltdd.data.entity.DanhGia;
import java.util.List;

public class DanhGiaRepository {
    private final DanhGiaDao danhGiaDao;

    public DanhGiaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        danhGiaDao = db.danhGiaDao();
    }

    public LiveData<List<DanhGia>> getByDiaDiem(int idDiaDiem) {
        return danhGiaDao.getByDiaDiem(idDiaDiem);
    }

    public LiveData<Double> getDiemTrungBinh(int idDiaDiem) {
        return danhGiaDao.getDiemTrungBinh(idDiaDiem);
    }

    public LiveData<Integer> getSoLuongDanhGia(int idDiaDiem) {
        return danhGiaDao.getSoLuongDanhGia(idDiaDiem);
    }

    public void insert(DanhGia danhGia) {
        AppDatabase.databaseWriteExecutor.execute(() -> danhGiaDao.insert(danhGia));
    }

    public void update(DanhGia danhGia) {
        AppDatabase.databaseWriteExecutor.execute(() -> danhGiaDao.update(danhGia));
    }

    public void delete(DanhGia danhGia) {
        AppDatabase.databaseWriteExecutor.execute(() -> danhGiaDao.delete(danhGia));
    }
}
