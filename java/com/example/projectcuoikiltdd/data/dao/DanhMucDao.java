package com.example.projectcuoikiltdd.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projectcuoikiltdd.data.entity.DanhMuc;

import java.util.List;

@Dao
public interface DanhMucDao {
    @Insert
    long insert(DanhMuc danhMuc);

    @Insert
    void insertAll(List<DanhMuc> list);

    @Update
    void update(DanhMuc danhMuc);

    @Delete
    void delete(DanhMuc danhMuc);

    @Query("SELECT * FROM DanhMuc ORDER BY tenDanhMuc ASC")
    LiveData<List<DanhMuc>> getAll();

    @Query("SELECT * FROM DanhMuc WHERE idDanhMuc = :id LIMIT 1")
    DanhMuc getById(int id);

    @Query("SELECT COUNT(*) FROM DanhMuc")
    int getCount();
}

