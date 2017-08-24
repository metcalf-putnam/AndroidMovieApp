package com.example.android.movieapp.utilities;

import android.database.Cursor;

import com.example.android.movieapp.Movie;
import com.example.android.movieapp.data.FavoritesContract;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Patrice Metcalf-Putnam on 8/23/2017.
 */

public class ParseMovieCursorUtils {

    public static ArrayList<Movie> generateMovieArray(Cursor cursor){
        final int COLUMN_MOVIE_ID = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID);
        final int COLUMN_VOTE_AVERAGE = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_VOTE_AVERAGE);
        final int COLUMN_TITLE = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_TITLE);
        final int COLUMN_POSTER_PATH = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH);
        final int COLUMN_OVERVIEW = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW);
        final int COLUMN_RELEASE_DATE = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE);
        final int COLUMN_ORIGINAL_TITLE = cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_ORIGINAL_TITLE);

        ArrayList<Movie> movieArray;
        movieArray = new ArrayList<>();
        cursor.moveToFirst();
        do{

            int id = cursor.getInt(COLUMN_MOVIE_ID);
            String idString = Integer.toString(id);
            String vote_average = cursor.getString(COLUMN_VOTE_AVERAGE);
            String title = cursor.getString(COLUMN_TITLE);
            String poster_path = cursor.getString(COLUMN_POSTER_PATH);
            String original_title = cursor.getString(COLUMN_ORIGINAL_TITLE);
            String overview = cursor.getString(COLUMN_OVERVIEW);
            String release_date = cursor.getString(COLUMN_RELEASE_DATE);

            HashMap<String, String> movieHash = new HashMap<>();

            movieHash.put("id", idString);
            movieHash.put("title", title);
            movieHash.put("vote_average", vote_average);
            movieHash.put("poster_path", poster_path);
            movieHash.put("original_title", original_title);
            movieHash.put("overview", overview);
            movieHash.put("release_date", release_date);

            movieArray.add(new Movie(movieHash));

        }while(cursor.moveToNext()); //continue until no more rows

        return movieArray;
    }

}
