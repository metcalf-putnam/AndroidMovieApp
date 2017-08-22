package com.example.android.movieapp.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fluffster on 8/6/2017.
 */

public class ParseMovieReviewsUtil {
    private static final String TRAILER_EXTRA = "TRAILER";
    private static final String NAME_EXTRA = "NAME";
    public static JSONArray getJsonArray(String input){
        JSONArray jsonReviews = null;
        try {
            JSONObject allJson = new JSONObject(input);
            jsonReviews = allJson.getJSONArray("results");

        }catch(JSONException e){
            e.printStackTrace();
        }
       return jsonReviews;
    }

    public static String getPrettyReviews(JSONArray jsonReviews){
        String prettyReviews = null;
        int length = jsonReviews.length();
        StringBuilder builder = new StringBuilder();
        try {
            for (int i = 0; i < length; i++) {
                JSONObject reviewJson = jsonReviews.getJSONObject(i);
                String reviewString = reviewJson.getString("content");
                builder.append("\"");
                builder.append(reviewString);
                builder.append("\"");
                builder.append("\n\n");
            }
            prettyReviews = builder.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }

        return prettyReviews;
    }

    public static ArrayList<HashMap<String, String>> getTrailerDetails(String rawJson){
        JSONArray array = getJsonArray(rawJson);
        int length = array.length();
        ArrayList<HashMap<String, String>> trailers = new ArrayList<>();

        try {

            for(int i = 0; i < length ; i++){
                HashMap<String, String> hash = new HashMap<>();
                JSONObject video = array.getJSONObject(i);
                String type = video.getString("type");
                String site = video.getString("site");
                if(type.contentEquals("Trailer") && site.contentEquals("YouTube")){
                    String name = video.getString("name");
                    String key = video.getString("key");
                    String url = NetworkUtils.getFullYouTubePath(key);
                    hash.put(NAME_EXTRA, name);
                    hash.put(TRAILER_EXTRA, url);
                    trailers.add(hash);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return trailers;
    }


}
