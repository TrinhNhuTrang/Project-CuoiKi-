package com.example.projectcuoikiltdd;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectcuoikiltdd.data.entity.DanhGia;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DanhGiaAdapter extends RecyclerView.Adapter<DanhGiaAdapter.ViewHolder> {

    public interface OnReviewActionListener {
        void onEdit(DanhGia danhGia);
        void onDelete(DanhGia danhGia);
    }

    private List<DanhGia> list = new ArrayList<>();
    private final int currentUserId;
    private final OnReviewActionListener listener;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public DanhGiaAdapter(int currentUserId, OnReviewActionListener listener) {
        this.currentUserId = currentUserId;
        this.listener = listener;
    }

    public void setData(List<DanhGia> newList) {
        this.list = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        float d = context.getResources().getDisplayMetrics().density;

        LinearLayout card = new LinearLayout(context);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.bg_card);
        int pad = Math.round(14 * d);
        card.setPadding(pad, pad, pad, pad);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = Math.round(10 * d);
        card.setLayoutParams(params);

        // Hàng trên: Tên người dùng + Số sao
        LinearLayout topRow = new LinearLayout(context);
        topRow.setOrientation(LinearLayout.HORIZONTAL);
        topRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView tvUser = new TextView(context);
        tvUser.setTextColor(ContextCompat.getColor(context, R.color.text_dark));
        tvUser.setTextSize(13);
        tvUser.setTypeface(Typeface.DEFAULT_BOLD);
        topRow.addView(tvUser, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        TextView tvStars = new TextView(context);
        tvStars.setTextColor(ContextCompat.getColor(context, R.color.star));
        tvStars.setTextSize(13);
        topRow.addView(tvStars);
        card.addView(topRow);

        // Ngày đánh giá
        TextView tvDate = new TextView(context);
        tvDate.setTextColor(ContextCompat.getColor(context, R.color.text_gray));
        tvDate.setTextSize(11);
        card.addView(tvDate);

        // Nội dung đánh giá
        TextView tvContent = new TextView(context);
        tvContent.setTextColor(ContextCompat.getColor(context, R.color.text_dark));
        tvContent.setTextSize(13);
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentParams.topMargin = Math.round(6 * d);
        card.addView(tvContent, contentParams);

        // Nút Sửa / Xóa (chỉ hiện với đánh giá của chính người dùng)
        LinearLayout actionRow = new LinearLayout(context);
        actionRow.setOrientation(LinearLayout.HORIZONTAL);
        actionRow.setGravity(Gravity.END);
        LinearLayout.LayoutParams actionParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        actionParams.topMargin = Math.round(8 * d);

        TextView btnEdit = new TextView(context);
        btnEdit.setText("Sửa");
        btnEdit.setTextColor(ContextCompat.getColor(context, R.color.green_primary));
        btnEdit.setTextSize(12);
        btnEdit.setTypeface(Typeface.DEFAULT_BOLD);
        btnEdit.setPadding(Math.round(10 * d), Math.round(4 * d), Math.round(10 * d), Math.round(4 * d));
        actionRow.addView(btnEdit);

        TextView btnDelete = new TextView(context);
        btnDelete.setText("Xóa");
        btnDelete.setTextColor(0xFFD32F2F);
        btnDelete.setTextSize(12);
        btnDelete.setTypeface(Typeface.DEFAULT_BOLD);
        btnDelete.setPadding(Math.round(10 * d), Math.round(4 * d), 0, Math.round(4 * d));
        actionRow.addView(btnDelete);

        card.addView(actionRow, actionParams);

        return new ViewHolder(card, tvUser, tvStars, tvDate, tvContent, actionRow, btnEdit, btnDelete);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DanhGia item = list.get(position);
        boolean isMine = (item.getIdNguoiDung() == currentUserId);

        holder.tvUser.setText(isMine ? "Bạn (Thành viên Việt Hành)" : "Du khách #" + item.getIdNguoiDung());
        holder.tvStars.setText(buildStarString((int) Math.round(item.getSoSao())));
        holder.tvDate.setText(sdf.format(new Date(item.getNgayDanhGia())));
        holder.tvContent.setText(item.getNoiDung());

        holder.actionRow.setVisibility(isMine ? View.VISIBLE : View.GONE);
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(item);
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(item);
        });
    }

    public static String buildStarString(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(i < count ? "★" : "☆");
        }
        return sb.toString();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUser, tvStars, tvDate, tvContent, btnEdit, btnDelete;
        LinearLayout actionRow;

        ViewHolder(@NonNull View itemView, TextView tvUser, TextView tvStars, TextView tvDate,
                   TextView tvContent, LinearLayout actionRow, TextView btnEdit, TextView btnDelete) {
            super(itemView);
            this.tvUser = tvUser;
            this.tvStars = tvStars;
            this.tvDate = tvDate;
            this.tvContent = tvContent;
            this.actionRow = actionRow;
            this.btnEdit = btnEdit;
            this.btnDelete = btnDelete;
        }
    }
}
