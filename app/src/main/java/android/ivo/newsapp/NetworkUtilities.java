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
import java.util.Scanner;

final class NetworkUtilities {
    private static final String TAG = "HttpHandler";

    private NetworkUtilities() {
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
        InputStream in;

        URL url = stringToURL(urlString);

        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setReadTimeout(5000);
        httpURLConnection.connect();

        if (httpURLConnection.getResponseCode() == 200) {
            // Connection established
            in = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                json = scanner.next();
            }

            in.close();
            httpURLConnection.disconnect();

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
