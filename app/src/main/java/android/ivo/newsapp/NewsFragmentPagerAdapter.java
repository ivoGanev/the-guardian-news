package android.ivo.newsapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class NewsFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private static final int INITIAL_COUNT = 1;
    private int mCount;

    NewsFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        mCount = INITIAL_COUNT;
    }

    void setCount(int count)
    {
        mCount = count;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // The guardian web API current page starts from 1 rather than 0
        int webApiPage = position + 1;
        return NewsFeedFragment.newInstance(webApiPage);
    }

    @Override
    public int getCount() {
        return mCount;
    }
}

