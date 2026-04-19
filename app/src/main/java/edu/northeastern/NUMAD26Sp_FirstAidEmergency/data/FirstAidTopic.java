package edu.northeastern.NUMAD26Sp_FirstAidEmergency.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "topics")
public class FirstAidTopic {
    @PrimaryKey @NonNull public String id;
    @NonNull public String title;
    @NonNull public String shortDescription;
    public int severity;  // 0=critical, 1=serious, 2=common
    @NonNull public String keywords;

    public FirstAidTopic(@NonNull String id, @NonNull String title,
                         @NonNull String shortDescription, int severity, @NonNull String keywords) {
        this.id = id;
        this.title = title;
        this.shortDescription = shortDescription;
        this.severity = severity;
        this.keywords = keywords;
    }
}