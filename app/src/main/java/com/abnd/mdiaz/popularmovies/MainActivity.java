package com.abnd.mdiaz.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.abnd.mdiaz.popularmovies.model.Movie;

public class MainActivity extends AppCompatActivity implements MovieViewHolder.OnMovieSelectedInteractionListener, FragmentManager.OnBackStackChangedListener {

    private MovieListFragment movieListFragment;
    private MovieDetailFragment movieDetailFragment;
    private Fragment mContent;

    private final String MOVIE_LIST_FRAGMENT_TAG = "movieFrag";
    private final String MOVIE_DETAIL_FRAGMENT_TAG = "movieDetail";
    private String TEMP_TAG;


    //private String mListType;
    //private MovieListFragment movieFrag;

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Listen for changes in the back stack
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        //Handle when activity is recreated like on orientation Change
        shouldDisplayHomeUp();

        if (savedInstanceState != null) { // saved instance state, fragment may exist
            // look up the instance that already exists by tag
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
            TEMP_TAG = mContent.getTag();

            //movieListFragment = (MovieListFragment)
            //getSupportFragmentManager().findFragmentByTag(MOVIE_LIST_FRAGMENT_TAG);
        } else if (mContent == null) {

            mContent = new MovieListFragment();
            TEMP_TAG = MOVIE_DETAIL_FRAGMENT_TAG;

        }

        if (!mContent.isInLayout()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.movie_list_fragment_container, mContent, TEMP_TAG)
                    .commit();
        }

    }

    @Override
    public void onMovieSelectedInteraction(Movie movie) {

        movieDetailFragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("selectedMovie", movie);
        movieDetailFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.movie_list_fragment_container, movieDetailFragment, MOVIE_DETAIL_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();

        mContent = movieDetailFragment;

    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp() {
        //Enable Up button only if there are entries in the back stack
        boolean canGoBack = getSupportFragmentManager().getBackStackEntryCount() > 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }
}