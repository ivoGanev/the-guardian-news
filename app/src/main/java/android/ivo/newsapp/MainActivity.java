package android.ivo.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.ivo.newsapp.databinding.ActivityMainBinding;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private ActivityMainBinding mActivityMainBinding;
    private NewsRecyclerViewAdapter mAdapter;

    private static final String GUARDIAN_URL = "https://content.guardianapis.com/search";
    private static final String API_KEY = "test";

    private static final int STATE_EMPTY = -1;
    private static final int STATE_NO_NETWORK = 0;
    private static final int STATE_VISIBLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
         * Inflate and bind the views
         * */
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View root = mActivityMainBinding.getRoot();
        setContentView(root);

        /*
         * Setup the RecyclerView
         * */
        RecyclerView recyclerView = mActivityMainBinding.recyclerView;
        mAdapter = new NewsRecyclerViewAdapter(new ArrayList<News>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()))
        ;
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);

        /*
         * When the user types in the text we are refreshing the data from the Guardian
         * */
        mActivityMainBinding.activityMainTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Reload the data from the Guardian.
                restartLoader();
            }
        });

        /*
         * Continue loading
         * */
        getSupportLoaderManager().initLoader(0, null, this);
    }

    private void restartLoader() {
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        /*
         * Get the query from the UI input
         * */
        String query = mActivityMainBinding.activityMainTextInput.getText().toString();
        /*
         * Build the URI Query
         * */
        Uri uri = Uri.parse(GUARDIAN_URL);
        Uri.Builder uriBuilder = uri.buildUpon();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String orderQuery = sharedPreferences.getString(
                getString(R.string.menu_item_sort_key),
                getString(R.string.menu_item_sort_default));

        uriBuilder
                .appendQueryParameter("q", query)
                .appendQueryParameter("order-by", orderQuery)
                .appendQueryParameter("api-key", API_KEY)
                .appendQueryParameter("show-fields", "byline")
                .build();

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
        /*
         * Check for network connection or if there is any data to display and give feedback to the user.
         * */
        if (!HttpUtilities.clientIsConnectedToNetwork(this)) {
            setAdaptorState(STATE_NO_NETWORK);
        } else if (data == null || data.size() == 0)
            setAdaptorState(STATE_EMPTY);
        else if (mAdapter != null) {
            mAdapter.clear();
            mAdapter.addAll(data);
            setAdaptorState(STATE_VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        // Remove any references pointing to the Loader
        mAdapter.clear();
    }

    void setAdaptorState(int state) {
        TextView textDisplay = mActivityMainBinding.activityMainEmptyInfoText;
        View layout = mActivityMainBinding.activityMainLayout;

        if (state == STATE_EMPTY) {
            layout.setVisibility(View.GONE);
            textDisplay.setVisibility(View.VISIBLE);
            textDisplay.setText(getString(R.string.feedback_message_no_data));
        } else if (state == STATE_VISIBLE) {
            layout.setVisibility(View.VISIBLE);
            textDisplay.setVisibility(View.GONE);
        } else if (state == STATE_NO_NETWORK) {
            layout.setVisibility(View.GONE);
            textDisplay.setVisibility(View.VISIBLE);
            textDisplay.setText(getString(R.string.feedback_message_no_network));
        }
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
        if(requestCode==PreferenceActivity.REQUEST_UPDATE)
            restartLoader();
    }
}
