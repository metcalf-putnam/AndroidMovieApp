package com.example.android.movieapp;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MovieTest {
    private static final String TEST_TITLE = "The Cherry Cola Capers";
    private static final String TEST_ID = "94666385992";
    private static final String  TEST_VOTE_AVERAGE = "8.9";
    private static final String  TEST_POSTER_PATH = "/f8Ng1Sgb3VLiSwAvrfKeQPzvlfr.jpg";
    private static final String TEST_FULL_POSTER_PATH = "http://image.tmdb.org/t/p/w185//f8Ng1Sgb3VLiSwAvrfKeQPzvlfr.jpg";
    private static final String  TEST_POPULARITY = "172";
    private static final String  TEST_ORIGINAL_LANGUAGE = "en";
    private static final String  TEST_ORIGINAL_TITLE = "The Worst Movie Ever";
    private static final String TEST_BACKDROP_PATH = "/Ytv7P13rbwQ3mLpCAY8lBTqI5s.jpg";
    private static final String  TEST_ADULT = "false";
    private static final String  TEST_OVERVIEW = "Five kindergartners kill everyone";
    private static final String  TEST_RELEASE_DATE = "2017-06-21";

    @Test
    public void movieCalls() throws Exception {

        assertEquals(TEST_TITLE, TEST_TITLE);

        HashMap<String, String> movieData = new HashMap<>();

        movieData.put("title", TEST_TITLE);
        movieData.put("id", TEST_ID);
        movieData.put("vote_average", TEST_VOTE_AVERAGE);
        movieData.put("poster_path", TEST_POSTER_PATH);
        movieData.put("popularity", TEST_POPULARITY);
        movieData.put("original_language", TEST_ORIGINAL_LANGUAGE);
        movieData.put("original_title", TEST_ORIGINAL_TITLE);
        movieData.put("backdrop_path", TEST_BACKDROP_PATH);
        movieData.put("adult", TEST_ADULT);
        movieData.put("overview", TEST_OVERVIEW);
        movieData.put("release_date", TEST_RELEASE_DATE);

        Movie movie = new Movie(movieData);

        assertEquals(TEST_TITLE, movie.getTitle());
        assertEquals(TEST_ID, movie.getId());
        assertEquals(TEST_VOTE_AVERAGE, movie.getVoteAverage());
        assertEquals(TEST_FULL_POSTER_PATH, movie.getPosterPath());
        assertEquals(TEST_POPULARITY, movie.getPopularity());
        assertEquals(TEST_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        assertEquals(TEST_ORIGINAL_TITLE, movie.getOriginalTitle());
        assertEquals(TEST_BACKDROP_PATH, movie.getBackdropPath());
        assertEquals(TEST_ADULT, movie.getAdult());
        assertEquals(TEST_OVERVIEW, movie.getOverview());
        assertEquals(TEST_RELEASE_DATE, movie.getReleaseDate());

    }
}