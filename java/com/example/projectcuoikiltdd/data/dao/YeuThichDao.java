package com.example.projectcuoikiltdd.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.projectcuoikiltdd.data.entity.DiaDiem;
import com.example.projectcuoikiltdd.data.entity.YeuThich;

import java.util.List;

@Dao
public interface YeuThichDao {
    @Insert
    long insert(YeuThich yeuThich);

    @Delete
    void delete(YeuThich yeuThich);

    @Query("DELETE FROM YeuThich WHERE idNguoiDung = :idNguoiDung AND idDiadiem = :idDiadiem")
    void remove(int idNguoiDung, int idDiadiem);

    @Query("SELECT EXISTS(SELECT 1 FROM YeuThich WHERE idNguoiDung = :idNguoiDung AND idDiadiem = :idDiadiem)")
    LiveData<Boolean> isYeuThich(int idNguoiDung, int idDiadiem);

    @Query("SELECT d.* FROM DiaDiem d " +
           "INNER JOIN YeuThich y ON d.idDiadiem = y.idDiadiem " +
           "WHERE y.idNguoiDung = :idNguoiDung " +
           "ORDER BY y.ngayTao DESC")
    LiveData<List<DiaDiem>> getDanhSachYeuThich(int idNguoiDung);
}

