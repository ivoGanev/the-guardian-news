package android.ivo.newsapp;

import java.util.ArrayList;
import java.util.List;

class NewsResponse {
    private ArrayList<News> mNews;
    private int mCurrentPage;
    private int mTotalPages;

    NewsResponse(ArrayList<News> news, int currentPage, int totalPages) {
        mNews = news;
        mCurrentPage = currentPage;
        mTotalPages = totalPages;
    }

    ArrayList<News> getNews() {
        return mNews;
    }

    int getCurrentPage() {
        return mCurrentPage;
    }

    int getTotalPages() {
        return mTotalPages;
    }
}
