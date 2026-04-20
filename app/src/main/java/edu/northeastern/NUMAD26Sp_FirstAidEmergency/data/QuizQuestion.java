package edu.northeastern.NUMAD26Sp_FirstAidEmergency.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz_questions")
public class QuizQuestion {
    @PrimaryKey(autoGenerate = true) public long id;
    @NonNull public String question;
    @NonNull public String optionA;
    @NonNull public String optionB;
    @NonNull public String optionC;
    @NonNull public String optionD;
    public int correctIndex; // 0=A, 1=B, 2=C, 3=D
    @NonNull public String explanation;

    public QuizQuestion(@NonNull String question, @NonNull String optionA, @NonNull String optionB,
                        @NonNull String optionC, @NonNull String optionD, int correctIndex,
                        @NonNull String explanation) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctIndex = correctIndex;
        this.explanation = explanation;
    }
}