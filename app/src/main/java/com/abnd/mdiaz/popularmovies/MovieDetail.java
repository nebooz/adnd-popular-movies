package com.abnd.mdiaz.popularmovies;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    private ImageView backdropImageView;
    private ImageView posterImageView;
    private TextView movieTitleTextView;
    private TextView movieRatingTextView;
    private TextView movieReleaseDateTextView;
    private TextView movieSynopsisTextView;
    private RelativeLayout mainLayout;
    private View titleBackground;
    private View releaseBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        String movieName = getIntent().getStringExtra("name");
        String moviePosterPath = getIntent().getStringExtra("poster_path");
        String movieBackdropPath = getIntent().getStringExtra("backdrop_path");
        float movieRating = getIntent().getFloatExtra("rating", 1f);
        String movieSynopsis = getIntent().getStringExtra("synopsis");
        String movieReleaseDate = getIntent().getStringExtra("release_date");
        int movieDarkColor = getIntent().getIntExtra("dark_color", Color.BLUE);
        int movieLightColor = getIntent().getIntExtra("light_color", Color.CYAN);

        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        titleBackground = findViewById(R.id.title_bg);
        releaseBackground = findViewById(R.id.release_bg);
        backdropImageView = (ImageView) findViewById(R.id.img_backdrop);
        posterImageView = (ImageView) findViewById(R.id.img_poster);
        movieTitleTextView = (TextView) findViewById(R.id.txt_title);
        movieRatingTextView = (TextView) findViewById(R.id.txt_rating);
        movieReleaseDateTextView = (TextView) findViewById(R.id.txt_release_date);
        movieSynopsisTextView = (TextView) findViewById(R.id.txt_synopsis);

        movieTitleTextView.setText(movieName);
        movieRatingTextView.setText(String.format("%.1f", movieRating));
        movieReleaseDateTextView.setText(movieReleaseDate);
        movieSynopsisTextView.setText(movieSynopsis);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, new int[] {Color.WHITE, movieDarkColor});

        mainLayout.setBackground(gd);

        movieSynopsisTextView.setTextColor(movieDarkColor);
        movieRatingTextView.setBackgroundColor(movieDarkColor);
        titleBackground.setBackgroundColor(movieDarkColor);
        releaseBackground.setBackgroundColor(movieLightColor);

        Picasso.with(this).load(moviePosterPath).into(posterImageView);
        Picasso.with(this).load(movieBackdropPath).into(backdropImageView);

    }
}
