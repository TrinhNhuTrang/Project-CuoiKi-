package com.example.projectcuoikiltdd.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "DanhGia",
    foreignKeys = {
        @ForeignKey(
            entity = NguoiDung.class,
            parentColumns = "idNguoiDung",
            childColumns = "idNguoiDung",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = DiaDiem.class,
            parentColumns = "idDiadiem",
            childColumns = "idDiadiem",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {@Index("idNguoiDung"), @Index("idDiadiem")}
)
public class DanhGia {
    @PrimaryKey(autoGenerate = true)
    private int idDanhGia;

    @ColumnInfo(name = "idNguoiDung")
    private int idNguoiDung;

    @ColumnInfo(name = "idDiadiem")
    private int idDiadiem;

    @ColumnInfo(name = "soSao")
    private double soSao;

    @ColumnInfo(name = "noiDung")
    private String noiDung;

    @ColumnInfo(name = "hinhAnh")
    private String hinhAnh;

    @ColumnInfo(name = "ngayDanhGia")
    private long ngayDanhGia;

    public DanhGia(int idNguoiDung, int idDiadiem, double soSao, String noiDung, String hinhAnh, long ngayDanhGia) {
        this.idNguoiDung = idNguoiDung;
        this.idDiadiem = idDiadiem;
        this.soSao = soSao;
        this.noiDung = noiDung;
        this.hinhAnh = hinhAnh;
        this.ngayDanhGia = ngayDanhGia;
    }

    public int getIdDanhGia() {
        return idDanhGia;
    }

    public void setIdDanhGia(int idDanhGia) {
        this.idDanhGia = idDanhGia;
    }

    public int getIdNguoiDung() {
        return idNguoiDung;
    }

    public void setIdNguoiDung(int idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public int getIdDiadiem() {
        return idDiadiem;
    }

    public void setIdDiadiem(int idDiadiem) {
        this.idDiadiem = idDiadiem;
    }

    public double getSoSao() {
        return soSao;
    }

    public void setSoSao(double soSao) {
        this.soSao = soSao;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public long getNgayDanhGia() {
        return ngayDanhGia;
    }

    public void setNgayDanhGia(long ngayDanhGia) {
        this.ngayDanhGia = ngayDanhGia;
    }
}

