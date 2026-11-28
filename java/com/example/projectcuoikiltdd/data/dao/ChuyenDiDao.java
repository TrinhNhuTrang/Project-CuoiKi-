package com.example.projectcuoikiltdd.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projectcuoikiltdd.data.entity.ChuyenDi;

import java.util.List;

@Dao
public interface ChuyenDiDao {
    @Insert
    long insert(ChuyenDi chuyenDi);

    @Update
    void update(ChuyenDi chuyenDi);

    @Delete
    void delete(ChuyenDi chuyenDi);

    @Query("SELECT * FROM ChuyenDi WHERE idNguoiDung = :idNguoiDung ORDER BY ngayBatDau DESC")
    LiveData<List<ChuyenDi>> getByNguoiDung(int idNguoiDung);

    @Query("SELECT * FROM ChuyenDi WHERE idChuyenDi = :id LIMIT 1")
    LiveData<ChuyenDi> getById(int id);
}

