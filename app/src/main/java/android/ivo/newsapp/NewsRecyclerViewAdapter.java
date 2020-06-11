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

    private OnViewClickedListener onViewClickedListener;
    private static final String TAG = NewsRecyclerViewAdapter.class.getSimpleName();
    private List<News> news;

    NewsRecyclerViewAdapter(List<News> news) {
        this.news = news;
    }

    void addAll(List<News> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_feed_element, parent, false);
        final NewsViewHolder holder = new NewsViewHolder(view);
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
    public void onBindViewHolder(@NonNull NewsViewHolder holder, final int position) {
        final News news = this.news.get(position);
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
        return news.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        if (news != null)
            return news.size();
        return 0;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final NewsFeedElementBinding binding;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NewsFeedElementBinding.bind(itemView);
            itemView.setOnClickListener(this);
            binding.newsFeedBtnHtml.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.news_feed_btn_html) {
                onViewClickedListener.onHttpButtonClicked(news.get(getAdapterPosition()), v);
            } else if (id == R.id.news_feed_layout) {
                notifyItemChanged(getAdapterPosition(), "visibility");
            }
        }
    }

    public void setOnViewClickedListener(OnViewClickedListener listener) {
        this.onViewClickedListener = listener;
    }

    interface OnViewClickedListener {
        void onHttpButtonClicked(News news, View view);
    }
}
