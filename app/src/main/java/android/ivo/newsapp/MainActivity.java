package android.ivo.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.ivo.newsapp.databinding.ActivityMainBinding;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<NewsResponse> {

    private ActivityMainBinding mBinding;
    private NewsFragmentPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private static final String TAG = "MainActivity";

    private static final String GUARDIAN_URL = "https://content.guardianapis.com/search";
    private static final String API_KEY = "test";

    private OnNewsQueryComplete mOnNewsQueryComplete = null;
    private Queue<FragmentArgs> mFragmentLoadingQueue = new LinkedList<>();

    private static class FragmentArgs {
        private Fragment mFragment;
        private Bundle mArgs;

        FragmentArgs(Fragment fragment, Bundle args) {
            mFragment = fragment;
            mArgs = args;
        }

        Fragment getFragment() {
            return mFragment;
        }

        Bundle getArgs() {
            return mArgs;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
         * Inflate and bind the views
         * */
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View root = mBinding.getRoot();
        setContentView(root);
        mViewPager = mBinding.activityMainViewPager;

        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(0, null, this);

        mAdapter = new NewsFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        /*
         * When the user types in the text we are refreshing the data from the Guardian
         * */
        mBinding.activityMainTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // clear any loading queue
                mFragmentLoadingQueue.clear();
                // Reload the data from the Guardian.
                mAdapter = new NewsFragmentPagerAdapter(getSupportFragmentManager());
                mViewPager.setAdapter(mAdapter);
            }
        });
    }

    private void restartLoader() {
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.restartLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<NewsResponse> onCreateLoader(int id, @Nullable Bundle args) {
        /*
         * Get the query from the UI input
         * */
        String query = mBinding.activityMainTextInput.getText().toString();
        /*
         * Build the URI Query
         * */
        Uri uri = Uri.parse(GUARDIAN_URL);
        Uri.Builder uriBuilder = uri.buildUpon();
        int currentPage = 1;
        if (args != null)
            currentPage = args.getInt("currentPage");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String orderQuery = sharedPreferences.getString(
                getString(R.string.menu_item_sort_key),
                getString(R.string.menu_item_sort_default));

        uriBuilder
                .appendQueryParameter("q", query)
                .appendQueryParameter("page", Integer.toString(currentPage))
                .appendQueryParameter("order-by", orderQuery)
                .appendQueryParameter("api-key", API_KEY)
                .appendQueryParameter("show-fields", "byline")
                .build();

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<NewsResponse> loader, NewsResponse data) {
        // After a new URI request has finished we load the data into the first fragment always
        if (mOnNewsQueryComplete != null) {
            // probably no internet connection
            if(data==null) {
                mOnNewsQueryComplete.onNewsQueryComplete(null);
                // nothing else to do here
                return;
            }
            else {
                // data is loaded call the waiting fragment
                mOnNewsQueryComplete.onNewsQueryComplete(data.getNews());
            }

            // the queue will be cleared if the activity gets destroyed and then resumed
            // so check the size before queueing a fragment for data
            if (mFragmentLoadingQueue.size() > 0) {
                // remove the fragment and start the next loading if there is a queue
                mFragmentLoadingQueue.remove();
                // push the next fragment for loading
                pushQueuedFragmentLoading(mFragmentLoadingQueue.peek());

                // only need to set the pager count when the first page loads
                if (data.getCurrentPage() == 1) {
                    mAdapter.setCount(data.getTotalPages());
                }
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<NewsResponse> loader) {
        // Remove any references pointing to the Loader
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_main_settings) {
            Intent intent = new Intent(this, PreferenceActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PreferenceActivity.REQUEST_UPDATE)
            restartLoader();
    }

    /**
     * Adds the fragment to a queue with http queries.
     * All the pager adapter fragments will be added to a queue in order to receive their
     * respective news data in a queued fashion. This is done because there is only one
     * Loader responsible for queries which can get only one query at a time.
     */
    public void registerFragmentForLoader(Fragment fragment, Bundle args) {
        // first fragment is always retrieving data until its removed from the queue
        FragmentArgs fragmentArgs = new FragmentArgs(fragment, args);
        mFragmentLoadingQueue.add(fragmentArgs);
        pushQueuedFragmentLoading(fragmentArgs);
    }

    private void pushQueuedFragmentLoading(FragmentArgs fragmentArgs) {
        // allow only one loading at a time so that
        // non of the fragments can cancel the http query
        if (mFragmentLoadingQueue.size() == 1) {
            // start loading
            LoaderManager loaderManager = LoaderManager.getInstance(this);
            loaderManager.restartLoader(0, fragmentArgs.getArgs(), this);
            // attach the fragment with the loading listener
            mOnNewsQueryComplete = (OnNewsQueryComplete) fragmentArgs.getFragment();
        }
    }

    /**
     * Implement this interface to receive a data of news when the loader has finished
     * retrieving data.
     */
    public interface OnNewsQueryComplete {
        void onNewsQueryComplete(ArrayList<News> newsData);
    }
}
