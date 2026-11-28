package com.example.projectcuoikiltdd.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.projectcuoikiltdd.data.entity.ChuyenDi;
import com.example.projectcuoikiltdd.data.repository.ChuyenDiRepository;

import java.util.List;

public class ChuyenDiViewModel extends AndroidViewModel {
    private final ChuyenDiRepository repository;

    public ChuyenDiViewModel(@NonNull Application application) {
        super(application);
        repository = new ChuyenDiRepository(application);
    }

    public LiveData<List<ChuyenDi>> getByNguoiDung(int idNguoiDung) {
        return repository.getByNguoiDung(idNguoiDung);
    }

    public LiveData<ChuyenDi> getById(int id) {
        return repository.getById(id);
    }

    public void insert(ChuyenDi chuyenDi) {
        repository.insert(chuyenDi);
    }

    public void update(ChuyenDi chuyenDi) {
        repository.update(chuyenDi);
    }

    public void delete(ChuyenDi chuyenDi) {
        repository.delete(chuyenDi);
    }
}
