package com.abnd.mdiaz.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MovieListActivity extends AppCompatActivity {

    private static final String TAG = MovieListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        RelativeLayout activityBaseLayout = (RelativeLayout) findViewById(R.id.movie_list_fragment_container);

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.list_mini_dark);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bmp);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        activityBaseLayout.setBackground(bitmapDrawable);

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