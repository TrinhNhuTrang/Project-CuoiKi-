package com.example.projectcuoikiltdd.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.projectcuoikiltdd.data.entity.DanhMuc;
import com.example.projectcuoikiltdd.data.repository.DanhMucRepository;
import java.util.List;

public class DanhMucViewModel extends AndroidViewModel {
    private final DanhMucRepository repository;

    public DanhMucViewModel(@NonNull Application application) {
        super(application);
        repository = new DanhMucRepository(application);
    }

    public LiveData<List<DanhMuc>> getAll() {
        return repository.getAll();
    }
}
