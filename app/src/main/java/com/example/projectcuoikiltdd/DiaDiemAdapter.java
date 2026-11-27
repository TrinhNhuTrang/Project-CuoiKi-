package com.example.projectcuoikiltdd;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectcuoikiltdd.data.entity.DiaDiem;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiaDiemAdapter extends RecyclerView.Adapter<DiaDiemAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(DiaDiem diaDiem);
    }

    private List<DiaDiem> list = new ArrayList<>();
    private final OnItemClickListener listener;
    private final boolean isHorizontal;

    public DiaDiemAdapter(boolean isHorizontal, OnItemClickListener listener) {
        this.isHorizontal = isHorizontal;
        this.listener = listener;
    }

    public void setData(List<DiaDiem> newList) {
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
        card.setClipToOutline(true);

        RecyclerView.LayoutParams params = isHorizontal
                ? new RecyclerView.LayoutParams(Math.round(240 * d), ViewGroup.LayoutParams.WRAP_CONTENT)
                : new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (isHorizontal) {
            params.setMarginEnd(Math.round(14 * d));
        } else {
            params.bottomMargin = Math.round(14 * d);
        }
        card.setLayoutParams(params);

        ImageView img = new ImageView(context);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setBackgroundColor(ContextCompat.getColor(context, R.color.green_soft));
        card.addView(img, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, Math.round((isHorizontal ? 135 : 185) * d)));

        LinearLayout info = new LinearLayout(context);
        info.setOrientation(LinearLayout.VERTICAL);
        int pad = Math.round(12 * d);
        info.setPadding(pad, pad, pad, pad);

        TextView tvName = new TextView(context);
        tvName.setTextColor(ContextCompat.getColor(context, R.color.text_dark));
        tvName.setTextSize(16);
        tvName.setTypeface(Typeface.DEFAULT_BOLD);
        tvName.setMaxLines(1);
        tvName.setEllipsize(TextUtils.TruncateAt.END);
        info.addView(tvName);

        TextView tvAddress = new TextView(context);
        tvAddress.setTextColor(ContextCompat.getColor(context, R.color.text_gray));
        tvAddress.setTextSize(12);
        tvAddress.setMaxLines(1);
        tvAddress.setEllipsize(TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams addrParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addrParams.topMargin = Math.round(4 * d);
        info.addView(tvAddress, addrParams);

        LinearLayout bottomRow = new LinearLayout(context);
        bottomRow.setOrientation(LinearLayout.HORIZONTAL);
        bottomRow.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rowParams.topMargin = Math.round(8 * d);

        TextView tvPrice = new TextView(context);
        tvPrice.setTextColor(ContextCompat.getColor(context, R.color.green_primary));
        tvPrice.setTextSize(13);
        tvPrice.setTypeface(Typeface.DEFAULT_BOLD);
        bottomRow.addView(tvPrice, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        TextView tvDetail = new TextView(context);
        tvDetail.setText("Chi tiết →");
        tvDetail.setTextColor(ContextCompat.getColor(context, R.color.home_action));
        tvDetail.setTextSize(12);
        tvDetail.setTypeface(Typeface.DEFAULT_BOLD);
        bottomRow.addView(tvDetail);

        info.addView(bottomRow, rowParams);
        card.addView(info);

        return new ViewHolder(card, img, tvName, tvAddress, tvPrice);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiaDiem item = list.get(position);
        Context context = holder.itemView.getContext();

        holder.tvName.setText(item.getTenDiaDiem());
        holder.tvAddress.setText("📍 " + item.getDiaChi());

        if (item.getGiaVe() <= 0) {
            holder.tvPrice.setText("Miễn phí vé");
        } else {
            NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
            holder.tvPrice.setText(nf.format(item.getGiaVe()) + "đ");
        }

        int resId = resolveImageRes(context, item.getHinhAnh());
        holder.img.setImageResource(resId);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
    }

    public static int resolveImageRes(Context context, String imageName) {
        if (!TextUtils.isEmpty(imageName)) {
            int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            if (resId != 0) return resId;
        }
        return R.drawable.img_trang_an;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvName, tvAddress, tvPrice;

        ViewHolder(@NonNull View itemView, ImageView img, TextView tvName, TextView tvAddress, TextView tvPrice) {
            super(itemView);
            this.img = img;
            this.tvName = tvName;
            this.tvAddress = tvAddress;
            this.tvPrice = tvPrice;
        }
    }
}
