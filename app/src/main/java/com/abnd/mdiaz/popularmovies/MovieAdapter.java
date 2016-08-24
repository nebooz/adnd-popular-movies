package com.abnd.mdiaz.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neboo on 23-Aug-16.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private Context mContext;
    private List<Movie> mMovieList = new ArrayList<>();

    public MovieAdapter(Context context, List<Movie> movieList) {
        mContext = context;
        mMovieList = movieList;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_card, parent, false);

        return new MovieViewHolder(v, mMovieList);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        Movie currentMovie = mMovieList.get(position);

        holder.movieName.setText(currentMovie.getName());
        Picasso.with(mContext).load(currentMovie.getPosterThumbnail()).into(holder.movieThumbnail);

    }

    @Override
    public int getItemCount() {
        return (null != mMovieList ? mMovieList.size() : 0);
    }

    public void setMovieList(List<Movie> movieList) {

        mMovieList.addAll(movieList);
        notifyDataSetChanged();

    }

    public void clearData() {
        mMovieList.clear();
    }
}
