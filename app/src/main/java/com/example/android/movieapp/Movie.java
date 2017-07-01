package com.example.android.movieapp;
import android.os.Parcel;
import android.os.Parcelable;
import com.example.android.movieapp.utilities.NetworkUtils;
import java.util.HashMap;

/**
 * @author Patrice Metcalf-Putnam
 *
 */

public class Movie implements Parcelable {

    private String mId;
    private String mTitle;
    private String mVoteAverage;
    private String mPosterPath;
    private String mPopularity;
    private String mOriginalLanguage;
    private String mOriginalTitle;
    private String mBackdropPath;
    private String mAdult;
    private String mOverview;
    private String mReleaseDate;

    public Movie(HashMap<String, String> movieData){
        if(movieData.containsKey("id")){
            mId = movieData.get("id");
        }


        if(movieData.containsKey("title")){
            mTitle = movieData.get("title");
        }

        if(movieData.containsKey("vote_average")){
            mVoteAverage = movieData.get("vote_average");
        }

        if(movieData.containsKey("poster_path")){
            mPosterPath = movieData.get("poster_path");
        }

        if(movieData.containsKey("popularity")){
            mPopularity = movieData.get("popularity");
        }

        if(movieData.containsKey("original_language")){
            mOriginalLanguage = movieData.get("original_language");
        }

        if(movieData.containsKey("original_title")){
            mOriginalTitle = movieData.get("original_title");
        }

        if(movieData.containsKey("backdrop_path")){
            mBackdropPath = movieData.get("backdrop_path");
        }

        if(movieData.containsKey("adult")){
            mAdult = movieData.get("adult");
        }

        if(movieData.containsKey("overview")){
            mOverview = movieData.get("overview");
        }

        if(movieData.containsKey("release_date")){
            mReleaseDate = movieData.get("release_date");
        }

    }

    private Movie(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mVoteAverage = in.readString();
        mPosterPath = in.readString();
        mPopularity = in.readString();
        mOriginalLanguage = in.readString();
        mOriginalTitle = in.readString();
        mBackdropPath = in.readString();
        mAdult = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeString(mVoteAverage);
        dest.writeString(mPosterPath);
        dest.writeString(mPopularity);
        dest.writeString(mOriginalLanguage);
        dest.writeString(mOriginalTitle);
        dest.writeString(mBackdropPath);
        dest.writeString(mAdult);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
    }

    public String getId(){
        return mId;
    }
    public String getTitle(){
        return mTitle;
    }
    public String getVoteAverage(){
        return mVoteAverage;
    }
    public String getPosterPath(){
        return NetworkUtils.getFullPosterPath(mPosterPath); //adds base url
    }
    public String getPopularity(){
        return mPopularity;
    }
    public String getOriginalLanguage(){
        return mOriginalLanguage;
    }
    public String getOriginalTitle(){
        return mOriginalTitle;
    }
    public String getBackdropPath(){
        return mBackdropPath;
    }
    public String getAdult(){
        return mAdult;
    }
    public String getOverview(){
        return mOverview;
    }
    public String getReleaseDate(){
        return mReleaseDate;
    }
    public String toString (){

        return mTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}