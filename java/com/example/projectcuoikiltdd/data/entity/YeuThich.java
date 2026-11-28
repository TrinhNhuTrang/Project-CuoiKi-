package com.example.projectcuoikiltdd.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "YeuThich",
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
public class YeuThich {
    @PrimaryKey(autoGenerate = true)
    private int idYeuThich;

    @ColumnInfo(name = "idNguoiDung")
    private int idNguoiDung;

    @ColumnInfo(name = "idDiadiem")
    private int idDiadiem;

    @ColumnInfo(name = "ngayTao")
    private long ngayTao;

    public YeuThich(int idNguoiDung, int idDiadiem, long ngayTao) {
        this.idNguoiDung = idNguoiDung;
        this.idDiadiem = idDiadiem;
        this.ngayTao = ngayTao;
    }

    public int getIdYeuThich() {
        return idYeuThich;
    }

    public void setIdYeuThich(int idYeuThich) {
        this.idYeuThich = idYeuThich;
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

    public long getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(long ngayTao) {
        this.ngayTao = ngayTao;
    }
}

