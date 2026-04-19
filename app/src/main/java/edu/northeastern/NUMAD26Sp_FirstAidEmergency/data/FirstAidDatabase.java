package edu.northeastern.NUMAD26Sp_FirstAidEmergency.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FirstAidTopic.class, FirstAidStep.class}, version = 1, exportSchema = false)
public abstract class FirstAidDatabase extends RoomDatabase {

    public abstract FirstAidDao dao();

    private static volatile FirstAidDatabase INSTANCE;
    public static final ExecutorService writeExecutor = Executors.newFixedThreadPool(4);

    public static FirstAidDatabase getInstance(final Context ctx) {
        if (INSTANCE == null) {
            synchronized (FirstAidDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    ctx.getApplicationContext(),
                                    FirstAidDatabase.class,
                                    "first_aid.db")
                            .addCallback(seedCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback seedCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            writeExecutor.execute(() -> Seeder.seed(INSTANCE.dao()));
        }
    };
}