package com.example.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieapp.data.MoviePreferences;
import com.example.android.movieapp.utilities.NetworkUtils;
import com.example.android.movieapp.utilities.ParseMovieJsonUtils;
import org.json.JSONException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements RecyclerAdapter.MoviePosterClickListener, LoaderManager.LoaderCallbacks<ArrayList<Movie>>{
    private int mMovieListSize = 0;
    private RecyclerAdapter mAdapter;
    private String mPreferredSearch;
    private int mPageResults = 1;
    private static final int MOVIE_DB_SEARCH_LOADER = 15;
    private static final String SEARCH_EXTRA = "searchExtra";
    private static final String PAGE_RESULTS_EXTRA = "pages";
    private static final String MOVIE_LIST_SIZE_EXTRA = "listSize";
    private static final String MOVIE_RESULTS_EXTRA = "movieList";
    private static final String PREFERRED_SEARCH_EXTRA = "preferred";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new RecyclerAdapter(this);
        RecyclerView moviePosterGrid;
        moviePosterGrid = (RecyclerView) findViewById(R.id.rv_movies);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4); //hardcoded number;;need change
        moviePosterGrid.setLayoutManager(layoutManager);
        moviePosterGrid.setHasFixedSize(true);
        moviePosterGrid.setAdapter(mAdapter);
        mPreferredSearch = MoviePreferences.getPreferred();

        if(savedInstanceState != null){
            mPageResults = savedInstanceState.getInt(PAGE_RESULTS_EXTRA);
            mMovieListSize = savedInstanceState.getInt(MOVIE_LIST_SIZE_EXTRA);
            ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(MOVIE_RESULTS_EXTRA);
            mAdapter.setMovieData(movies);
            mPreferredSearch = savedInstanceState.getString(PREFERRED_SEARCH_EXTRA);
        }
        if(!checkConnection()) {
            return; //not connected
        }

        moviePosterGrid.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(checkConnection()) {
                    GridLayoutManager gridLayout = (GridLayoutManager) recyclerView.getLayoutManager();
                    int lastPosition = gridLayout.findLastVisibleItemPosition() + 5;
                    if (mMovieListSize != 0) {
                        if (lastPosition >= mMovieListSize) {
                            callLoader();
                        }
                    }
                }
            }
        });


        callLoader();
    }

    private void callLoader(){
        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_EXTRA, mPreferredSearch);
        LoaderManager loaderManager = getSupportLoaderManager();

        Loader<ArrayList<Movie>> movieLoader = loaderManager.getLoader(MOVIE_DB_SEARCH_LOADER);

        if(movieLoader == null){
            loaderManager.initLoader(MOVIE_DB_SEARCH_LOADER, queryBundle, this);
        }
        else{
            loaderManager.restartLoader(MOVIE_DB_SEARCH_LOADER, queryBundle, this);
        }
    }
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle args) {
        if(!checkConnection()){
            return null;
        }
        return new AsyncTaskLoader<ArrayList<Movie>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args == null){
                    return;
                } else {
                    forceLoad();
                }
            }

            @Override
            public ArrayList<Movie> loadInBackground() {
                String searchInfo = args.getString(SEARCH_EXTRA);
                if(searchInfo == null || TextUtils.isEmpty(searchInfo)){
                    return null;
                }

                ArrayList<Movie> movieArray;
                movieArray = new ArrayList<>();
                URL url = NetworkUtils.buildBaseSortURL(searchInfo, mPageResults);
                String jsonData;
                if(NetworkUtils.isNetworkAvailable(MainActivity.this)) {
                    try {
                        jsonData = NetworkUtils.getResponseFromHttpUrl(url);

                        movieArray = ParseMovieJsonUtils.getSimpleMovieStringsFromJson(jsonData);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mPageResults++;
                    return movieArray;
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> movieData) {

        if(movieData != null){
            mMovieListSize = mAdapter.setMovieData(movieData);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }

    @Override
    public void onMoviePosterClick(Movie movie) {

        Context context = MainActivity.this;
        Class destinationActivity = MovieDetail.class;
        Intent startDetailActivity = new Intent(context, destinationActivity);

        startDetailActivity.putExtra("movie", movie);
        startActivity(startDetailActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_type_toggle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int clickedItemId = item.getItemId();
        String mostPopular = NetworkUtils.getMostPopularMoviesSearch();
        String topRated = NetworkUtils.getTopRatedMoviesSearch();
        switch(clickedItemId){
            case R.id.menu_popular:
                mPreferredSearch = mostPopular;
                break;
            case R.id.menu_top_rated:
                mPreferredSearch = topRated;
                break;
            case R.id.menu_favorites: //need to update this
                break;
        }
        if(checkConnection()){
            resetSearch();
            callLoader();
        }
        return super.onOptionsItemSelected(item);
    }
    private void resetSearch(){
        mMovieListSize = 0;
        if(mAdapter != null) {
            mAdapter.clearMovieData();
        }
        mPageResults = 1;
    }
    private boolean checkConnection(){
        TextView error = (TextView) findViewById(R.id.tv_error);
        if(NetworkUtils.isNetworkAvailable(this)){
            error.setVisibility(View.INVISIBLE);
            return true;
        }else{
            error.setText(R.string.error_no_internet);
            error.setVisibility(View.VISIBLE);
            return false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(PAGE_RESULTS_EXTRA, mPageResults);
        outState.putInt(MOVIE_LIST_SIZE_EXTRA, mMovieListSize);
        outState.putString(PREFERRED_SEARCH_EXTRA, mPreferredSearch);
        outState.putParcelableArrayList(MOVIE_RESULTS_EXTRA, mAdapter.getMoviesArray());
    }
}
