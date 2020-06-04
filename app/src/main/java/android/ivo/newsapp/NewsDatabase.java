package android.ivo.newsapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.time.OffsetDateTime;

@Database(exportSchema = false, entities = News.class, version = 1)
public abstract class NewsDatabase extends RoomDatabase {
    private static final String DB_NAME = "news_db";
    private static NewsDatabase instance;

    public static synchronized NewsDatabase getInstance(Context context)
    {
        if(instance==null) {
            instance = Room.databaseBuilder(context,NewsDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract NewsDao newsDao();
}


