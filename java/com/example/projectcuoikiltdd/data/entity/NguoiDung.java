package com.example.projectcuoikiltdd.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "NguoiDung", indices = {@Index(value = {"email"}, unique = true)})
public class NguoiDung {
    @PrimaryKey(autoGenerate = true)
    private int idNguoiDung;

    @ColumnInfo(name = "hoTen")
    private String hoTen;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "matKhau")
    private String matKhau;

    @ColumnInfo(name = "soDienThoai")
    private String soDienThoai;

    public NguoiDung(String hoTen, String email, String matKhau, String soDienThoai) {
        this.hoTen = hoTen;
        this.email = email;
        this.matKhau = matKhau;
        this.soDienThoai = soDienThoai;
    }

    public int getIdNguoiDung() {
        return idNguoiDung;
    }

    public void setIdNguoiDung(int idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
}

