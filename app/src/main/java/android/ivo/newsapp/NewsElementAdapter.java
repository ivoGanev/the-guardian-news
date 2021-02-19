package android.ivo.newsapp;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

abstract class NewsElementAdapter<T extends RecyclerView.ViewHolder> extends  RecyclerView.Adapter<T> {
    private List<News> mNews;

    void addAll(List<News> news) {
        this.mNews = news;
        notifyDataSetChanged();
    }

    public News getNews(int position)
    {
        return mNews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mNews.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        if (mNews != null)
            return mNews.size();
        return 0;
    }

    @NonNull
    @Override
    public abstract T onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(@NonNull T holder, int position);
}
