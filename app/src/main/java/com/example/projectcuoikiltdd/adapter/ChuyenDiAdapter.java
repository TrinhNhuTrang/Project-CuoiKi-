package com.example.projectcuoikiltdd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectcuoikiltdd.R;
import com.example.projectcuoikiltdd.data.entity.ChuyenDi;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChuyenDiAdapter extends RecyclerView.Adapter<ChuyenDiAdapter.TripViewHolder> {

    public interface OnTripClickListener {
        void onTripClick(ChuyenDi chuyenDi);
    }

    public interface OnTripDeleteListener {
        void onTripDelete(ChuyenDi chuyenDi);
    }

    private List<ChuyenDi> tripList = new ArrayList<>();
    private final OnTripClickListener clickListener;
    private final OnTripDeleteListener deleteListener;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final DecimalFormat currencyFormat = new DecimalFormat("#,###");

    public ChuyenDiAdapter(OnTripClickListener clickListener, OnTripDeleteListener deleteListener) {
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    public void setTripList(List<ChuyenDi> tripList) {
        this.tripList = tripList != null ? tripList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chuyen_di, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        ChuyenDi chuyenDi = tripList.get(position);
        holder.bind(chuyenDi);
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    class TripViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTripName;
        private final TextView tvTripDestination;
        private final TextView tvTripDates;
        private final TextView tvTripBudget;
        private final TextView tvTripNote;
        private final View boxTripNote;
        private final View btnEditTrip;
        private final View btnDeleteTrip;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTripName = itemView.findViewById(R.id.tv_trip_name);
            tvTripDestination = itemView.findViewById(R.id.tv_trip_destination);
            tvTripDates = itemView.findViewById(R.id.tv_trip_dates);
            tvTripBudget = itemView.findViewById(R.id.tv_trip_budget);
            tvTripNote = itemView.findViewById(R.id.tv_trip_note);
            boxTripNote = itemView.findViewById(R.id.box_trip_note);
            btnEditTrip = itemView.findViewById(R.id.btn_edit_trip);
            btnDeleteTrip = itemView.findViewById(R.id.btn_delete_trip);
        }

        public void bind(ChuyenDi trip) {
            tvTripName.setText(trip.getTenChuyenDi() != null ? trip.getTenChuyenDi() : "Chuyến đi chưa đặt tên");
            tvTripDestination.setText(trip.getDiaChi() != null ? trip.getDiaChi() : "Chưa chọn điểm đến");

            String startDateStr = trip.getNgayBatDau() > 0 ? dateFormat.format(new Date(trip.getNgayBatDau())) : "--/--/----";
            String endDateStr = trip.getNgayKetThuc() > 0 ? dateFormat.format(new Date(trip.getNgayKetThuc())) : "--/--/----";
            tvTripDates.setText(startDateStr + " - " + endDateStr);

            if (trip.getChiPhiDuKien() > 0) {
                tvTripBudget.setText(currencyFormat.format(trip.getChiPhiDuKien()) + " VNĐ");
            } else {
                tvTripBudget.setText("Miễn phí / Chưa tính");
            }

            if (trip.getGhiChu() != null && !trip.getGhiChu().trim().isEmpty()) {
                boxTripNote.setVisibility(View.VISIBLE);
                tvTripNote.setText(trip.getGhiChu());
            } else {
                boxTripNote.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onTripClick(trip);
                }
            });

            btnEditTrip.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onTripClick(trip);
                }
            });

            btnDeleteTrip.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onTripDelete(trip);
                }
            });
        }
    }
}
