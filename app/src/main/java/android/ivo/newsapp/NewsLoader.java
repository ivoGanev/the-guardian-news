package android.ivo.newsapp;

import android.content.Context;
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

class NewsLoader extends AsyncTaskLoader<List<News>> {
    private static final String TAG = "NewsLoader";
    private String mUri;

    NewsLoader(@NonNull Context context, String uri) {
        super(context);
        mUri = uri;
    }

    @Nullable
    @Override
    public List<News> loadInBackground() {
        /*
         * Retrieve the JSON data and bind it to a list
         * */
        ArrayList<News> arrayList = new ArrayList<News>();
        try {
            String json = HttpUtilities.retrieveJsonData(mUri);
            arrayList = BindJsonData(json);

        } catch (IOException e) {
            Log.e(TAG, "loadInBackground: " + e);
        }
        return arrayList;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    private ArrayList<News> BindJsonData(String json) {
        ArrayList<News> newsArray = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject response = jsonObject.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject element = results.getJSONObject(i);
                JSONObject fields = null;
                try {
                    fields = element.getJSONObject("fields");
                } catch (JSONException e) {
                    // Silent exception
                }

                String date = element.getString("webPublicationDate");
                String webTitle = element.getString("webTitle");
                String httpUrl = element.getString("webUrl");
                String sectionName = element.getString("sectionName");

                String byline = null;
                if(fields!=null)
                   byline = fields.getString("byline");

                News newsElement = new News.Builder()
                        .publicationDate(date)
                        .sectionName(sectionName)
                        .title(webTitle)
                        .apiUrl(httpUrl)
                        .byline(byline)
                        .build();

                newsArray.add(newsElement);
            }

        } catch (JSONException e) {
            Log.e(TAG, "BindJsonData: " + e);
        }

        return newsArray;
    }

}
