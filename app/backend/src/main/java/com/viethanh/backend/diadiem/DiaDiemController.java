package com.viethanh.backend.diadiem;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/dia-diem")
public class DiaDiemController {
    private final DiaDiemRepository repository;

    public DiaDiemController(DiaDiemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<DiaDiem> timDiaDiem(
            @RequestParam(name = "tinhId", defaultValue = "0") int tinhId,
            @RequestParam(name = "loaiId", defaultValue = "0") int loaiId,
            @RequestParam(name = "tuKhoa", defaultValue = "") String tuKhoa) {
        if (tinhId < 0 || loaiId < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Mã tỉnh và mã loại địa điểm phải từ 0 trở lên");
        }
        return repository.tim(tinhId, loaiId, tuKhoa);
    }

    @GetMapping("/bo-loc")
    public BoLoc layBoLoc() {
        return new BoLoc(repository.layTinh(), repository.layLoaiDiaDiem());
    }

    @GetMapping("/{id}")
    public DiaDiem layChiTiet(@PathVariable("id") int id) {
        return repository.timTheoId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy địa điểm"));
    }
}
