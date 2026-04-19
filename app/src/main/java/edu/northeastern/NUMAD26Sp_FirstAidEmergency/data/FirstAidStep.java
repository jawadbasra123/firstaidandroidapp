package edu.northeastern.NUMAD26Sp_FirstAidEmergency.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "steps")
public class FirstAidStep {
    @PrimaryKey(autoGenerate = true) public long id;
    @NonNull public String topicId;
    public int orderIndex;
    @NonNull public String instruction;
    public String caution;

    public FirstAidStep(@NonNull String topicId, int orderIndex,
                        @NonNull String instruction, String caution) {
        this.topicId = topicId;
        this.orderIndex = orderIndex;
        this.instruction = instruction;
        this.caution = caution;
    }
}