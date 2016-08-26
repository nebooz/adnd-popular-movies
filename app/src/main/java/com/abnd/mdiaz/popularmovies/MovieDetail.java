package com.abnd.mdiaz.popularmovies;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abnd.mdiaz.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String SMALL_IMAGE_SIZE = "w92";
    private static final String MEDIUM_IMAGE_SIZE = "w185";
    private static final String LARGE_IMAGE_SIZE = "w500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //No landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Get all the Extras...
        Movie movie = getIntent().getParcelableExtra("selected_movie");

        String movieName = movie.getTitle();
        String moviePosterPath = movie.getPosterPath();
        String movieBackdropPath = movie.getBackdropPath();
        double movieRating = movie.getVoteAverage();
        String movieSynopsis = movie.getOverview();
        String movieReleaseDate = movie.getReleaseDate();

        //Assign all views...
        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        titleBackground = findViewById(R.id.title_bg);
        releaseBackground = findViewById(R.id.release_bg);
        backdropImageView = (ImageView) findViewById(R.id.img_backdrop);
        posterImageView = (ImageView) findViewById(R.id.img_poster);
        movieTitleTextView = (TextView) findViewById(R.id.txt_title);
        movieRatingTextView = (TextView) findViewById(R.id.txt_rating);
        movieReleaseDateTextView = (TextView) findViewById(R.id.txt_release_date);
        movieSynopsisTextView = (TextView) findViewById(R.id.txt_synopsis);

        String formattedDate = this.getString(R.string.release_date) + dateFormat(movieReleaseDate);

        //Assign values to views...
        movieTitleTextView.setText(movieName);
        movieRatingTextView.setText(String.format("%.1f", movieRating));
        movieReleaseDateTextView.setText(formattedDate);
        movieSynopsisTextView.setText(movieSynopsis);

        String colorGenerationPath = IMAGE_BASE_URL + SMALL_IMAGE_SIZE + moviePosterPath;

        Picasso.with(this).load(colorGenerationPath).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                new getLayoutColors().execute(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        String fullPosterPath = IMAGE_BASE_URL + MEDIUM_IMAGE_SIZE + moviePosterPath;
        String fullBackdropPath = IMAGE_BASE_URL + LARGE_IMAGE_SIZE + movieBackdropPath;

        //Picasso magic.
        Picasso.with(this).load(fullPosterPath).into(posterImageView);
        Picasso.with(this).load(fullBackdropPath).into(backdropImageView);

    }

    private static String dateFormat(String releaseDate) {

        String inputString = "yyyy-MM-dd";
        String outputString = "MMMM dd, yyyy";

        SimpleDateFormat parser = new SimpleDateFormat(inputString);
        SimpleDateFormat properForm = new SimpleDateFormat(outputString);

        String formattedDate = null;

        try {
            Date properDate = parser.parse(releaseDate);
            formattedDate = properForm.format(properDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    private class getLayoutColors extends AsyncTask<Bitmap, Void, Palette> {

        protected Palette doInBackground(Bitmap... bitmaps) {
            return new Palette.Builder(bitmaps[0]).generate();
        }

        protected void onPostExecute(Palette palette) {

            int darkColor = palette.getDarkMutedColor(ContextCompat.getColor(MovieDetail.this,
                    R.color.defaultDarkColor));
            int lightColor = palette.getLightMutedColor(ContextCompat.getColor(MovieDetail.this,
                    R.color.defaultLightColor));

            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM, new int[] {Color.WHITE, darkColor});

            mainLayout.setBackground(gd);

            movieSynopsisTextView.setTextColor(darkColor);
            movieRatingTextView.setBackgroundColor(darkColor);
            titleBackground.setBackgroundColor(darkColor);
            releaseBackground.setBackgroundColor(lightColor);

        }
    }
}
