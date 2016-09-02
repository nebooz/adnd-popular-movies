package com.abnd.mdiaz.popularmovies.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abnd.mdiaz.popularmovies.R;
import com.abnd.mdiaz.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String SMALL_IMAGE_SIZE = "w92";
    private static final String MEDIUM_IMAGE_SIZE = "w185";
    private static final String LARGE_IMAGE_SIZE = "w500";

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

        String fullPosterPath = IMAGE_BASE_URL + MEDIUM_IMAGE_SIZE + currentMovie.getPosterPath();

        holder.movieName.setText(currentMovie.getTitle());
        Picasso.with(mContext).load(fullPosterPath).into(holder.movieThumbnail);

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
