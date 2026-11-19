package com.example.projectcuoikiltdd.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projectcuoikiltdd.data.entity.NguoiDung;

import java.util.List;

@Dao
public interface NguoiDungDao {
    @Insert
    long insert(NguoiDung nguoiDung);

    @Update
    void update(NguoiDung nguoiDung);

    @Delete
    void delete(NguoiDung nguoiDung);

    @Query("SELECT * FROM NguoiDung WHERE email = :email AND matKhau = :matKhau LIMIT 1")
    NguoiDung login(String email, String matKhau);

    @Query("SELECT * FROM NguoiDung WHERE email = :email LIMIT 1")
    NguoiDung getByEmail(String email);

    @Query("SELECT * FROM NguoiDung WHERE idNguoiDung = :id LIMIT 1")
    LiveData<NguoiDung> getById(int id);

    @Query("SELECT * FROM NguoiDung ORDER BY idNguoiDung DESC")
    LiveData<List<NguoiDung>> getAll();
}

