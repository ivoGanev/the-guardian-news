package android.ivo.newsapp;

import android.ivo.newsapp.databinding.NewsFeedElementBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class NewsElementHeadlinesAdapter extends NewsElementAdapter<NewsElementHeadlinesAdapter.ViewHolder> {
    private ViewHolder.OnViewClickedListener mOnViewClickedListener;

    public void setOnViewClickedListener(ViewHolder.OnViewClickedListener onViewClickedListener) {
        mOnViewClickedListener = onViewClickedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_feed_element, parent, false);
        final ViewHolder holder = new ViewHolder(view, mOnViewClickedListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final News news = getNews(position);
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

    static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {
        NewsFeedElementBinding binding;
        private ViewHolder.OnViewClickedListener onViewClickListener;

        public ViewHolder(@NonNull View itemView, OnViewClickedListener listener) {
            super(itemView);
            binding = NewsFeedElementBinding.bind(itemView);
            itemView.setOnClickListener(this);
            binding.newsFeedBtnHtml.setOnClickListener(this);
            binding.newsFeedBtnBookmark.setOnClickListener(this);
            this.onViewClickListener = listener;
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
            void onHttpButtonClicked(ViewHolder holder);

            void onBookmarksButtonClicked(ViewHolder holder);

            void onElementClicked(ViewHolder holder);
        }
    }
}
