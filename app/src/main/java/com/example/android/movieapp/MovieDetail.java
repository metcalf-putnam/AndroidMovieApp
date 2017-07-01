package com.example.android.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

/**
 * @author Patrice Metcalf-Putnam
 */

public class MovieDetail extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moviedetail);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movie")) {
                Movie m = intentThatStartedThisActivity.getParcelableExtra("movie");
                ImageView poster = (ImageView) findViewById(R.id.iv_movieDetailPoster);
                TextView title = (TextView) findViewById(R.id.tv_originalTitle);
                title.setText(m.getOriginalTitle());

                String averageUserRating = "Average User Rating: " + m.getVoteAverage();
                TextView userRating = (TextView) findViewById(R.id.tv_userRating);
                userRating.setText(averageUserRating);

                String date = "Release Date: " + m.getReleaseDate();
                TextView releaseDate = (TextView) findViewById(R.id.tv_releaseDate);
                releaseDate.setText(date);

                TextView overview = (TextView) findViewById(R.id.tv_overview);
                overview.setText(m.getOverview());

                Picasso.with(this).load(m.getPosterPath()).into(poster);
            }
        }

    }
}
