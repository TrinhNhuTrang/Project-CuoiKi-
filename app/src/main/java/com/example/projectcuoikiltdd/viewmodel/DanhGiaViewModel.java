package com.example.projectcuoikiltdd.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.projectcuoikiltdd.data.entity.DanhGia;
import com.example.projectcuoikiltdd.data.repository.DanhGiaRepository;
import java.util.List;

public class DanhGiaViewModel extends AndroidViewModel {
    private final DanhGiaRepository repository;

    public DanhGiaViewModel(@NonNull Application application) {
        super(application);
        repository = new DanhGiaRepository(application);
    }

    public LiveData<List<DanhGia>> getByDiaDiem(int idDiaDiem) {
        return repository.getByDiaDiem(idDiaDiem);
    }

    public LiveData<Double> getDiemTrungBinh(int idDiaDiem) {
        return repository.getDiemTrungBinh(idDiaDiem);
    }

    public LiveData<Integer> getSoLuongDanhGia(int idDiaDiem) {
        return repository.getSoLuongDanhGia(idDiaDiem);
    }

    public void insert(DanhGia danhGia) {
        repository.insert(danhGia);
    }

    public void update(DanhGia danhGia) {
        repository.update(danhGia);
    }

    public void delete(DanhGia danhGia) {
        repository.delete(danhGia);
    }
}
