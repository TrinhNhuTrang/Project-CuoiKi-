package com.example.projectcuoikiltdd.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "ChuyenDi",
    foreignKeys = @ForeignKey(
        entity = NguoiDung.class,
        parentColumns = "idNguoiDung",
        childColumns = "idNguoiDung",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("idNguoiDung")}
)
public class ChuyenDi {
    @PrimaryKey(autoGenerate = true)
    private int idChuyenDi;

    @ColumnInfo(name = "idNguoiDung")
    private int idNguoiDung;

    @ColumnInfo(name = "tenChuyenDi")
    private String tenChuyenDi;

    @ColumnInfo(name = "diaChi")
    private String diaChi;

    @ColumnInfo(name = "ngayBatDau")
    private long ngayBatDau;

    @ColumnInfo(name = "ngayKetThuc")
    private long ngayKetThuc;

    @ColumnInfo(name = "chiPhiDuKien")
    private double chiPhiDuKien;

    public ChuyenDi(int idNguoiDung, String tenChuyenDi, String diaChi, long ngayBatDau, long ngayKetThuc, double chiPhiDuKien) {
        this.idNguoiDung = idNguoiDung;
        this.tenChuyenDi = tenChuyenDi;
        this.diaChi = diaChi;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.chiPhiDuKien = chiPhiDuKien;
    }

    public int getIdChuyenDi() {
        return idChuyenDi;
    }

    public void setIdChuyenDi(int idChuyenDi) {
        this.idChuyenDi = idChuyenDi;
    }

    public int getIdNguoiDung() {
        return idNguoiDung;
    }

    public void setIdNguoiDung(int idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public String getTenChuyenDi() {
        return tenChuyenDi;
    }

    public void setTenChuyenDi(String tenChuyenDi) {
        this.tenChuyenDi = tenChuyenDi;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public long getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(long ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public long getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(long ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public double getChiPhiDuKien() {
        return chiPhiDuKien;
    }

    public void setChiPhiDuKien(double chiPhiDuKien) {
        this.chiPhiDuKien = chiPhiDuKien;
    }
}
