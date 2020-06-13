package android.ivo.newsapp;

import android.ivo.newsapp.databinding.NewsFeedElementBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsRecyclerViewAdapter
        extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {

    private static final String TAG = NewsRecyclerViewAdapter.class.getSimpleName();
    private List<News> mNews;
    private NewsViewHolder.OnViewClickedListener mOnViewClickedListener;

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

    public void setOnViewClickedListener(NewsViewHolder.OnViewClickedListener onViewClickedListener) {
        mOnViewClickedListener = onViewClickedListener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_feed_element, parent, false);
        final NewsViewHolder holder = new NewsViewHolder(view, mOnViewClickedListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, final int position) {
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

    static class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final NewsFeedElementBinding binding;
        private OnViewClickedListener onViewClickListener;

        NewsViewHolder(@NonNull View itemView, OnViewClickedListener listener) {
            super(itemView);
            binding = NewsFeedElementBinding.bind(itemView);
            onViewClickListener = listener;
            itemView.setOnClickListener(this);
            binding.newsFeedBtnHtml.setOnClickListener(this);
            binding.newsFeedBtnBookmark.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            if (onViewClickListener != null) {
                if (viewId == R.id.news_feed_btn_html) {
                    onViewClickListener.onHttpButtonClicked(this);
                } else if (viewId == R.id.news_feed_layout) {
                    onViewClickListener.onElementClicked(this);
                } else if (viewId == R.id.news_feed_btn_bookmark) {
                    onViewClickListener.onBookmarksButtonClicked(this);
                }
            }
        }

        interface OnViewClickedListener {
            void onHttpButtonClicked(NewsViewHolder holder);

            void onBookmarksButtonClicked(NewsViewHolder holder);

            void onElementClicked(NewsViewHolder holder);
        }
    }
}
