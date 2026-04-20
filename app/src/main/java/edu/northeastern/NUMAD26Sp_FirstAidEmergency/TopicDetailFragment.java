package edu.northeastern.NUMAD26Sp_FirstAidEmergency;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.NUMAD26Sp_FirstAidEmergency.data.FirstAidDatabase;

public class TopicDetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topic_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String topicId = args != null ? args.getString("topicId") : null;
        String topicTitle = args != null ? args.getString("topicTitle") : "Details";

        TextView title = view.findViewById(R.id.title);
        TextView subtitle = view.findViewById(R.id.subtitle);
        title.setText(topicTitle);

        StepAdapter adapter = new StepAdapter();
        RecyclerView rv = view.findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        if (topicId != null) {
            FirstAidDatabase db = FirstAidDatabase.getInstance(requireContext());
            db.dao().getTopicById(topicId).observe(getViewLifecycleOwner(), t -> {
                if (t != null) subtitle.setText(t.shortDescription);
            });
            db.dao().getStepsForTopic(topicId).observe(getViewLifecycleOwner(), adapter::submitList);
        }
    }
}