package android.ivo.newsapp;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class NewsLoader extends AsyncTaskLoader<NewsResponse> {
    private static final String TAG = "NewsLoader";
    private String mUri;

    NewsLoader(@NonNull Context context, String uri) {
        super(context);
        mUri = uri;
    }

    @Override
    public void onCanceled(@Nullable NewsResponse data) {
        super.onCanceled(data);
        Log.d(TAG, "onCanceled: ");
    }

    @Nullable
    @Override
    public NewsResponse loadInBackground() {
        /*
         * Retrieve the JSON data and bind it to a list
         * */
        NewsResponse newsResponse = null;
        try {
            String json = HttpUtilities.retrieveJsonData(mUri);
            newsResponse = BindJsonData(json);

        } catch (IOException e) {
            Log.e(TAG, "loadInBackground: " + e);
        }
        return newsResponse;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    private NewsResponse BindJsonData(String json) {
        NewsResponse newsResponse = new NewsResponse(null, 0, 0);

        try {
            ArrayList<News> news = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(json);
            JSONObject response = jsonObject.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            int currentPage = response.getInt("currentPage");
            int totalPages = response.getInt("total");

            for (int i = 0; i < results.length(); i++) {
                JSONObject element = results.getJSONObject(i);
                JSONObject fields = null;
                try {
                    fields = element.getJSONObject("fields");
                } catch (JSONException e) {
                    // sometime fields is null and exception is being thrown so it needs to be caught
                }

                String date = element.getString("webPublicationDate");
                String webTitle = element.getString("webTitle");
                String httpUrl = element.getString("webUrl");
                String sectionName = element.getString("sectionName");

                String byline = null;
                if (fields != null)
                    byline = fields.getString("byline");

                News newsElement = new News.Builder()
                        .publicationDate(date)
                        .sectionName(sectionName)
                        .title(webTitle)
                        .apiUrl(httpUrl)
                        .byline(byline)
                        .build();

                news.add(newsElement);
            }

            newsResponse = new NewsResponse(news, currentPage, totalPages);

        } catch (JSONException e) {
            Log.e(TAG, "BindJsonData: " + e);
        }

        return newsResponse;
    }


}
