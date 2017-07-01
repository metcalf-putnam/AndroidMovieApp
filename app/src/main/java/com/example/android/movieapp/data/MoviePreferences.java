package com.example.android.movieapp.data;

/**
 * @author Patrice Metcalf-Putnam
 */

public class MoviePreferences {
    private static final String preferred = com.example.android.movieapp.utilities.NetworkUtils.getMostPopularMoviesSearch();

    public static String getPreferred(){
        return preferred;
    }
}
