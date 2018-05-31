package com.massurya.myasynctaskloader.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.massurya.myasynctaskloader.Adapter.FavoriteAdapter;
import com.massurya.myasynctaskloader.Navigate;
import com.massurya.myasynctaskloader.R;

import static com.massurya.myasynctaskloader.ContentProvider.DatabaseContract.CONTENT_URI;

public class Favorite extends Fragment {

    private Cursor list;
    private Navigate mainActivity;
    private Toolbar toolbar;
    private FavoriteAdapter adapter;
    private RecyclerView recyclerView;

    public Favorite() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (Navigate) getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mainActivity.setupNavigationDrawer(toolbar);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_circle, container, false);
        adapter = new FavoriteAdapter(list);
        recyclerView = view.findViewById(R.id.recycler_view);
        ProgressBar progressBar = view.findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        toolbar = view.findViewById(R.id.toolbar);
        setupToolbar();
        new loadDataAsync().execute();

        return view;
    }

    private void setupToolbar() {
        toolbar.setTitle(R.string.favorite_movies);
        mainActivity.setSupportActionBar(toolbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        new loadDataAsync().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class loadDataAsync extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContext().getContentResolver().query(
                    CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            list = cursor;
            adapter.replaceAll(list);
        }
    }
}