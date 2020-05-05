package android.ivo.newsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

final class HttpUtilities {
    private static final String TAG = "HttpHandler";

    private HttpUtilities() {
    }

    static boolean clientIsConnectedToNetwork(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return  networkInfo.isConnectedOrConnecting();
            }
        }
        return false;
    }

    static String retrieveJsonData(String urlString) throws IOException {
        String json = "";
        HttpURLConnection httpURLConnection;
        InputStream stream;

        URL url = stringToURL(urlString);

        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setReadTimeout(5000);
        httpURLConnection.connect();

        if (httpURLConnection.getResponseCode() == 200) {
            //OK
            stream = httpURLConnection.getInputStream();

            StringBuilder stringBuilder = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader r = new BufferedReader(reader);

            String line = r.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = r.readLine();
            }

            stream.close();
            httpURLConnection.disconnect();
            json = stringBuilder.toString();

        } else {
            Log.e(TAG, "retrieveJsonData: Connection response code: " + httpURLConnection.getResponseCode());
        }

        return json;
    }

    private static URL stringToURL(String value) {
        URL result = null;
        try {
            result = new URL(value);
        }
        catch (MalformedURLException e) {
            Log.e(TAG, "stringToURL: Malformed URL Exception");
        }
        return  result;
    }



}
