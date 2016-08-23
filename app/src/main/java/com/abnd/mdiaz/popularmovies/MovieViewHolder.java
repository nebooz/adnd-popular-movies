package com.abnd.mdiaz.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by neboo on 23-Aug-16.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {
    protected ImageView movieThumbnail;
    protected TextView movieName;

    public MovieViewHolder(View itemView) {
        super(itemView);
        movieName = (TextView) itemView.findViewById(R.id.card_title);
        movieThumbnail = (ImageView) itemView.findViewById(R.id.card_thumbnail);
    }
}
