package com.example.android.movieapp.utilities;
import com.example.android.movieapp.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Patrice Metcalf-Putnam
 */

public final class ParseMovieJsonUtils {

    public static ArrayList<Movie> getSimpleMovieStringsFromJson(String movieJsonString)
        throws JSONException{
        ArrayList<Movie> movieArray;
        movieArray = new ArrayList<>();

        JSONObject completeJsonResults = new JSONObject(movieJsonString);
        JSONArray movieList = completeJsonResults.getJSONArray("results");

        int length = movieList.length();

        for(int i = 0; i < length; i++){
            try {
                JSONObject m = movieList.getJSONObject(i);
                String id = m.getString("id");
                String vote_average = m.getString("vote_average");
                String title = m.getString("title");
                String popularity = m.getString("popularity");
                String poster_path = m.getString("poster_path");
                String original_language = m.getString("original_language");
                String original_title = m.getString("original_title");

                String backdrop_path = m.getString("backdrop_path");
                String adult = m.getString("adult");
                String overview = m.getString("overview");
                String release_date = m.getString("release_date");

                HashMap<String, String> movie = new HashMap<>();
                movie.put("id", id);
                movie.put("title", title);
                movie.put("vote_average", vote_average);
                movie.put("poster_path", poster_path);
                movie.put("popularity", popularity);
                movie.put("original_language", original_language);
                movie.put("original_title", original_title);
                movie.put("backdrop_path", backdrop_path);
                movie.put("adult", adult);
                movie.put("overview", overview);
                movie.put("release_date", release_date);

                movieArray.add(new Movie(movie));
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
        return movieArray;

    }
}
