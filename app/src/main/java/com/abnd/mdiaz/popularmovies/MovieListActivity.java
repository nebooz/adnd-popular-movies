package com.abnd.mdiaz.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MovieListActivity extends AppCompatActivity {

    private static final String TAG = MovieListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        if (!checkConnectivity()) {

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