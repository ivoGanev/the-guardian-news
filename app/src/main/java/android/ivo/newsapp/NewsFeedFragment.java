package android.ivo.newsapp;

import android.content.Intent;
import android.ivo.newsapp.databinding.NewsFragmentContainerBinding;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public class NewsFeedFragment extends Fragment implements MainActivity.OnApiDataReceived {
    private static final String TAG = "NewsFeedFragment";
    private NewsFragmentContainerBinding mBinding;
    private NewsRecyclerViewAdapter mNewsAdapter;

    private ArrayList<News> mPageNews = new ArrayList<>();
    private final static String CURRENT_PAGE_BUNDLE_KEY = "currentPage";
    private int mCurrentPage;

    @IntDef(flag = true, value = {
            State.EMPTY,
            State.NO_NETWORK,
            State.VISIBLE})

    @Retention(RetentionPolicy.SOURCE)
    @interface State {
        int EMPTY = 0;
        int NO_NETWORK = 1;
        int VISIBLE = 2;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadBundleData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = NewsFragmentContainerBinding.inflate(inflater, container, false);
        initRecyclerView();
        fetchApiData();
        displayLoadingIndicator(true);
        return mBinding.getRoot();
    }

    private void loadBundleData() {
        if (getArguments() != null)
            mCurrentPage = getArguments().getInt(CURRENT_PAGE_BUNDLE_KEY);
    }

    private void fetchApiData() {
        Bundle args = new Bundle();
        args.putInt(CURRENT_PAGE_BUNDLE_KEY, mCurrentPage);
        MainActivity mainActivity = (MainActivity) getActivity();

        if(mainActivity!=null)
        mainActivity.enqueueForApiData(this, args);
        else
            Log.e(TAG, "fetchApiData: Main activity is null.");
    }

    private void initRecyclerView() {
        RecyclerView newsRecyclerView = mBinding.recyclerView;
        mNewsAdapter = new NewsRecyclerViewAdapter(mPageNews);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        newsRecyclerView.addItemDecoration(
                new DividerItemDecoration(requireContext(),
                        linearLayoutManager.getOrientation()));
        newsRecyclerView.setLayoutManager(linearLayoutManager);
        newsRecyclerView.setAdapter(mNewsAdapter);
    }

    static NewsFeedFragment newInstance(int currentPage) {
        NewsFeedFragment fragment = new NewsFeedFragment();
        Bundle args = new Bundle();
        args.putInt(CURRENT_PAGE_BUNDLE_KEY, currentPage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void handleReceivedApiData(ArrayList<News> newsData) {
        if (newsData == null) {
            // No incoming data. Check if there is network connection
            if (!NetworkUtilities.clientIsConnectedToNetwork(requireContext()))
                displayUiPageState(State.NO_NETWORK);
                // if the connection is OK, there is no data coming from the API
            else
                displayUiPageState(State.EMPTY);
        } else if (newsData.size() == 0) {
            displayUiPageState(State.EMPTY);
        } else {
            displayUiPageState(State.VISIBLE);
            mNewsAdapter.addAll(newsData);
        }
    }

    private void displayUiPageState(int state) {
        TextView infoText = mBinding.listInfoText;
        View layout = mBinding.recyclerView;

        displayLoadingIndicator(false);

        if (state == State.EMPTY) {
            swapVisibility(layout, infoText);
            infoText.setText(getString(R.string.feedback_message_no_data));
        } else if (state == State.VISIBLE) {
            swapVisibility(infoText, layout);
        } else if (state == State.NO_NETWORK) {
            swapVisibility(layout, infoText);
            infoText.setText(getString(R.string.feedback_message_no_network));
        }
    }

    private void displayLoadingIndicator(boolean value) {
        int visibility = (value) ? View.VISIBLE : View.GONE;
        mBinding.listProgressBar.setVisibility(visibility);
    }

    /**
     * Swaps the visibility for the two views
     * @param visibleView The view which will become gone
     * @param goneView The view which will become visible
     * */
    private void swapVisibility(View visibleView, View goneView) {
        visibleView.setVisibility(View.GONE);
        goneView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
