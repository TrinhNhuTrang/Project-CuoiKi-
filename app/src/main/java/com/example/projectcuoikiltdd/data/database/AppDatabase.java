package com.example.projectcuoikiltdd.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.projectcuoikiltdd.data.dao.ChuyenDiDao;
import com.example.projectcuoikiltdd.data.dao.DanhGiaDao;
import com.example.projectcuoikiltdd.data.dao.DanhMucDao;
import com.example.projectcuoikiltdd.data.dao.DiaDiemDao;
import com.example.projectcuoikiltdd.data.dao.NguoiDungDao;
import com.example.projectcuoikiltdd.data.dao.YeuThichDao;
import com.example.projectcuoikiltdd.data.entity.ChuyenDi;
import com.example.projectcuoikiltdd.data.entity.DanhGia;
import com.example.projectcuoikiltdd.data.entity.DanhMuc;
import com.example.projectcuoikiltdd.data.entity.DiaDiem;
import com.example.projectcuoikiltdd.data.entity.NguoiDung;
import com.example.projectcuoikiltdd.data.entity.YeuThich;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
    entities = {
        NguoiDung.class,
        DanhMuc.class,
        DiaDiem.class,
        YeuThich.class,
        ChuyenDi.class,
        DanhGia.class
    },
    version = 2,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract NguoiDungDao nguoiDungDao();
    public abstract DanhMucDao danhMucDao();
    public abstract DiaDiemDao diaDiemDao();
    public abstract YeuThichDao yeuThichDao();
    public abstract ChuyenDiDao chuyenDiDao();
    public abstract DanhGiaDao danhGiaDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "dulich_db.db"
                    )
                    .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
