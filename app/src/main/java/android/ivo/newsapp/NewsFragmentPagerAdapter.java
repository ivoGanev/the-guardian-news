package android.ivo.newsapp;

import android.content.Context;
import android.ivo.newsapp.databinding.NewsFragmentContainerBinding;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.preference.ListPreference;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewsFragmentPagerAdapter extends FragmentStatePagerAdapter {
    // Need an initial count so at least one page gets created
    // and a NewsFeedFragment starts getting the http information.
    // After that the count will be set from the first fragment
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
        // associate the fragment with a page number.
        // The guardian web API current page starts from 1 rather than 0
        int webApiPage = position + 1;
        return NewsFeedFragment.newInstance(webApiPage);
    }

    @Override
    public int getCount() {
        return mCount;
    }
}

