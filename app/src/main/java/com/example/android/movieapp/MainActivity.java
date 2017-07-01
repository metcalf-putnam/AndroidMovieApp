package com.example.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.android.movieapp.data.MoviePreferences;
import com.example.android.movieapp.utilities.NetworkUtils;
import com.example.android.movieapp.utilities.ParseMovieJsonUtils;
import org.json.JSONException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements RecyclerAdapter.MoviePosterClickListener{
    private int mMovieListSize;
    private RecyclerAdapter mAdapter;
    private String mPreferredSearch;
    private int mPageResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView moviePosterGrid;
        mMovieListSize = 0;
        mPageResults = 1;
        moviePosterGrid = (RecyclerView) findViewById(R.id.rv_movies);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        moviePosterGrid.setLayoutManager(layoutManager);
        moviePosterGrid.setHasFixedSize(true);
        moviePosterGrid.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                GridLayoutManager gridLayout = (GridLayoutManager) recyclerView.getLayoutManager();
                int lastPosition = gridLayout.findLastVisibleItemPosition() + 5;
                if(mMovieListSize != 0){

                    if(lastPosition >= mMovieListSize){
                        new fetchMovieData().execute(mPreferredSearch);
                    }
                }
            }
        });
        mAdapter = new RecyclerAdapter(this);
        moviePosterGrid.setAdapter(mAdapter);

        mPreferredSearch = MoviePreferences.getPreferred();

        new fetchMovieData().execute(mPreferredSearch);
    }

    private class fetchMovieData extends AsyncTask<String, Void, ArrayList<Movie>>{

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            ArrayList<Movie> movieArray;
            movieArray = new ArrayList<>();

            if (params.length == 0) {
                return null;
            }
            String preferredSearch = params[0];
            URL url = NetworkUtils.buildBaseSortURL(preferredSearch, mPageResults);
            String jsonData;

            try {
                jsonData = NetworkUtils.getResponseFromHttpUrl(url);
                movieArray = ParseMovieJsonUtils.getSimpleMovieStringsFromJson(jsonData);

            } catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            }
            mPageResults++;
            return movieArray;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieData) {

            if(movieData != null){
                String output = "";
                for(Movie m: movieData) {
                    output += m.toString();
                }
                mMovieListSize = mAdapter.setMovieData(movieData);
            }
        }

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

        if(clickedItemId == R.id.menu_popular && !mPreferredSearch.equals(mostPopular)){
            mPreferredSearch = mostPopular;
            resetSearch();
            new fetchMovieData().execute(mPreferredSearch);
            return true;
        }

        else if(clickedItemId == R.id.menu_top_rated && !mPreferredSearch.equals(topRated)){
            mPreferredSearch = topRated;
            resetSearch();
            new fetchMovieData().execute(mPreferredSearch);
            return true;
        }
        else {
            Toast.makeText(this, R.string.already_selected, Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
    }
    private void resetSearch(){
        mMovieListSize = 0;
        mAdapter.clearMovieData();
        mPageResults = 1;
    }
}
