package com.example.projectcuoikiltdd.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projectcuoikiltdd.data.entity.DiaDiem;

import java.util.List;

@Dao
public interface DiaDiemDao {
    @Insert
    long insert(DiaDiem diaDiem);

    @Insert
    void insertAll(List<DiaDiem> list);

    @Update
    void update(DiaDiem diaDiem);

    @Delete
    void delete(DiaDiem diaDiem);

    @Query("SELECT * FROM DiaDiem ORDER BY idDiadiem DESC")
    LiveData<List<DiaDiem>> getAll();

    @Query("SELECT * FROM DiaDiem ORDER BY idDiadiem DESC")
    List<DiaDiem> getAllSync();

    @Query("SELECT * FROM DiaDiem WHERE idDanhMuc = :idDanhMuc")
    LiveData<List<DiaDiem>> getByDanhMuc(int idDanhMuc);

    @Query("SELECT * FROM DiaDiem WHERE tenDiaDiem LIKE '%' || :keyword || '%' OR diaChi LIKE '%' || :keyword || '%'")
    LiveData<List<DiaDiem>> search(String keyword);

    @Query("SELECT * FROM DiaDiem WHERE idDiadiem = :id LIMIT 1")
    LiveData<DiaDiem> getById(int id);

    @Query("SELECT COUNT(*) FROM DiaDiem")
    int getCount();
}

