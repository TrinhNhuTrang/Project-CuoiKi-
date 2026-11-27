package com.example.projectcuoikiltdd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectcuoikiltdd.data.entity.DiaDiem;
import com.example.projectcuoikiltdd.viewmodel.DanhGiaViewModel;
import com.example.projectcuoikiltdd.viewmodel.DiaDiemViewModel;
import com.google.android.gms.maps.model.LatLng;
import java.text.NumberFormat;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_PLACE_ID = "place_id";
    static final String EXTRA_PLACE_NAME = "place_name";
    static final String EXTRA_PLACE_ADDRESS = "place_address";
    static final String EXTRA_PLACE_DESCRIPTION = "place_description";
    static final String EXTRA_PLACE_LATITUDE = "place_latitude";
    static final String EXTRA_PLACE_LONGITUDE = "place_longitude";

    private int placeId = -1;
    private DiaDiem currentPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnBackTop).setOnClickListener(v -> finish());

        placeId = getIntent().getIntExtra(EXTRA_PLACE_ID, -1);

        // Chuyển sang màn Đánh giá mang theo ID địa điểm
        findViewById(R.id.tabReview).setOnClickListener(v -> openReviewScreen());
        findViewById(R.id.placeRating).setOnClickListener(v -> openReviewScreen());

        // Nút thêm vào lịch trình -> chuyển sang TaoChuyenDiActivity kèm tên địa điểm
        findViewById(R.id.btnAddPlan).setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, TaoChuyenDiActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Chuyển sang tạo lịch trình cho điểm đến này", Toast.LENGTH_SHORT).show();
        });

        DiaDiemViewModel diaDiemViewModel = new ViewModelProvider(this).get(DiaDiemViewModel.class);
        DanhGiaViewModel danhGiaViewModel = new ViewModelProvider(this).get(DanhGiaViewModel.class);

        if (placeId != -1) {
            diaDiemViewModel.getById(placeId).observe(this, diaDiem -> {
                if (diaDiem != null) {
                    currentPlace = diaDiem;
                    bindPlaceData(diaDiem);
                }
            });

            // Cập nhật điểm trung bình & số lượng đánh giá theo thời gian thực
            TextView tvRating = findViewById(R.id.placeRating);
            TextView tvRatingCount = findViewById(R.id.placeRatingCount);

            danhGiaViewModel.getDiemTrungBinh(placeId).observe(this, avg -> {
                if (avg != null && avg > 0) {
                    tvRating.setText(String.format(Locale.US, "⭐ %.1f", avg));
                } else {
                    tvRating.setText("⭐ Chưa có");
                }
            });

            danhGiaViewModel.getSoLuongDanhGia(placeId).observe(this, count -> {
                int total = count != null ? count : 0;
                tvRatingCount.setText(total + " đánh giá");
            });

            // Hiển thị danh sách đánh giá nổi bật ngay trên màn Detail
            RecyclerView rvHighlights = findViewById(R.id.placeReviewHighlights);
            DanhGiaAdapter highlightAdapter = new DanhGiaAdapter(1, new DanhGiaAdapter.OnReviewActionListener() {
                @Override public void onEdit(com.example.projectcuoikiltdd.data.entity.DanhGia danhGia) { openReviewScreen(); }
                @Override public void onDelete(com.example.projectcuoikiltdd.data.entity.DanhGia danhGia) { danhGiaViewModel.delete(danhGia); }
            });
            rvHighlights.setAdapter(highlightAdapter);
            danhGiaViewModel.getByDiaDiem(placeId).observe(this, highlightAdapter::setData);
        } else {
            // Dự phòng nếu mở từ Intent truyền chuỗi trực tiếp
            Intent place = getIntent();
            String name = place.getStringExtra(EXTRA_PLACE_NAME);
            String address = place.getStringExtra(EXTRA_PLACE_ADDRESS);
            ((TextView) findViewById(R.id.placeName)).setText(name);
            ((TextView) findViewById(R.id.placeAddress)).setText(address);
            ((TextView) findViewById(R.id.placeDescription)).setText(place.getStringExtra(EXTRA_PLACE_DESCRIPTION));

            double latitude = place.getDoubleExtra(EXTRA_PLACE_LATITUDE, Double.NaN);
            double longitude = place.getDoubleExtra(EXTRA_PLACE_LONGITUDE, Double.NaN);
            LatLng location = null;
            if (latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180) {
                location = new LatLng(latitude, longitude);
            }
            PlaceMap.bind(this, location, name, address);
        }
    }

    private void bindPlaceData(DiaDiem diaDiem) {
        ((TextView) findViewById(R.id.placeName)).setText(diaDiem.getTenDiaDiem());
        ((TextView) findViewById(R.id.placeAddress)).setText("📍 " + diaDiem.getDiaChi());
        ((TextView) findViewById(R.id.placeDescription)).setText(diaDiem.getMoTa());
        ((TextView) findViewById(R.id.placeBadge)).setText("🌿 ĐIỂM ĐẾN VIỆT HÀNH");
        ((TextView) findViewById(R.id.placeVisitDuration)).setText("3 - 4 giờ");

        TextView tvPrice = findViewById(R.id.placePrice);
        TextView tvPriceUnit = findViewById(R.id.placePriceUnit);
        if (diaDiem.getGiaVe() <= 0) {
            tvPrice.setText("Miễn phí");
            tvPriceUnit.setText("Vé tham quan");
        } else {
            NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
            tvPrice.setText(nf.format(diaDiem.getGiaVe()) + "đ");
            tvPriceUnit.setText("Giá vé / người");
        }

        ImageView imgCover = findViewById(R.id.placeCover);
        imgCover.setImageResource(DiaDiemAdapter.resolveImageRes(this, diaDiem.getHinhAnh()));

        LatLng location = null;
        if (diaDiem.getViDo() >= -90 && diaDiem.getViDo() <= 90
                && diaDiem.getKinhDo() >= -180 && diaDiem.getKinhDo() <= 180
                && (diaDiem.getViDo() != 0 || diaDiem.getKinhDo() != 0)) {
            location = new LatLng(diaDiem.getViDo(), diaDiem.getKinhDo());
        }
        PlaceMap.bind(this, location, diaDiem.getTenDiaDiem(), diaDiem.getDiaChi());
    }

    private void openReviewScreen() {
        Intent intent = new Intent(DetailActivity.this, ReviewActivity.class);
        intent.putExtra(EXTRA_PLACE_ID, placeId);
        startActivity(intent);
    }
}