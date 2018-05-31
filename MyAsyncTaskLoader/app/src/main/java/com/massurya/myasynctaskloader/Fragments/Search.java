package com.massurya.myasynctaskloader.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.massurya.myasynctaskloader.Adapter.MoviesAdapter;
import com.massurya.myasynctaskloader.Entity.MoviesItem;
import com.massurya.myasynctaskloader.Loader.LoaderSearch;
import com.massurya.myasynctaskloader.Navigate;
import com.massurya.myasynctaskloader.R;

import java.util.ArrayList;

public class Search extends Fragment implements LoaderManager
        .LoaderCallbacks<ArrayList<MoviesItem>> {

    private Navigate mainActivity;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private MoviesAdapter movieAdapter;
    private ProgressBar mProgress;
    private String search_title;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (Navigate) getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        movieAdapter = new MoviesAdapter(getActivity());
        mainActivity.setupNavigationDrawer(toolbar);
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_circle, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        setupToolbar();
        setHasOptionsMenu(true);
        mProgress = view.findViewById(R.id.progress);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.INVISIBLE);
        mProgress.setVisibility(View.VISIBLE);
        return view;
    }

    private void setupToolbar() {
        toolbar.setTitle(R.string.search_movie);
        mainActivity.setSupportActionBar(toolbar);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);
        final MenuItem search = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setMaxWidth(0);
        searchView.setQueryHint(getResources().getString(R.string.hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search_title = query;
                submit();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void submit() {
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public android.support.v4.content.Loader<ArrayList<MoviesItem>> onCreateLoader(int d, Bundle args) {

        return new LoaderSearch(getActivity(), search_title);

    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<ArrayList<MoviesItem>> loader, ArrayList<MoviesItem> data) {
        recyclerView.setVisibility(View.VISIBLE);
        movieAdapter.setMovieItemNowPlayings(data);
        recyclerView.setAdapter(movieAdapter);
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<ArrayList<MoviesItem>> loader) {
        movieAdapter.clearMovie();
    }

}

