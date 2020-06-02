package android.ivo.newsapp;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NewsDao{
    @Query("SELECT * FROM news")
    List<News> getNewsList();

    @Insert
    void insertNews(News news);

    @Update
    void updateNews(News news);

    @Delete
    void deleteNews(News news);
}
