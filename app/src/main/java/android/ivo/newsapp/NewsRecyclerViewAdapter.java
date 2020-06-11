package android.ivo.newsapp;

import android.ivo.newsapp.databinding.NewsFeedElementBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> implements View.OnClickListener {
    private static final String TAG = NewsRecyclerViewAdapter.class.getSimpleName();
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
        NewsViewHolder holder = new NewsViewHolder(view);
        view.setOnClickListener(this);
        view.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty())
            onBindViewHolder(holder, position);
        else {
            if (payloads.contains("visibility")) {
                View extras = holder.binding.newsExtras;
                if (extras.getVisibility() == View.GONE)
                    extras.setVisibility(View.VISIBLE);
                else
                    extras.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
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
    public long getItemId(int position) {
        return mNews.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        if (mNews != null)
            return mNews.size();
        return 0;
    }

    @Override
    public void onClick(View v) {
        NewsViewHolder holder = (NewsViewHolder) v.getTag();
        notifyItemChanged(holder.getAdapterPosition(), "visibility");
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        final NewsFeedElementBinding binding;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NewsFeedElementBinding.bind(itemView);
        }
    }
}
