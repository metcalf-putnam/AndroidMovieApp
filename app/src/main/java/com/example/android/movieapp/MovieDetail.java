package com.example.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieapp.utilities.NetworkUtils;
import com.example.android.movieapp.utilities.ParseMovieReviewsUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Patrice Metcalf-Putnam
 */

public class MovieDetail extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<HashMap<String, String>>,
                    View.OnClickListener {
    private static final String MOVIE_EXTRA = "movie";
    private static final String ID_EXTRA = "id";
    private static final String REVIEWS_EXTRA = "reviews";
    private static final String TRAILERS_EXTRA = "trailers";
    private static final String TRAILER_EXTRA = "TRAILER";
    private static final String NAME_EXTRA = "NAME";
    private static final int MOVIE_LOADER = 20;
    private static ArrayList<String> mTrailers = new ArrayList<String>();
    private static ArrayList<String> mTrailerNames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moviedetail);


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(MOVIE_EXTRA)) {
                Movie m = intentThatStartedThisActivity.getParcelableExtra(MOVIE_EXTRA);
                ImageView poster = (ImageView) findViewById(R.id.iv_movieDetailPoster);
                TextView title = (TextView) findViewById(R.id.tv_originalTitle);
                title.setText(m.getOriginalTitle());

                String averageUserRating = getResources().getString(R.string.detail_average_user_rating, m.getVoteAverage());
                TextView userRating = (TextView) findViewById(R.id.tv_userRating);
                userRating.setText(averageUserRating);

                String date = m.getReleaseDate();
                TextView releaseDate = (TextView) findViewById(R.id.tv_releaseDate);
                releaseDate.setText(date);

                TextView overview = (TextView) findViewById(R.id.tv_overview);
                overview.setText(m.getOverview());

                Picasso.with(this).load(m.getPosterPath())
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.drawable.ic_error)
                        .into(poster);

                ImageView playTrailer = (ImageView) findViewById(R.id.iv_play_trailer);
                playTrailer.setOnClickListener(this);

                String movieId = m.getId();
                callLoader(movieId);

            }
        }
    }

    private void callLoader(String movieId){
        Bundle queryBundle = new Bundle();
        queryBundle.putString(ID_EXTRA, movieId);
        LoaderManager loaderManager = getSupportLoaderManager();

        Loader<ArrayList<Movie>> movieLoader = loaderManager.getLoader(MOVIE_LOADER);

        if(movieLoader == null){
            loaderManager.initLoader(MOVIE_LOADER, queryBundle, this);
        }
        else{
            loaderManager.restartLoader(MOVIE_LOADER, queryBundle, this);
        }
    }
    @Override
    public Loader<HashMap<String, String>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<HashMap<String, String>>(this) {

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
            public HashMap<String, String> loadInBackground() {
                String movieId = args.getString(ID_EXTRA);
                if(movieId == null || TextUtils.isEmpty(movieId)){
                    return null;
                }
                HashMap<String, String> hash = new HashMap();

                String trailers = NetworkUtils.getTrailers(movieId);
                hash.put(TRAILERS_EXTRA, trailers);

                String movieReviews = NetworkUtils.getReviews(movieId);
                hash.put(REVIEWS_EXTRA, movieReviews);


                return hash;

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<HashMap<String, String>> loader, HashMap<String, String> movieDetails) {
        TextView reviewsTV = (TextView) findViewById(R.id.tv_reviews);
        mTrailerNames.clear();
        mTrailers.clear();
        if(movieDetails != null){
            String reviews = movieDetails.get(REVIEWS_EXTRA);
            if(reviews == null || TextUtils.isEmpty(reviews)){
                reviewsTV.setText(R.string.no_reviews);
            }
            else{
                String prettyReviews = ParseMovieReviewsUtil
                        .getPrettyReviews(ParseMovieReviewsUtil.getJsonArray(reviews));
                if(prettyReviews == null || TextUtils.isEmpty(prettyReviews)){
                    reviewsTV.setText(R.string.no_reviews);
                }else{
                    reviewsTV.setText(prettyReviews);
                }
            }
            String trailersJson = movieDetails.get(TRAILERS_EXTRA);
            ArrayList<HashMap<String, String>> trailerArray = ParseMovieReviewsUtil.getTrailerDetails(trailersJson);
            int length = trailerArray.size();
            if(length > 0) {
                for (int i = 0; i < length; i++) {
                    HashMap<String, String> trailer = trailerArray.get(i);
                    String name = trailer.get(NAME_EXTRA);
                    String video = trailer.get(TRAILER_EXTRA);
                    mTrailerNames.add(name);
                    mTrailers.add(video);
                }
            }
        } else{
            reviewsTV.setText(R.string.review_error);
        }
        addSpinnerOptions();
    }

    @Override
    public void onLoaderReset(Loader<HashMap<String, String>> loader) {

    }
    //modified from code found here:
    // https://www.mkyong.com/android/android-spinner-drop-down-list-example/
    private void addSpinnerOptions(){
        Spinner spinner = (Spinner) findViewById(R.id.spinner_trailer);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mTrailerNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    //modified from example code
    // here: https://developer.android.com/guide/components/intents-common.html#Music
    public void playMedia(Uri http) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(http);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_play_trailer:case R.id.tv_play_trailer:
                if(!mTrailers.isEmpty()){
                    Spinner spinner = (Spinner) findViewById(R.id.spinner_trailer);
                    int spinnerPosition = spinner.getSelectedItemPosition();
                    String trailerUrl = mTrailers.get(spinnerPosition);
                    Uri trailerUri = Uri.parse(trailerUrl);
                    playMedia(trailerUri);
                }
                break;
        }

    }
}
