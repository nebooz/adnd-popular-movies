package com.abnd.mdiaz.popularmovies;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //private String mListType;
    //private MovieListFragment movieFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Items were being duplicated on rotation.
        if (savedInstanceState == null) {

            //Getting the fragment in place
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            MovieListFragment fragment = new MovieListFragment();
            fragmentTransaction.add(R.id.movie_list_fragment_container, fragment, "movieFrag");
            fragmentTransaction.commit();

            //Without this line, trying to find the fragment returned null.
            //fragmentManager.executePendingTransactions();

            //Get the frag.
            //movieFrag = (MovieListFragment) fragmentManager.findFragmentByTag("movieFrag");

            //No landscape mode
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }

    }

}