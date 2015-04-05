package com.tanya.testapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tanya.testapp.utils.Streams;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tatyana on 05.04.15.
 */
public class WebClient {
    static final String TAG = WebClient.class.getSimpleName();

    private static final WebClient sInstance = new WebClient();

    public static WebClient get() {
        return sInstance;
    }

    private WebClient() {
        super();
    }

    private static String SERVER_URL = "http://app.mobilenobo.com/c/apptest?id=";

    public String makeHttpRequest( Context mContext, String query) {
        final String urlStr = getUrl(query);
        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(urlStr);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            String responseData = Streams.read(in);
            return responseData;

        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(mContext, "Server error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(mContext, "Server error" + e.getMessage(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(mContext, "Server error" + e.getMessage(), Toast.LENGTH_SHORT).show();

        } finally {
            urlConnection.disconnect();

        }
        return null;
    }

    private String getUrl(String query) {
            return SERVER_URL + query;
        }
}
