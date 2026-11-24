package com.example.projectcuoikiltdd;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectcuoikiltdd.data.entity.DiaDiem;
import com.example.projectcuoikiltdd.databinding.ActivityMapsBinding;
import com.example.projectcuoikiltdd.viewmodel.DiaDiemViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng hcm = new LatLng(10.7769, 106.7009);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcm, 13));

        // Lắng nghe dữ liệu địa điểm du lịch từ SQLite (Room Database) và tự động cắm Marker
        DiaDiemViewModel diaDiemViewModel = new ViewModelProvider(this).get(DiaDiemViewModel.class);
        diaDiemViewModel.getAll().observe(this, listDiaDiem -> {
            if (listDiaDiem != null && !listDiaDiem.isEmpty()) {
                googleMap.clear();
                for (DiaDiem diaDiem : listDiaDiem) {
                    LatLng point = new LatLng(diaDiem.getViDo(), diaDiem.getKinhDo());
                    googleMap.addMarker(new MarkerOptions()
                            .position(point)
                            .title(diaDiem.getTenDiaDiem())
                            .snippet(diaDiem.getDiaChi() + " - Giá vé: " + diaDiem.getGiaVe() + "đ"));
                }
            }
        });
    }
}
