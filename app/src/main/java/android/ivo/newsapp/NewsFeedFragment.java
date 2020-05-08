package android.ivo.newsapp;

import android.app.Activity;
import android.content.Context;
import android.ivo.newsapp.databinding.NewsFragmentContainerBinding;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public class NewsFeedFragment extends Fragment implements MainActivity.OnNewsQueryComplete {
    private NewsFragmentContainerBinding mBinding;
    private NewsRecyclerViewAdapter mNewsAdapter;

    private ArrayList<News> mPageNews = new ArrayList<>();

    private int currentPage;

    @IntDef ( flag = true, value = {
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
        if (getArguments() != null)
            currentPage = getArguments().getInt("currentPage");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = NewsFragmentContainerBinding.inflate(inflater, container, false);

        //Setup the RecyclerView
        RecyclerView newsRecyclerView = mBinding.recyclerView;
        mNewsAdapter = new NewsRecyclerViewAdapter(mPageNews);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        newsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation()));
        newsRecyclerView.setLayoutManager(linearLayoutManager);
        newsRecyclerView.setAdapter(mNewsAdapter);

        // request http data for this page
        Bundle args = new Bundle();
        args.putInt("currentPage", currentPage);
        MainActivity mainActivity = (MainActivity) getActivity();
        // enqueue for http data
        mainActivity.registerFragmentForLoader(this, args);
        return mBinding.getRoot();
    }

    static NewsFeedFragment newInstance(int currentPage) {
        NewsFeedFragment fragment = new NewsFeedFragment();
        Bundle args = new Bundle();
        args.putInt("currentPage", currentPage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onNewsQueryComplete(ArrayList<News> newsData) {
        if (newsData == null) {
            // No incoming data. Check if there is network connection
            if (!HttpUtilities.clientIsConnectedToNetwork(requireContext()))
                setAdaptorState(State.NO_NETWORK);
                // Connection OK, so there is no data coming from the API
            else
                setAdaptorState(State.EMPTY);
        } else if (newsData.size() == 0) {
            // Nothing to display at this point. Inform the user.
            setAdaptorState(State.EMPTY);
        }
        else {
            //pass news response data to fragment
            setAdaptorState(State.VISIBLE);
            mNewsAdapter.addAll(newsData);
        }
    }

    private void setAdaptorState(int state) {
        TextView textDisplay = mBinding.activityMainEmptyInfoText;
        View layout = mBinding.activityMainLayout;

        if (state == State.EMPTY) {
            layout.setVisibility(View.GONE);
            textDisplay.setVisibility(View.VISIBLE);
            textDisplay.setText(getString(R.string.feedback_message_no_data));
        } else if (state == State.VISIBLE) {
            layout.setVisibility(View.VISIBLE);
            textDisplay.setVisibility(View.GONE);
        } else if (state == State.NO_NETWORK) {
            layout.setVisibility(View.GONE);
            textDisplay.setVisibility(View.VISIBLE);
            textDisplay.setText(getString(R.string.feedback_message_no_network));
        }
    }

}
