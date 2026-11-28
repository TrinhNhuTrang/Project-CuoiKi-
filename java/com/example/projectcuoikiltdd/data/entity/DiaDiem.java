package com.example.projectcuoikiltdd.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "DiaDiem",
    foreignKeys = @ForeignKey(
        entity = DanhMuc.class,
        parentColumns = "idDanhMuc",
        childColumns = "idDanhMuc",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("idDanhMuc")}
)
public class DiaDiem {
    @PrimaryKey(autoGenerate = true)
    private int idDiadiem;

    @ColumnInfo(name = "idDanhMuc")
    private int idDanhMuc;

    @ColumnInfo(name = "tenDiaDiem")
    private String tenDiaDiem;

    @ColumnInfo(name = "diaChi")
    private String diaChi;

    @ColumnInfo(name = "viDo")
    private double viDo;

    @ColumnInfo(name = "kinhDo")
    private double kinhDo;

    @ColumnInfo(name = "moTa")
    private String moTa;

    @ColumnInfo(name = "hinhAnh")
    private String hinhAnh;

    @ColumnInfo(name = "giaVe")
    private double giaVe;

    public DiaDiem(int idDanhMuc, String tenDiaDiem, String diaChi, double viDo, double kinhDo, String moTa, String hinhAnh, double giaVe) {
        this.idDanhMuc = idDanhMuc;
        this.tenDiaDiem = tenDiaDiem;
        this.diaChi = diaChi;
        this.viDo = viDo;
        this.kinhDo = kinhDo;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
        this.giaVe = giaVe;
    }

    public int getIdDiadiem() {
        return idDiadiem;
    }

    public void setIdDiadiem(int idDiadiem) {
        this.idDiadiem = idDiadiem;
    }

    public int getIdDanhMuc() {
        return idDanhMuc;
    }

    public void setIdDanhMuc(int idDanhMuc) {
        this.idDanhMuc = idDanhMuc;
    }

    public String getTenDiaDiem() {
        return tenDiaDiem;
    }

    public void setTenDiaDiem(String tenDiaDiem) {
        this.tenDiaDiem = tenDiaDiem;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public double getViDo() {
        return viDo;
    }

    public void setViDo(double viDo) {
        this.viDo = viDo;
    }

    public double getKinhDo() {
        return kinhDo;
    }

    public void setKinhDo(double kinhDo) {
        this.kinhDo = kinhDo;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public double getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(double giaVe) {
        this.giaVe = giaVe;
    }
}

