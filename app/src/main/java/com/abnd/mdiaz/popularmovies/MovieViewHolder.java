package com.abnd.mdiaz.popularmovies;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by neboo on 23-Aug-16.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {

    private List<Movie> mMovieList;
    protected ImageView movieThumbnail;
    protected TextView movieName;

    public MovieViewHolder(View itemView, List<Movie> movieList) {
        super(itemView);
        mMovieList = movieList;
        movieName = (TextView) itemView.findViewById(R.id.card_title);
        movieThumbnail = (ImageView) itemView.findViewById(R.id.card_thumbnail);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                Intent intent = new Intent(view.getContext(), MovieDetail.class);
                Movie selectedMovie = mMovieList.get(position);

                String movieName = selectedMovie.getName();
                String movieReleaseDate = selectedMovie.getReleaseDate();
                float movieRating = selectedMovie.getRating();
                String moviePosterPath = selectedMovie.getPosterThumbnail();
                String movieBackdropPath = selectedMovie.getBackdropImage();
                String movieSynopsis = selectedMovie.getSynopsis();

                /*
                I'm gonna use the serializable or parcelable objects only when I understand
                how they work...
                */
                intent.putExtra("name", movieName);
                intent.putExtra("release_date", movieReleaseDate);
                intent.putExtra("rating", movieRating);
                intent.putExtra("poster_path", moviePosterPath);
                intent.putExtra("backdrop_path", movieBackdropPath);
                intent.putExtra("synopsis", movieSynopsis);
                intent.putExtra("dark_color", selectedMovie.getDarkColor());
                intent.putExtra("light_color", selectedMovie.getLightColor());

                view.getContext().startActivity(intent);
            }
        });
    }
}
