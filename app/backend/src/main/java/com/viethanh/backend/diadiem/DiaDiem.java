package com.viethanh.backend.diadiem;

import java.math.BigDecimal;

public record DiaDiem(
        int id,
        String tenDiaDiem,
        String diaChi,
        int tinhId,
        String tenTinh,
        int loaiId,
        String tenLoai,
        String moTa,
        Double viDo,
        Double kinhDo,
        String hinhAnh,
        BigDecimal giaVe
) {}
