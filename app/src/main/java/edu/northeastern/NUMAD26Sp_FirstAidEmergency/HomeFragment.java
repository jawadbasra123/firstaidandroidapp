package edu.northeastern.NUMAD26Sp_FirstAidEmergency;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.NUMAD26Sp_FirstAidEmergency.data.FirstAidDatabase;

public class HomeFragment extends Fragment {

    private TopicAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.recycler);
        rv.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        adapter = new TopicAdapter(topic -> {
            Toast.makeText(requireContext(), topic.title + " tapped", Toast.LENGTH_SHORT).show();
            // In Milestone 6 this will navigate to the detail screen.
        });
        rv.setAdapter(adapter);

        FirstAidDatabase.getInstance(requireContext())
                .dao()
                .getAllTopics()
                .observe(getViewLifecycleOwner(), topics -> adapter.submitList(topics));
    }
}