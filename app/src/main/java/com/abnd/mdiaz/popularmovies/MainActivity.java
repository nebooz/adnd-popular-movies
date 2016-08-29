package com.abnd.mdiaz.popularmovies;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MovieListFragment movieListFragment;
    private final String MOVIE_LIST_FRAGMENT_TAG = "movieFrag";


    //private String mListType;
    //private MovieListFragment movieFrag;

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
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.movie_list_fragment_container, movieListFragment, MOVIE_LIST_FRAGMENT_TAG)
                    .commit();
        }


        /*//Getting the fragment in place
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MovieListFragment fragment = new MovieListFragment();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.replace(R.id.movie_list_fragment_container, fragment, "movieFrag");
        fragmentTransaction.commit();*/

        //Without this line, trying to find the fragment returned null.
        //fragmentManager.executePendingTransactions();

        //Get the frag.
        //movieFrag = (MovieListFragment) fragmentManager.findFragmentByTag("movieFrag");

        //No landscape mode
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

}