package com.example.projectcuoikiltdd.data.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
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
import java.util.ArrayList;
import java.util.List;
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
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // Chèn sẵn 1 người dùng mặc định để đảm bảo khóa ngoại bảng DanhGia luôn hợp lệ
            try {
                db.execSQL("INSERT OR IGNORE INTO NguoiDung (idNguoiDung, hoTen, email, matKhau) VALUES (1, 'Nguyễn Mai Linh', 'mailinh.travel@gmail.com', '12345678')");
            } catch (Exception ignored) {
                // Dự phòng nếu bảng NguoiDung có thêm cột không cho phép null
                try {
                    db.execSQL("INSERT OR IGNORE INTO NguoiDung (idNguoiDung, hoTen, email, soDienThoai, matKhau, anhDaiDien) VALUES (1, 'Nguyễn Mai Linh', 'mailinh.travel@gmail.com', '0912345678', '12345678', '')");
                } catch (Exception ignored2) {}
            }

            databaseWriteExecutor.execute(() -> {
                if (INSTANCE == null) return;
                DiaDiemDao ddDao = INSTANCE.diaDiemDao();
                DanhMucDao dmDao = INSTANCE.danhMucDao();

                // Nếu database chưa có địa điểm nào -> tự động nạp dữ liệu mẫu ngay lập tức
                if (ddDao.getCount() == 0) {
                    long idThienNhien = dmDao.insert(new DanhMuc("Thiên nhiên"));
                    long idDiTich = dmDao.insert(new DanhMuc("Di tích văn hóa"));
                    long idBienDao = dmDao.insert(new DanhMuc("Nghỉ dưỡng biển"));
                    long idAmThuc = dmDao.insert(new DanhMuc("Ẩm thực"));

                    List<DiaDiem> samplePlaces = new ArrayList<>();
                    samplePlaces.add(new DiaDiem((int) idDiTich, "Quần thể danh thắng Tràng An",
                            "Xã Trường Yên, Huyện Hoa Lư, Tỉnh Ninh Bình", 20.2506, 105.9745,
                            "Di sản Văn hóa và Thiên nhiên Thế giới được UNESCO công nhận kép với hệ thống hang động đá vôi kỳ vĩ.",
                            "img_trang_an", 250000));
                    samplePlaces.add(new DiaDiem((int) idThienNhien, "Ruộng bậc thang Mù Cang Chải",
                            "Huyện Mù Cang Chải, Tỉnh Yên Bái", 21.8492, 104.0864,
                            "Kỳ quan ruộng bậc thang vàng óng trên những sườn đồi Tây Bắc hùng vĩ.",
                            "img_ruong_bac_thang", 0));
                    samplePlaces.add(new DiaDiem((int) idDiTich, "Phố cổ Hội An",
                            "Phường Minh An, Thành phố Hội An, Tỉnh Quảng Nam", 15.8801, 108.3380,
                            "Đô thị cổ bên sông Hoài với kiến trúc nhà cổ rêu phong, đèn lồng rực rỡ về đêm.",
                            "img_photo1", 80000));
                    samplePlaces.add(new DiaDiem((int) idThienNhien, "Hồ Xuân Hương & Đồi Đa Phú",
                            "Thành phố Đà Lạt, Tỉnh Lâm Đồng", 11.9404, 108.4583,
                            "Không khí se lạnh đặc trưng của cao nguyên Lâm Viên, lý tưởng để săn mây và ngắm hoa dã quỳ.",
                            "img_photo2", 0));
                    samplePlaces.add(new DiaDiem((int) idBienDao, "Bãi Sao Phú Quốc",
                            "Phường An Thới, Thành phố Phú Quốc, Tỉnh Kiên Giang", 10.0564, 104.0353,
                            "Bãi cát trắng mịn như kem cùng làn nước biển xanh ngọc bích trong vắt.",
                            "img_photo1", 0));
                    samplePlaces.add(new DiaDiem((int) idBienDao, "Vịnh Hạ Long",
                            "Thành phố Hạ Long, Tỉnh Quảng Ninh", 20.9101, 107.1839,
                            "Kỳ quan thiên nhiên thế giới với hàng nghìn đảo đá vôi lớn nhỏ trên mặt vịnh xanh.",
                            "img_trang_an", 290000));
                    samplePlaces.add(new DiaDiem((int) idAmThuc, "Chợ Bến Thành",
                            "Quận 1, Thành phố Hồ Chí Minh", 10.7725, 106.6980,
                            "Biểu tượng văn hóa và thiên đường ẩm thực đường phố đặc sắc của Sài Gòn.",
                            "img_photo2", 0));
                    samplePlaces.add(new DiaDiem((int) idThienNhien, "Đỉnh Fansipan",
                            "Thị xã Sa Pa, Tỉnh Lào Cai", 22.3033, 103.7750,
                            "Nóc nhà Đông Dương ở độ cao 3.143m giữa biển mây Hoàng Liên Sơn.",
                            "img_ruong_bac_thang", 800000));

                    ddDao.insertAll(samplePlaces);
                }
            });
        }
    };
}