package android.ivo.newsapp;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import java.util.List;

public class BookmarksActivity extends AppCompatActivity implements NewsElementBookmarksAdapter.ViewHolder.OnViewClickListener {
    private RecyclerView mRecyclerView;
    private NewsElementBookmarksAdapter mNewsElementHeadlinesAdapter;
    private NewsDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        mRecyclerView = findViewById(R.id.layout_rv_bookmarks);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        mDatabase= NewsDatabase.getInstance(this);
        mNewsElementHeadlinesAdapter = new NewsElementBookmarksAdapter(this);
        updateRecyclerView();

        mRecyclerView.setAdapter(mNewsElementHeadlinesAdapter);
    }

    @Override
    public void OnDeleteClicked(NewsElementBookmarksAdapter.ViewHolder holder) {
        News currentNews = mNewsElementHeadlinesAdapter.getNews(holder.getAdapterPosition());
        Log.d("TAG", "OnDeleteClicked: " + currentNews.getTitle());
        mDatabase.newsDao().deleteNews(currentNews);
        updateRecyclerView();
    }

    private void updateRecyclerView()
    {
        List<News> newsList = mDatabase.newsDao().getNewsList();
        mNewsElementHeadlinesAdapter.addAll(newsList);
    }
}