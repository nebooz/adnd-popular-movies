package com.abnd.mdiaz.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.abnd.mdiaz.popularmovies.fragments.MovieListFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final String MOVIE_LIST_FRAGMENT_TAG = "movieFrag";
    private MovieListFragment movieListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkConnectivity()) {

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

        } else {

            TextView noInternet = (TextView) findViewById(R.id.txt_no_internet);
            noInternet.setVisibility(View.VISIBLE);

        }

    }

    private boolean checkConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity has been resumed.");
    }

}