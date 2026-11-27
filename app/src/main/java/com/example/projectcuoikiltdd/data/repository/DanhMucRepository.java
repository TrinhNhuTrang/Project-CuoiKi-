package com.example.projectcuoikiltdd.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.projectcuoikiltdd.data.dao.DanhMucDao;
import com.example.projectcuoikiltdd.data.database.AppDatabase;
import com.example.projectcuoikiltdd.data.entity.DanhMuc;
import java.util.List;

public class DanhMucRepository {
    private final DanhMucDao danhMucDao;

    public DanhMucRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        danhMucDao = db.danhMucDao();
    }

    public LiveData<List<DanhMuc>> getAll() {
        return danhMucDao.getAll();
    }
}
