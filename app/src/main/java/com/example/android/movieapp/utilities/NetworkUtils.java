package com.example.android.movieapp.utilities;

import android.net.Uri;

import com.example.android.movieapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * @author Patrice Metcalf-Putnam
 */

public class NetworkUtils {
    private static final String MY_API_KEY = "YOUR_API_KEY_HERE";

    private static final String BASE_MOVIE_SEARCH_URL = "api.themoviedb.org";
    private static final String AUTH_TYPE = "3";
    private static final String MEDIA_TYPE_MOVIE = "movie";
    private static final String API_BASE = "api_key";
    private static final String LANGUAGE = "language";
    private static final String ENGLISH_DEFAULT = "en-US";
    private static final String PAGE = "page";

    private static final String MOST_POPULAR_MOVIES_SEARCH = "popular";
    private static final String TOP_RATED_MOVIES_SEARCH = "top_rated";

    private static final String BASE_MOVIE_POSTER_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w185/";

    public static String getMostPopularMoviesSearch(){
        return MOST_POPULAR_MOVIES_SEARCH;
    }
    public static String getTopRatedMoviesSearch() { return TOP_RATED_MOVIES_SEARCH; }

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
        URL url = null;
        try{
            url = new URL(builder.toString());
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String getFullPosterPath(String posterPath){
        return BASE_MOVIE_POSTER_URL + POSTER_SIZE + posterPath;
    }
}
