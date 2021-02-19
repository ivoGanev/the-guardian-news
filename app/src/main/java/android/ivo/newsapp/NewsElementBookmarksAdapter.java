package android.ivo.newsapp;

import android.ivo.newsapp.databinding.ActivityBookmarksBinding;
import android.ivo.newsapp.databinding.BookmarksElementBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class NewsElementBookmarksAdapter extends NewsElementAdapter<NewsElementBookmarksAdapter.ViewHolder> {
    ViewHolder.OnViewClickListener mOnViewClickListener;

    public NewsElementBookmarksAdapter(ViewHolder.OnViewClickListener onViewClickListener) {
        mOnViewClickListener = onViewClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmarks_element, parent, false);
        return new ViewHolder(view, mOnViewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = getNews(position);
        holder.binding.bookmarkTvTitle.setText(news.getTitle());
        holder.binding.bookmarkTvSection.setText(news.getSectionName());
        holder.binding.bookmarkTvDate.setText(news.getPublicationDate());
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        BookmarksElementBinding binding;
        private OnViewClickListener mOnViewClickListener;

        public ViewHolder(@NonNull View itemView, OnViewClickListener listener) {
            super(itemView);
            binding = BookmarksElementBinding.bind(itemView);
            binding.bookmarkBtnDelete.setOnClickListener(this);
            mOnViewClickListener = listener;
        }

        @Override
        public void onClick(View v) {
            if (mOnViewClickListener != null)
                mOnViewClickListener.OnDeleteClicked(this);
        }

        interface OnViewClickListener {
            void OnDeleteClicked(ViewHolder holder);
        }
    }
}
