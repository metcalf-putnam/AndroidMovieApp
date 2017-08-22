package com.example.android.movieapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Tegan on 8/13/2017.
 */

public class FavoritesContract {
    public static final String AUTHORITY = "com.example.android.AndroidMovieApp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoritesEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_TITLE = "title";
    }
}
