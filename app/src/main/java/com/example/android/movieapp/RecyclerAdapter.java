package com.example.android.movieapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * @author Patrice Metcalf-Putnam
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.PosterHolder> {
    private ArrayList<Movie> mMoviesArray;
    final private MoviePosterClickListener mOnMoviePosterClick;
    public interface MoviePosterClickListener{
        void onMoviePosterClick(Movie movie);
    }

    public RecyclerAdapter (MoviePosterClickListener listener){
        mOnMoviePosterClick = listener;
    }

    @Override
    public PosterHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new PosterHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterHolder holder, int position) {
        holder.bind(mMoviesArray.get(position));
    }

    @Override
    public int getItemCount() {
        if (null == mMoviesArray) return 0;
        return mMoviesArray.size();
    }

    class PosterHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        final ImageView movieItemImageView;

        public PosterHolder(View movieItem){
            super(movieItem);
            movieItemImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }
        public void bind(Movie movie){
            String url = movie.getPosterPath();
            Picasso.with(movieItemImageView.getContext()).load(url).into(movieItemImageView);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Movie movie = mMoviesArray.get(clickedPosition);
            mOnMoviePosterClick.onMoviePosterClick(movie);
        }

    }
    public int setMovieData(ArrayList<Movie> movies){
        if(mMoviesArray == null){
            mMoviesArray = movies;
        }
        else {
            mMoviesArray.addAll(movies);
        }
        notifyDataSetChanged();
        return mMoviesArray.size();
    }

    public void clearMovieData(){
        mMoviesArray.clear();
    }

}
