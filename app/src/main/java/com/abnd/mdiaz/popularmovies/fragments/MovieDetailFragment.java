package com.abnd.mdiaz.popularmovies.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.abnd.mdiaz.popularmovies.R;
import com.abnd.mdiaz.popularmovies.model.Movie;
import com.abnd.mdiaz.popularmovies.model.Trailer;
import com.abnd.mdiaz.popularmovies.model.TrailersResponse;
import com.abnd.mdiaz.popularmovies.rest.ApiClient;
import com.abnd.mdiaz.popularmovies.rest.ApiInterface;
import com.abnd.mdiaz.popularmovies.utils.SensitiveInfo;
import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailFragment extends Fragment {

    private static final String TAG = MovieDetailFragment.class.getSimpleName();
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String SMALL_IMAGE_SIZE = "w92";
    private static final String MEDIUM_IMAGE_SIZE = "w185";
    private static final String LARGE_IMAGE_SIZE = "w500";
    private ImageView backdropImageView;
    private ImageView posterImageView;
    private TextView movieTitleTextView;
    private TextView movieRatingTextView;
    private TextView movieReleaseDateTextView;
    private TextView movieSynopsisTextView;
    private TextView trailerHeader;
    private ScrollView mainLayout;
    private String mMovieName;
    private int mMovieId;
    private String mMoviePosterPath;
    private String mMovieBackdropPath;
    private double mMovieRating;
    private String mMovieSynopsis;
    private String mMovieReleaseDate;

    private int mDarkColor;
    private int mLightColor;

    private LinearLayout mTrailerContainer;

    public MovieDetailFragment() {
        // Required empty public constructor
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Movie movie = getArguments().getParcelable("selectedMovie");

        mMovieName = movie.getTitle();
        mMovieId = movie.getId();
        mMoviePosterPath = movie.getPosterPath();
        mMovieBackdropPath = movie.getBackdropPath();
        mMovieRating = movie.getVoteAverage();
        mMovieSynopsis = movie.getOverview();
        String preFixedReleaseDate = movie.getReleaseDate();

        //Proper date
        mMovieReleaseDate = this.getString(R.string.release_date) + dateFormat(preFixedReleaseDate);

    }

    private void getTrailerList(int movieId) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<TrailersResponse> call = apiService.getMovieTrailers(movieId, SensitiveInfo.getMoviesApiKey());

        call.enqueue(new Callback<TrailersResponse>() {
            @Override
            public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                List<Trailer> trailerList = response.body().getTrailers();

                for (final Trailer currentTrailer : trailerList) {

                    TextView currentTrailerView = (TextView) LayoutInflater.from(getContext())
                            .inflate(R.layout.movie_detail_trailer_view, mTrailerContainer, false);

                    currentTrailerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + currentTrailer.getKey()));
                                startActivity(intent);
                            } catch (ActivityNotFoundException ex) {
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://www.youtube.com/watch?v=" + currentTrailer.getKey()));
                                startActivity(intent);
                            }
                        }
                    });

                    //currentTrailerView.setClickable(true);
                    //currentTrailerView.setBackgroundResource(backgroundResource);
                    currentTrailerView.setText(currentTrailer.getName());
                    mTrailerContainer.addView(currentTrailerView);
                }

                Log.d(TAG, "Number of trailers received: " + trailerList.size());
            }

            @Override
            public void onFailure(Call<TrailersResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        //Assign all views...
        mainLayout = (ScrollView) view.findViewById(R.id.main_layout);
        backdropImageView = (ImageView) view.findViewById(R.id.img_backdrop);
        posterImageView = (ImageView) view.findViewById(R.id.img_poster);
        movieTitleTextView = (TextView) view.findViewById(R.id.txt_title);
        movieRatingTextView = (TextView) view.findViewById(R.id.txt_rating);
        movieReleaseDateTextView = (TextView) view.findViewById(R.id.txt_release_date);
        movieSynopsisTextView = (TextView) view.findViewById(R.id.txt_synopsis);
        trailerHeader = (TextView) view.findViewById(R.id.txt_trailer_header);

        //Assign values to views...
        movieTitleTextView.setText(mMovieName);
        movieRatingTextView.setText(String.format("%.1f", mMovieRating));
        movieReleaseDateTextView.setText(mMovieReleaseDate);
        movieSynopsisTextView.setText(mMovieSynopsis);

        String fullPosterPath = IMAGE_BASE_URL + MEDIUM_IMAGE_SIZE + mMoviePosterPath;
        String fullBackdropPath = IMAGE_BASE_URL + LARGE_IMAGE_SIZE + mMovieBackdropPath;

        Picasso.with(getContext()).load(fullBackdropPath).into(backdropImageView);

        Picasso.with(getContext()).load(fullPosterPath).into(
                posterImageView,
                PicassoPalette.with(fullPosterPath, posterImageView)
                        .intoCallBack(new PicassoPalette.CallBack() {
                            @Override
                            public void onPaletteLoaded(Palette palette) {

                                /*
                                Apparently, there is a difference between just getting the Resource
                                straight up or using the getColor method...
                                */
                                int defaultDarkColor = ContextCompat.getColor(getContext(),
                                        R.color.defaultDarkColor);
                                int defaultLightColor = ContextCompat.getColor(getContext(),
                                        R.color.defaultLightColor);

                                mDarkColor = palette.getDarkMutedColor(ContextCompat.
                                        getColor(getContext(), R.color.defaultDarkColor));

                                if (mDarkColor == defaultDarkColor) {
                                    mDarkColor = palette.getDarkVibrantColor(ContextCompat.
                                            getColor(getContext(), R.color.defaultDarkColor));
                                }

                                mLightColor = palette.getLightMutedColor(ContextCompat.
                                        getColor(getContext(), R.color.defaultLightColor));

                                if (mLightColor == defaultLightColor) {
                                    mLightColor = palette.getLightVibrantColor(ContextCompat.
                                            getColor(getContext(), R.color.defaultDarkColor));
                                }

                                movieRatingTextView.setBackgroundColor(mDarkColor);
                                movieRatingTextView.setShadowLayer(10, 0, 0, Color.BLACK);

                                movieTitleTextView.setBackgroundColor(mDarkColor);
                                movieTitleTextView.setShadowLayer(10, 0, 0, Color.BLACK);

                                movieReleaseDateTextView.setBackgroundColor(mLightColor);

                                int darkAlphaColor = ColorUtils.setAlphaComponent(mDarkColor, 128);
                                movieSynopsisTextView.setBackgroundColor(darkAlphaColor);

                                GradientDrawable gd = new GradientDrawable(
                                        GradientDrawable.Orientation.TOP_BOTTOM,
                                        new int[]{Color.WHITE, mLightColor});

                                mainLayout.setBackground(gd);

                                trailerHeader.setBackgroundColor(mDarkColor);

                            }
                        })
        );

        mTrailerContainer = (LinearLayout) view.findViewById(R.id.trailer_list_container);
        getTrailerList(mMovieId);

        return view;
    }

}
