package com.massurya.myasynctaskloader.Loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.massurya.myasynctaskloader.BuildConfig;
import com.massurya.myasynctaskloader.Constant.Constant;
import com.massurya.myasynctaskloader.Entity.MoviesItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.massurya.myasynctaskloader.Constant.Constant.Movie_Search;
import static com.massurya.myasynctaskloader.Constant.Constant.Query_Search;

public class LoaderSearch extends AsyncTaskLoader<ArrayList<MoviesItem>> {
    private ArrayList<MoviesItem> mData;
    private boolean mHasResult = false;

    private String query;

    public LoaderSearch(final Context context, String query) {
        super(context);
        onContentChanged();
        this.query = query;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        } else if (mHasResult) {
            deliverResult(mData);
        }
    }

    @Override
    public void deliverResult(final ArrayList<MoviesItem> data) {
        mData = data;
        mHasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mHasResult) {
            mData = null;
            mHasResult = false;
        }
    }

    @Override
    public ArrayList<MoviesItem> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();
        final ArrayList<MoviesItem> moviesItems = new ArrayList<>();
        String url = BuildConfig.BASE_URL + Movie_Search +
                Constant.API_QUERY + BuildConfig.API_KEY + Query_Search + query;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movies = list.getJSONObject(i);
                        MoviesItem moviesItem = new MoviesItem(movies);
                        moviesItems.add(moviesItem);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                                  Throwable error) {

            }
        });

        return moviesItems;
    }

}
