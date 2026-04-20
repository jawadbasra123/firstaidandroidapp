package edu.northeastern.NUMAD26Sp_FirstAidEmergency;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import edu.northeastern.NUMAD26Sp_FirstAidEmergency.data.FirstAidDao;
import edu.northeastern.NUMAD26Sp_FirstAidEmergency.data.FirstAidDatabase;
import edu.northeastern.NUMAD26Sp_FirstAidEmergency.data.FirstAidTopic;

public class HomeFragment extends Fragment {

    private TopicAdapter adapter;
    private FirstAidDao dao;
    private LiveData<List<FirstAidTopic>> currentSource;
    private Observer<List<FirstAidTopic>> observer;

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

        // Header setup
        TextView headerTitle = view.findViewById(R.id.header_title);
        View header911 = view.findViewById(R.id.header_911_button);
        headerTitle.setText("First Aid Emergency");
        header911.setOnClickListener(v -> call911());

        RecyclerView rv = view.findViewById(R.id.recycler);
        rv.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        adapter = new TopicAdapter(topic -> {
            Bundle args = new Bundle();
            args.putString("topicId", topic.id);
            args.putString("topicTitle", topic.title);
            Navigation.findNavController(view)
                    .navigate(R.id.action_home_to_detail, args);
        });
        rv.setAdapter(adapter);

        dao = FirstAidDatabase.getInstance(requireContext()).dao();
        observer = topics -> adapter.submitList(topics);

        swapSource(dao.getAllTopics());

        TextInputEditText searchInput = view.findViewById(R.id.search_input);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {
                String q = s.toString().trim();
                if (TextUtils.isEmpty(q)) swapSource(dao.getAllTopics());
                else swapSource(dao.search("%" + q + "%"));
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void swapSource(LiveData<List<FirstAidTopic>> newSource) {
        if (currentSource != null) currentSource.removeObserver(observer);
        currentSource = newSource;
        currentSource.observe(getViewLifecycleOwner(), observer);
    }

    private void call911() {
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:911"));
        startActivity(i);
    }
}