package com.massurya.myasynctaskloader.Loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.massurya.myasynctaskloader.BuildConfig;
import com.massurya.myasynctaskloader.Constant.Constant;
import com.massurya.myasynctaskloader.Entity.MoviesItem;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class LoaderHome extends AsyncTaskLoader<ArrayList<MoviesItem>>{
    private ArrayList<MoviesItem> mData;
    private boolean mHasResult = false;

    public LoaderHome(final Context context) {
        super(context);
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        }else if (mHasResult) {
            deliverResult(mData);
        }
    }

    @Override
    public  void deliverResult(final ArrayList<MoviesItem> data) {
        mData = data;
        mHasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if(mHasResult) {
            mData = null;
            mHasResult = false;
        }
    }

    @Override
    public ArrayList<MoviesItem> loadInBackground(){
        SyncHttpClient client = new SyncHttpClient();
        final ArrayList<MoviesItem> moviesItems = new ArrayList<>();
        String url = BuildConfig.BASE_URL + Constant.Discovery +
                Constant.API_QUERY+ BuildConfig.API_KEY;
        Log.d("URL nya ",url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart(){
                super.onStart();
                setUseSynchronousMode(true);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list =responseObject.getJSONArray("results");
                    for (int  i= 0; i < list.length(); i++){
                        JSONObject movies = list.getJSONObject(i);
                        MoviesItem moviesItem = new MoviesItem(movies);
                        moviesItems.add(moviesItem);
                    }
                }catch (Exception e) {
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

