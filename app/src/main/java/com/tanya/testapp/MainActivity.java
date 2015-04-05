package com.tanya.testapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tanya.testapp.utils.AppUtil;

import java.net.URISyntaxException;
import java.util.List;

public class MainActivity extends ActionBarActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static String TAG = MainActivity.class.getSimpleName();

    private static final int SMS_LOADER = 0;
    private static final String SEARCH_FILTER = "apptest.com/i?id=";


    private Context mContext;
    private TextView mResultText;
    private TextView mLogsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mResultText = (TextView)findViewById(R.id.resultText);
        mLogsText = (TextView)findViewById(R.id.logsText);

        mResultText.setVisibility(View.GONE);
        mLogsText.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onStartClick(View view){
        // start sms reading
        getLoaderManager().restartLoader(SMS_LOADER, null, this);
        mResultText.setText("Server responses:");
        mLogsText.setText("Logs:");
        mResultText.setVisibility(View.VISIBLE);
        mLogsText.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case SMS_LOADER:
                Uri uri = Uri.parse("content://sms/inbox");

                String[] mProjection = {"address", "body"};

                String mSelectionClause = "body LIKE '%"+ SEARCH_FILTER +"%'";

                // Returns a new CursorLoader
                return new CursorLoader(
                        this,               // activity context
                        uri,                // Table to query
                        mProjection,        // Projection to return
                        mSelectionClause,   // No selection clause
                        null,               // No selection arguments
                        null                // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.moveToFirst()) {
            for(int i=0; i < data.getCount(); i++) {
                analiseSms(data);
                data.moveToNext();
            }
        } else {
            mLogsText.append("\n Not found \n");
            mResultText.append("\n Not found \n");
        }
        data.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    // gets and parse SMS data to extract "id" value from url
    private void analiseSms(Cursor data){
        // read sms
        String address = data.getString(data.getColumnIndexOrThrow("address")).toString();
        String body = data.getString(data.getColumnIndexOrThrow("body")).toString();

        mLogsText.append("\nSMS From: " + address + "\n");
        Log.i(TAG, "SMS From: " + address + "\n");

        mLogsText.append("Message: " + body + "\n");
        Log.i(TAG, "Message: " + body + "\n");

        List<String> urls = AppUtil.extractUrls(body);

        try {
            for(String url : urls) {
                String id = AppUtil.extractParamsFromURL(url).get("id");
                Log.i(TAG, "id = " + id);
                new WebTask().execute(id);
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    private class WebTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... query) {
            return WebClient.get().makeHttpRequest(mContext, query[0]);
        }


        @Override
        protected void onPostExecute(final String data) {
            if(data == null) return;

            Log.i("API response", data);

            //show result in UI
            mResultText.append("\n"+ data);
        }

        @Override
        protected void onCancelled() {
        }

    }
}
