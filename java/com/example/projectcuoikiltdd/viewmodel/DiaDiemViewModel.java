package com.example.projectcuoikiltdd.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.projectcuoikiltdd.data.entity.DiaDiem;
import com.example.projectcuoikiltdd.data.repository.DiaDiemRepository;

import java.util.List;

public class DiaDiemViewModel extends AndroidViewModel {
    private final DiaDiemRepository repository;
    private final LiveData<List<DiaDiem>> allDiaDiem;

    public DiaDiemViewModel(@NonNull Application application) {
        super(application);
        repository = new DiaDiemRepository(application);
        allDiaDiem = repository.getAll();
    }

    public LiveData<List<DiaDiem>> getAll() {
        return allDiaDiem;
    }

    public LiveData<List<DiaDiem>> getByDanhMuc(int idDanhMuc) {
        return repository.getByDanhMuc(idDanhMuc);
    }

    public LiveData<List<DiaDiem>> search(String keyword) {
        return repository.search(keyword);
    }

    public LiveData<DiaDiem> getById(int id) {
        return repository.getById(id);
    }

    public void insert(DiaDiem diaDiem) {
        repository.insert(diaDiem);
    }

    public void update(DiaDiem diaDiem) {
        repository.update(diaDiem);
    }

    public void delete(DiaDiem diaDiem) {
        repository.delete(diaDiem);
    }
}

