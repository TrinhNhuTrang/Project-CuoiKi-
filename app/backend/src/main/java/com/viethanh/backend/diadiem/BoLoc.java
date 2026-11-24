package com.viethanh.backend.diadiem;

import java.util.List;

public record BoLoc(List<LuaChon> tinh, List<LuaChon> loaiDiaDiem) {
    public record LuaChon(int id, String ten) {}
}
