package com.abnd.mdiaz.popularmovies;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abnd.mdiaz.popularmovies.model.Movie;
import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieDetailActivity extends AppCompatActivity {

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //No landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Get the Parcelable object
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

        //Proper date
        String formattedDate = this.getString(R.string.release_date) + dateFormat(movieReleaseDate);

        //Assign values to views...
        movieTitleTextView.setText(movieName);
        movieRatingTextView.setText(String.format("%.1f", movieRating));
        movieReleaseDateTextView.setText(formattedDate);
        movieSynopsisTextView.setText(movieSynopsis);

        String fullPosterPath = IMAGE_BASE_URL + MEDIUM_IMAGE_SIZE + moviePosterPath;
        String fullBackdropPath = IMAGE_BASE_URL + LARGE_IMAGE_SIZE + movieBackdropPath;

        Picasso.with(this).load(fullBackdropPath).into(backdropImageView);

        Picasso.with(this).load(fullPosterPath).into(
                posterImageView,
                PicassoPalette.with(fullPosterPath, posterImageView)
                        .use(PicassoPalette.Profile.MUTED_DARK)
                        .intoBackground(movieSynopsisTextView, PicassoPalette.Swatch.RGB)
                        .intoBackground(movieRatingTextView, PicassoPalette.Swatch.RGB)
                        .intoBackground(titleBackground, PicassoPalette.Swatch.RGB)

                        .use(PicassoPalette.Profile.MUTED_LIGHT)
                        .intoBackground(releaseBackground, PicassoPalette.Swatch.RGB)

                        .intoCallBack(new PicassoPalette.CallBack() {
                            @Override
                            public void onPaletteLoaded(Palette palette) {
                                int darkColor = palette.getDarkMutedColor(ContextCompat.getColor(MovieDetailActivity.this,
                                        R.color.defaultDarkColor));

                                GradientDrawable gd = new GradientDrawable(
                                        GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.WHITE, darkColor});

                                mainLayout.setBackground(gd);
                            }
                        })
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
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
}
