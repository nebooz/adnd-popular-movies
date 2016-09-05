package com.abnd.mdiaz.popularmovies;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.abnd.mdiaz.popularmovies.fragments.MovieDetailFragment;
import com.abnd.mdiaz.popularmovies.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    private final String MOVIE_DETAIL_FRAGMENT_TAG = "movieDetail";
    private MovieDetailFragment movieDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Movie selectedMovie = getIntent().getParcelableExtra("selected_movie");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(selectedMovie.getTitle());

        if (savedInstanceState == null) {

            movieDetailFragment = MovieDetailFragment.newInstance(selectedMovie);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.movie_detail_fragment_container, movieDetailFragment, MOVIE_DETAIL_FRAGMENT_TAG)
                    .commit();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
