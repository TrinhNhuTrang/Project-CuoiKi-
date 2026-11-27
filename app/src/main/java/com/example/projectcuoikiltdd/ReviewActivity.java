package com.example.projectcuoikiltdd;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectcuoikiltdd.data.entity.DanhGia;
import com.example.projectcuoikiltdd.viewmodel.DanhGiaViewModel;
import com.example.projectcuoikiltdd.viewmodel.DiaDiemViewModel;
import java.util.List;
import java.util.Locale;

public class ReviewActivity extends AppCompatActivity {
    private static final int CURRENT_USER_ID = 1;

    private TextView[] stars;
    private int rating = 0;
    private int placeId = -1;
    private DanhGia editingReview = null;

    private EditText edtReview;
    private TextView btnSubmit;
    private DanhGiaViewModel danhGiaViewModel;
    private DanhGiaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        placeId = getIntent().getIntExtra(DetailActivity.EXTRA_PLACE_ID, 1);

        edtReview = findViewById(R.id.edtReview);
        btnSubmit = findViewById(R.id.btnSubmit);

        stars = new TextView[]{
                findViewById(R.id.star1), findViewById(R.id.star2),
                findViewById(R.id.star3), findViewById(R.id.star4),
                findViewById(R.id.star5)
        };

        for (int i = 0; i < stars.length; i++) {
            final int value = i + 1;
            stars[i].setOnClickListener(v -> {
                rating = value;
                updateStars();
            });
        }

        danhGiaViewModel = new ViewModelProvider(this).get(DanhGiaViewModel.class);
        DiaDiemViewModel diaDiemViewModel = new ViewModelProvider(this).get(DiaDiemViewModel.class);

        // Hiển thị thông tin địa điểm đang đánh giá
        diaDiemViewModel.getById(placeId).observe(this, diaDiem -> {
            if (diaDiem != null) {
                ((TextView) findViewById(R.id.reviewPlaceBadge)).setText("🌿 ĐÁNH GIÁ ĐIỂM ĐẾN");
                ((TextView) findViewById(R.id.reviewPlaceName)).setText(diaDiem.getTenDiaDiem());
                ((TextView) findViewById(R.id.reviewPlaceAddress)).setText("📍 " + diaDiem.getDiaChi());
            }
        });

        // Danh sách đánh giá cộng đồng + Sửa / Xóa đánh giá của mình
        RecyclerView rvReviews = findViewById(R.id.communityReviews);
        adapter = new DanhGiaAdapter(CURRENT_USER_ID, new DanhGiaAdapter.OnReviewActionListener() {
            @Override
            public void onEdit(DanhGia danhGia) {
                editingReview = danhGia;
                rating = (int) Math.round(danhGia.getSoSao());
                updateStars();
                edtReview.setText(danhGia.getNoiDung());
                edtReview.requestFocus();
                btnSubmit.setText("💾 Cập nhật đánh giá");
            }

            @Override
            public void onDelete(DanhGia danhGia) {
                new AlertDialog.Builder(ReviewActivity.this)
                        .setTitle("Xóa đánh giá")
                        .setMessage("Bạn có chắc chắn muốn xóa đánh giá này không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            danhGiaViewModel.delete(danhGia);
                            if (editingReview != null && editingReview.getIdDanhGia() == danhGia.getIdDanhGia()) {
                                resetForm();
                            }
                            Toast.makeText(ReviewActivity.this, "Đã xóa đánh giá", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
        rvReviews.setAdapter(adapter);

        // Lắng nghe thay đổi danh sách đánh giá để tính điểm trung bình & phân bổ sao
        danhGiaViewModel.getByDiaDiem(placeId).observe(this, list -> {
            adapter.setData(list);
            updateStatistics(list);
        });

        // Nút Gửi / Cập nhật đánh giá
        btnSubmit.setOnClickListener(v -> submitReview());
    }

    private void submitReview() {
        // Kiểm tra số sao hợp lệ (1 - 5 sao)
        if (rating < 1 || rating > 5) {
            Toast.makeText(this, "Vui lòng chọn số sao đánh giá (từ 1 đến 5 sao)", Toast.LENGTH_SHORT).show();
            return;
        }

        String content = edtReview.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            edtReview.setError("Vui lòng nhập nội dung nhận xét");
            edtReview.requestFocus();
            return;
        }

        if (editingReview != null) {
            editingReview.setSoSao(rating);
            editingReview.setNoiDung(content);
            editingReview.setNgayDanhGia(System.currentTimeMillis());
            danhGiaViewModel.update(editingReview);
            Toast.makeText(this, "Đã cập nhật đánh giá!", Toast.LENGTH_SHORT).show();
        } else {
            DanhGia newReview = new DanhGia(
                    CURRENT_USER_ID,
                    placeId,
                    rating,
                    content,
                    "",
                    System.currentTimeMillis()
            );
            danhGiaViewModel.insert(newReview);
            Toast.makeText(this, "Cảm ơn bạn đã gửi đánh giá!", Toast.LENGTH_SHORT).show();
        }

        resetForm();
    }

    private void resetForm() {
        editingReview = null;
        rating = 0;
        updateStars();
        edtReview.setText("");
        edtReview.clearFocus();
        btnSubmit.setText("➤ Gửi đánh giá");
    }

    private void updateStars() {
        for (int i = 0; i < stars.length; i++) {
            stars[i].setTextColor(i < rating ? Color.parseColor("#F5A623") : Color.parseColor("#C8D6C8"));
        }
    }

    private void updateStatistics(List<DanhGia> list) {
        TextView tvAvg = findViewById(R.id.reviewAverage);
        TextView tvAvgStars = findViewById(R.id.reviewAverageStars);
        TextView tvTotal = findViewById(R.id.reviewTotal);
        TextView tvCommunityTotal = findViewById(R.id.reviewCommunityTotal);

        if (list == null || list.isEmpty()) {
            tvAvg.setText("0.0");
            tvAvgStars.setText("☆☆☆☆☆");
            tvTotal.setText("0 nhận xét");
            tvCommunityTotal.setText("0 bài viết");
            setStarBar(R.id.reviewFiveProgress, R.id.reviewFiveShare, 0);
            setStarBar(R.id.reviewFourProgress, R.id.reviewFourShare, 0);
            setStarBar(R.id.reviewThreeProgress, R.id.reviewThreeShare, 0);
            setStarBar(R.id.reviewTwoProgress, R.id.reviewTwoShare, 0);
            setStarBar(R.id.reviewOneProgress, R.id.reviewOneShare, 0);
            return;
        }

        int total = list.size();
        double sum = 0;
        int[] counts = new int[6]; // index 1..5

        for (DanhGia dg : list) {
            sum += dg.getSoSao();
            int s = (int) Math.round(dg.getSoSao());
            if (s >= 1 && s <= 5) {
                counts[s]++;
            }
        }

        double avg = sum / total;
        tvAvg.setText(String.format(Locale.US, "%.1f", avg));
        tvAvgStars.setText(DanhGiaAdapter.buildStarString((int) Math.round(avg)));
        tvTotal.setText(total + " nhận xét");
        tvCommunityTotal.setText(total + " bài viết");

        setStarBar(R.id.reviewFiveProgress, R.id.reviewFiveShare, (counts[5] * 100) / total);
        setStarBar(R.id.reviewFourProgress, R.id.reviewFourShare, (counts[4] * 100) / total);
        setStarBar(R.id.reviewThreeProgress, R.id.reviewThreeShare, (counts[3] * 100) / total);
        setStarBar(R.id.reviewTwoProgress, R.id.reviewTwoShare, (counts[2] * 100) / total);
        setStarBar(R.id.reviewOneProgress, R.id.reviewOneShare, (counts[1] * 100) / total);
    }

    private void setStarBar(int progressId, int labelId, int percent) {
        ProgressBar pb = findViewById(progressId);
        TextView tv = findViewById(labelId);
        if (pb != null) pb.setProgress(percent);
        if (tv != null) tv.setText(percent + "%");
    }
}