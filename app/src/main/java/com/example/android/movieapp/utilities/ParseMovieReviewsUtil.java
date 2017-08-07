package com.example.android.movieapp.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fluffster on 8/6/2017.
 */

public class ParseMovieReviewsUtil {
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


}
