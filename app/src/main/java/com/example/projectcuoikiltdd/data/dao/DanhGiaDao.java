package com.example.projectcuoikiltdd.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.projectcuoikiltdd.data.entity.DanhGia;
import java.util.List;

@Dao
public interface DanhGiaDao {
    @Insert
    long insert(DanhGia danhGia);

    @Update
    void update(DanhGia danhGia);

    @Delete
    void delete(DanhGia danhGia);

    @Query("SELECT * FROM DanhGia WHERE idDiadiem = :idDiadiem ORDER BY ngayDanhGia DESC")
    LiveData<List<DanhGia>> getByDiaDiem(int idDiadiem);

    @Query("SELECT AVG(soSao) FROM DanhGia WHERE idDiadiem = :idDiadiem")
    LiveData<Double> getDiemTrungBinh(int idDiadiem);

    @Query("SELECT COUNT(*) FROM DanhGia WHERE idDiadiem = :idDiadiem")
    LiveData<Integer> getSoLuongDanhGia(int idDiadiem);
}