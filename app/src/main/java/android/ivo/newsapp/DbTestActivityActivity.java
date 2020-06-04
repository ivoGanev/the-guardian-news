package android.ivo.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.ivo.newsapp.databinding.DbTestActivityBinding;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.Random;

public class DbTestActivityActivity extends AppCompatActivity {
    private static final String TAG = DbTestActivityActivity.class.getSimpleName();

    DbTestActivityBinding binding;
    private NewsDao newsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DbTestActivityBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        NewsDatabase database = NewsDatabase.getInstance(this);
        newsDao = database.newsDao();

        final Random random = new Random();
        final String[] titles = new String[]
                {
                        "New heaven found for small time investors",
                        "Three thousand soldiers help to build a desert city",
                        "On a mission to our neighbor galaxy",
                        "AI can now thinks as a human"
                };

        // Get
        binding.btnDbGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printNewsDb();
            }
        });

        // Insert
        binding.btnDbInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titles[random.nextInt(titles.length)];

                News news = new News.Builder()
                        .title(title)
                        .build();
                newsDao.insertNews(news);
                Log.d(TAG, "onClick: Inserted\n" + title);
            }
        });

        // Update
        binding.btnDbUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedNewsTitle = "\"Hello update \"";
                Log.d(TAG, "Updating database 0 with text: " + updatedNewsTitle);
                List<News> news = newsDao.getNewsList();

                // testing only the first item
                News oldNews = news.get(0);
                oldNews.setTitle(updatedNewsTitle);
                newsDao.updateNews(oldNews);
            }
        });

        // Delete
        binding.btnDbDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = 1;
                News news = newsDao.getNews(id);
                if(news!=null) {
                    newsDao.deleteNews(news);
                    Log.d(TAG, "Deleted item " + id);
                }
                else {
                    Log.d(TAG, "No items to delete");
                }
            }
        });
    }

    private void printNewsDb()
    {
        List<News> news = newsDao.getNewsList();
        if(news.size() == 0) {
            Log.d(TAG, "There are no news.");
            return;
        }
        Log.d(TAG, "----------------------------------------------");
        for (News n : news) {
            Log.d(TAG, "News ID: " + n.getId() + "  : Title: " + n.getTitle());
        }
    }
}