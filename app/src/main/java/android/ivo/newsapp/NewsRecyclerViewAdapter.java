package android.ivo.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, int position) {
        final News news = mNews.get(position);

        holder.titleView.setText(news.getTitle());
        holder.dateView.setText(news.getPublicationDate());
        holder.sectionName.setText(news.getSectionName());

        String byline = news.getByline();
        if (byline != null)
            holder.byline.setText(news.getByline());
        else
            holder.byline.setVisibility(View.GONE);

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getHttpUrl()));
                holder.rootView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mNews!=null)
            return mNews.size();
        return 0;
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        final TextView sectionName;
        final TextView titleView;
        final TextView dateView;
        final TextView byline;

        View rootView;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            dateView = itemView.findViewById(R.id.news_feed_date);
            titleView = itemView.findViewById(R.id.news_feed_title);
            sectionName = itemView.findViewById(R.id.news_feed_section);
            byline = itemView.findViewById(R.id.news_feed_byline);

            rootView = itemView;
        }
    }
}
