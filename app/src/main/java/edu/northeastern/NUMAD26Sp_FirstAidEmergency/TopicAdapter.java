package edu.northeastern.NUMAD26Sp_FirstAidEmergency;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.NUMAD26Sp_FirstAidEmergency.data.FirstAidTopic;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.VH> {

    public interface OnClick {
        void onClick(FirstAidTopic topic);
    }

    private List<FirstAidTopic> items = new ArrayList<>();
    private final OnClick listener;

    public TopicAdapter(OnClick listener) {
        this.listener = listener;
    }

    public void submitList(List<FirstAidTopic> list) {
        items = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topic, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        FirstAidTopic t = items.get(pos);
        h.title.setText(t.title);
        h.subtitle.setText(t.shortDescription);

        int color;
        switch (t.severity) {
            case 0: color = R.color.severity_critical; break;
            case 1: color = R.color.severity_serious; break;
            default: color = R.color.severity_common;
        }
        h.severityBar.setBackgroundResource(color);

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(t);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        View severityBar;

        VH(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            subtitle = v.findViewById(R.id.subtitle);
            severityBar = v.findViewById(R.id.severity_bar);
        }
    }
}