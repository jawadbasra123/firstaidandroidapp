package edu.northeastern.NUMAD26Sp_FirstAidEmergency;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import edu.northeastern.NUMAD26Sp_FirstAidEmergency.data.FirstAidDatabase;
import edu.northeastern.NUMAD26Sp_FirstAidEmergency.data.QuizQuestion;

public class QuizFragment extends Fragment {

    private List<QuizQuestion> questions;
    private int currentIndex = 0;
    private int score = 0;
    private boolean answered = false;

    // Default button color used when resetting option buttons between questions.
    private final int brandRed = Color.parseColor("#C62828");

    private TextView progress, questionText, explanation, scoreText;
    private MaterialButton optionA, optionB, optionC, optionD, nextButton, restartButton;
    private LinearLayout questionGroup, resultGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progress = view.findViewById(R.id.progress);
        questionText = view.findViewById(R.id.question_text);
        optionA = view.findViewById(R.id.option_a);
        optionB = view.findViewById(R.id.option_b);
        optionC = view.findViewById(R.id.option_c);
        optionD = view.findViewById(R.id.option_d);
        explanation = view.findViewById(R.id.explanation);
        nextButton = view.findViewById(R.id.next_button);
        questionGroup = view.findViewById(R.id.question_group);
        resultGroup = view.findViewById(R.id.result_group);
        scoreText = view.findViewById(R.id.score_text);
        restartButton = view.findViewById(R.id.restart_button);

        optionA.setOnClickListener(v -> answer(0, optionA));
        optionB.setOnClickListener(v -> answer(1, optionB));
        optionC.setOnClickListener(v -> answer(2, optionC));
        optionD.setOnClickListener(v -> answer(3, optionD));

        nextButton.setOnClickListener(v -> {
            currentIndex++;
            answered = false;
            render();
        });

        restartButton.setOnClickListener(v -> {
            currentIndex = 0;
            score = 0;
            answered = false;
            render();
        });

        FirstAidDatabase.getInstance(requireContext())
                .dao()
                .getRandomQuestions(8)
                .observe(getViewLifecycleOwner(), list -> {
                    questions = list;
                    render();
                });
    }

    private void render() {
        if (questions == null || questions.isEmpty()) return;

        if (currentIndex >= questions.size()) {
            questionGroup.setVisibility(View.GONE);
            resultGroup.setVisibility(View.VISIBLE);
            scoreText.setText(score + " / " + questions.size());
            return;
        }

        questionGroup.setVisibility(View.VISIBLE);
        resultGroup.setVisibility(View.GONE);

        QuizQuestion q = questions.get(currentIndex);
        progress.setText("Question " + (currentIndex + 1) + " of " + questions.size());
        questionText.setText(q.question);
        optionA.setText(q.optionA);
        optionB.setText(q.optionB);
        optionC.setText(q.optionC);
        optionD.setText(q.optionD);

        resetButtons();
        explanation.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
    }

    private void answer(int choice, MaterialButton btn) {
        if (answered || questions == null) return;
        answered = true;
        QuizQuestion q = questions.get(currentIndex);
        if (choice == q.correctIndex) {
            btn.setBackgroundColor(Color.parseColor("#2E7D32")); // green
            score++;
        } else {
            btn.setBackgroundColor(Color.parseColor("#C62828")); // red
            highlightCorrect(q.correctIndex);
        }
        explanation.setVisibility(View.VISIBLE);
        explanation.setText(q.explanation);
        nextButton.setVisibility(View.VISIBLE);
    }

    private void highlightCorrect(int correctIndex) {
        MaterialButton correct;
        switch (correctIndex) {
            case 0: correct = optionA; break;
            case 1: correct = optionB; break;
            case 2: correct = optionC; break;
            default: correct = optionD;
        }
        correct.setBackgroundColor(Color.parseColor("#2E7D32"));
    }

    private void resetButtons() {
        optionA.setBackgroundColor(brandRed);
        optionB.setBackgroundColor(brandRed);
        optionC.setBackgroundColor(brandRed);
        optionD.setBackgroundColor(brandRed);
    }
}