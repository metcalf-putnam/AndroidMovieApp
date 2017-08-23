package com.example.android.movieapp.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.TextView;

import com.example.android.movieapp.MainActivity;
import com.example.android.movieapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import static java.security.AccessController.getContext;

/**
 * @author Patrice Metcalf-Putnam
 */

public class NetworkUtils {
    private static final String MY_API_KEY = ApiKey.getApi();

    private static final String BASE_MOVIE_SEARCH_URL = "api.themoviedb.org";
    private static final String AUTH_TYPE = "3";
    private static final String MEDIA_TYPE_MOVIE = "movie";
    private static final String API_BASE = "api_key";
    private static final String LANGUAGE = "language";
    private static final String ENGLISH_DEFAULT = "en-US";
    private static final String PAGE = "page";

    private static final String MOST_POPULAR_MOVIES_SEARCH = "popular";
    private static final String TOP_RATED_MOVIES_SEARCH = "top_rated";
    private static final String REVIEWS_SEARCH = "reviews";
    private static final String TRAILERS_SEARCH = "videos";

    private static final String BASE_MOVIE_POSTER_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w185/";

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    public static String getMostPopularMoviesSearch(){
        return MOST_POPULAR_MOVIES_SEARCH;
    }
    public static String getTopRatedMoviesSearch() { return TOP_RATED_MOVIES_SEARCH; }

    public static String getReviews(String movieId){
        URL reviewUrl = buildDetailedMovieSearchUrl(movieId, REVIEWS_SEARCH);
        String reviews = null;
        try {
            reviews = getResponseFromHttpUrl(reviewUrl);
        }catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return reviews;
    }

    public static String getTrailers(String movieId){
        URL trailerUrl = buildDetailedMovieSearchUrl(movieId, TRAILERS_SEARCH);
        String trailers = null;
        try{
            trailers = getResponseFromHttpUrl(trailerUrl);
        }catch(IOException e){
            e.printStackTrace();
            return e.getMessage();
        }
        return trailers;
    }
    private static URL buildDetailedMovieSearchUrl(String movieId, String query){
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")
                .authority(BASE_MOVIE_SEARCH_URL)
                .appendPath(AUTH_TYPE)
                .appendPath(MEDIA_TYPE_MOVIE)
                .appendPath(movieId)
                .appendPath(query)
                .appendQueryParameter(API_BASE, MY_API_KEY)
                .appendQueryParameter(LANGUAGE, ENGLISH_DEFAULT)
                .appendQueryParameter(PAGE, "1")
                .build();
        return makeUrlFromString(builder.toString());
    }

    public static URL buildBaseSortURL(String query, int page){
        Uri.Builder builder = new Uri.Builder();
        String pageString = Integer.toString(page);

        builder.scheme("https")
                .authority(BASE_MOVIE_SEARCH_URL)
                .appendPath(AUTH_TYPE)
                .appendPath(MEDIA_TYPE_MOVIE)
                .appendPath(query)
                .appendQueryParameter(API_BASE, MY_API_KEY)
                .appendQueryParameter(LANGUAGE, ENGLISH_DEFAULT)
                .appendQueryParameter(PAGE, pageString)
                .build();
        return makeUrlFromString(builder.toString());
    }

    private static URL makeUrlFromString(String builder){
        URL url = null;
        try{
            url = new URL(builder.toString());
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    private static String getMessageFromStream(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        scanner.useDelimiter("\\A");

        boolean hasInput = scanner.hasNext();
        if (hasInput) {
            return scanner.next();
        } else {
            return null;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(5 * 1000);
        urlConnection.setReadTimeout(7 * 1000);
            try {
                InputStream in = urlConnection.getInputStream();
                return getMessageFromStream(in);
            } catch (IOException e) {
                InputStream err = urlConnection.getErrorStream();
                String errResponse = getMessageFromStream(err);
                throw new IOException(errResponse);
            } finally {
                urlConnection.disconnect();
            }
    }

    public static boolean isNetworkAvailable(Context context) {
        // Using ConnectivityManager to check for Network Connection
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    } //This function modified from http://www.androidbegin.com/tutorial/android-check-internet-connection/

    public static String getFullPosterPath(String posterPath){
        return BASE_MOVIE_POSTER_URL + POSTER_SIZE + posterPath;
    }
    public static String getFullYouTubePath(String trailerPath){
        return YOUTUBE_BASE_URL + trailerPath;
    }
}
