package android.ivo.newsapp;

import android.ivo.newsapp.databinding.NewsFeedElementBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {
    private List<News> mNews;

    NewsRecyclerViewAdapter(List<News> news) {
        mNews = news;
    }

    void addAll(List<News> news) {
        mNews = news;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_feed_element, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, final int position) {
        final News news = mNews.get(position);
        NewsFeedElementBinding binding = holder.binding;

        binding.newsFeedTitle.setText(news.getTitle());
        binding.newsFeedDate.setText(news.getPublicationDate());
        binding.newsFeedSection.setText(news.getSectionName());

        String byline = news.getByline();
        if (byline != null)
            binding.newsFeedByline.setText(news.getByline());
        else
            binding.newsFeedByline.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        if (mNews != null)
            return mNews.size();
        return 0;
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        final NewsFeedElementBinding binding;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NewsFeedElementBinding.bind(itemView);
        }
    }
}
