package com.kpmbustimingapp.app;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kpmbustimingapp.app.R;

import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {

    private List<BusInfo> busList;

    public BusAdapter(List<BusInfo> busList) {
        this.busList = busList;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bus, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        BusInfo bus = busList.get(position);
        holder.timeText.setText(bus.getTime());
        holder.typeText.setText(bus.getType());

        // All buses show with full color - no gray effect
        holder.itemCard.setAlpha(1.0f);
        holder.timeText.setTextColor(Color.parseColor("#333333"));
        holder.typeText.setTextColor(Color.parseColor("#666666"));
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    static class BusViewHolder extends RecyclerView.ViewHolder {
        CardView itemCard;
        TextView timeText;
        TextView typeText;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCard = itemView.findViewById(R.id.busItemCard);
            timeText = itemView.findViewById(R.id.busTimeText);
            typeText = itemView.findViewById(R.id.busTypeText);
        }
    }
}
