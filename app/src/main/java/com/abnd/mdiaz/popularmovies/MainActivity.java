package com.abnd.mdiaz.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private MovieListFragment movieListFragment;

    private static final String TAG = MainActivity.class.getSimpleName();

    private final String MOVIE_LIST_FRAGMENT_TAG = "movieFrag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) { // saved instance state, fragment may exist
            // look up the instance that already exists by tag
            movieListFragment = (MovieListFragment)
                    getSupportFragmentManager().findFragmentByTag(MOVIE_LIST_FRAGMENT_TAG);
        } else if (movieListFragment == null) {
            // only create fragment if they haven't been instantiated already
            movieListFragment = new MovieListFragment();
        }

        if (!movieListFragment.isInLayout()) {
            Log.d(TAG, "MovieListFragment is not in layout.");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.movie_list_fragment_container, movieListFragment, MOVIE_LIST_FRAGMENT_TAG)
                    .commit();
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "MainActivity has been resumed.");
    }

}