package edu.northeastern.NUMAD26Sp_FirstAidEmergency.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FirstAidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTopics(List<FirstAidTopic> topics);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSteps(List<FirstAidStep> steps);

    @Query("SELECT * FROM topics ORDER BY severity ASC, title ASC")
    LiveData<List<FirstAidTopic>> getAllTopics();

    @Query("SELECT * FROM topics WHERE id = :id LIMIT 1")
    LiveData<FirstAidTopic> getTopicById(String id);

    @Query("SELECT * FROM steps WHERE topicId = :topicId ORDER BY orderIndex ASC")
    LiveData<List<FirstAidStep>> getStepsForTopic(String topicId);

    @Query("SELECT * FROM topics WHERE title LIKE :q OR shortDescription LIKE :q OR keywords LIKE :q")
    LiveData<List<FirstAidTopic>> search(String q);

    @Query("SELECT COUNT(*) FROM topics")
    int countTopics();
}