package com.viethanh.backend.diadiem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class DiaDiemRepository {
    private static final String SELECT_DIA_DIEM = """
            SELECT d.id, d.ten, d.dia_chi, d.tinh_id, t.ten AS ten_tinh,
                   d.loai_id, l.ten AS ten_loai, d.mo_ta,
                   d.vi_do, d.kinh_do, d.hinh_anh, d.gia_ve
            FROM dia_diem d
            JOIN tinh t ON t.id = d.tinh_id
            JOIN loai_dia_diem l ON l.id = d.loai_id
            """;

    private final JdbcClient jdbc;

    public DiaDiemRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public List<DiaDiem> tim(int tinhId, int loaiId, String tuKhoa) {
        String mauTimKiem = "%" + tuKhoa.strip().toLowerCase(Locale.ROOT)
                .replace("!", "!!").replace("%", "!%").replace("_", "!_") + "%";

        return jdbc.sql(SELECT_DIA_DIEM + """
                        WHERE (:tinhId = 0 OR d.tinh_id = :tinhId)
                          AND (:loaiId = 0 OR d.loai_id = :loaiId)
                          AND (LOWER(d.ten) LIKE :tuKhoa ESCAPE '!'
                               OR LOWER(d.dia_chi) LIKE :tuKhoa ESCAPE '!')
                        ORDER BY d.id
                        """)
                .param("tinhId", tinhId)
                .param("loaiId", loaiId)
                .param("tuKhoa", mauTimKiem)
                .query(DiaDiemRepository::docDiaDiem)
                .list();
    }

    public Optional<DiaDiem> timTheoId(int id) {
        return jdbc.sql(SELECT_DIA_DIEM + "WHERE d.id = :id")
                .param("id", id)
                .query(DiaDiemRepository::docDiaDiem)
                .optional();
    }

    public List<BoLoc.LuaChon> layTinh() {
        return jdbc.sql("SELECT id, ten FROM tinh ORDER BY id")
                .query(BoLoc.LuaChon.class)
                .list();
    }

    public List<BoLoc.LuaChon> layLoaiDiaDiem() {
        return jdbc.sql("SELECT id, ten FROM loai_dia_diem ORDER BY id")
                .query(BoLoc.LuaChon.class)
                .list();
    }

    private static DiaDiem docDiaDiem(ResultSet row, int rowNum) throws SQLException {
        return new DiaDiem(
                row.getInt("id"),
                row.getString("ten"),
                row.getString("dia_chi"),
                row.getInt("tinh_id"),
                row.getString("ten_tinh"),
                row.getInt("loai_id"),
                row.getString("ten_loai"),
                row.getString("mo_ta"),
                row.getObject("vi_do", Double.class),
                row.getObject("kinh_do", Double.class),
                row.getString("hinh_anh"),
                row.getBigDecimal("gia_ve"));
    }
}
