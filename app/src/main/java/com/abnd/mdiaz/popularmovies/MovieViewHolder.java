package com.abnd.mdiaz.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abnd.mdiaz.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by neboo on 23-Aug-16.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public OnMovieSelectedInteractionListener mListener;


    private List<Movie> mMovieList;
    protected ImageView movieThumbnail;
    protected TextView movieName;

    public MovieViewHolder(View itemView, List<Movie> movieList) {
        super(itemView);
        mMovieList = movieList;
        movieName = (TextView) itemView.findViewById(R.id.card_title);
        movieThumbnail = (ImageView) itemView.findViewById(R.id.card_thumbnail);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        mListener = (OnMovieSelectedInteractionListener) view.getContext();

        int position = getAdapterPosition();
        Movie selectedMovie = mMovieList.get(position);

        if (mListener != null) {
            mListener.onMovieSelectedInteraction(selectedMovie);
        }

        //Intent intent = new Intent(view.getContext(), MovieDetailActivity.class);
        //intent.putExtra("selected_movie", selectedMovie);

        //view.getContext().startActivity(intent);

    }


    public interface OnMovieSelectedInteractionListener {
        // TODO: Update argument type and name
        void onMovieSelectedInteraction(Movie movie);
    }
}
