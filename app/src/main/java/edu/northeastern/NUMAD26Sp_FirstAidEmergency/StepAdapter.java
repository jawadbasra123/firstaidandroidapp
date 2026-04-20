package edu.northeastern.NUMAD26Sp_FirstAidEmergency;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.NUMAD26Sp_FirstAidEmergency.data.FirstAidStep;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.VH> {

    private List<FirstAidStep> items = new ArrayList<>();

    public void submitList(List<FirstAidStep> list) {
        items = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        FirstAidStep s = items.get(pos);
        h.stepNumber.setText(String.valueOf(s.orderIndex));
        h.instruction.setText(s.instruction);
        if (TextUtils.isEmpty(s.caution)) {
            h.cautionRow.setVisibility(View.GONE);
        } else {
            h.cautionRow.setVisibility(View.VISIBLE);
            h.cautionText.setText(s.caution);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView stepNumber, instruction, cautionText;
        LinearLayout cautionRow;

        VH(View v) {
            super(v);
            stepNumber = v.findViewById(R.id.step_number);
            instruction = v.findViewById(R.id.instruction);
            cautionRow = v.findViewById(R.id.caution_row);
            cautionText = v.findViewById(R.id.caution_text);
        }
    }
}