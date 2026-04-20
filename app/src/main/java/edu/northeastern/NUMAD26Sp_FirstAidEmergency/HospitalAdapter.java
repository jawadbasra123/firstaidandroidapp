package edu.northeastern.NUMAD26Sp_FirstAidEmergency;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {

    public interface OnNavigateListener {
        void onNavigate(Hospital hospital);
    }

    private List<Hospital> hospitals = new ArrayList<>();
    private final OnNavigateListener listener;

    public HospitalAdapter(OnNavigateListener listener) {
        this.listener = listener;
    }

    public void setHospitals(List<Hospital> hospitals) {
        this.hospitals = hospitals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hospital, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hospital h = hospitals.get(position);

        holder.name.setText(h.getName());
        holder.address.setText(h.getAddress() != null ? h.getAddress() : "");

        if (h.getRating() > 0) {
            holder.rating.setText(String.format("★ %.1f", h.getRating()));
        } else {
            holder.rating.setText("No rating");
        }

        if (h.isOpenNow()) {
            holder.status.setText("Open");
            holder.status.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            holder.status.setText("");
        }

        holder.distance.setText(String.format("%.1f km", h.getDistanceKm()));
        holder.navigateBtn.setOnClickListener(v -> listener.onNavigate(h));
    }

    @Override
    public int getItemCount() {
        return hospitals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name, address, rating, status, distance;
        final Button navigateBtn;

        ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.hospital_name);
            address = v.findViewById(R.id.hospital_address);
            rating = v.findViewById(R.id.hospital_rating);
            status = v.findViewById(R.id.hospital_status);
            distance = v.findViewById(R.id.hospital_distance);
            navigateBtn = v.findViewById(R.id.navigate_btn);
        }
    }
}
