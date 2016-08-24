package com.abnd.mdiaz.popularmovies;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by neboo on 23-Aug-16.
 */
public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private String mUrl;

    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {

        URL u = null;
        try {

            u = new URL(mUrl);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return QueryUtils.fetchMovieData(u, getContext());
    }
}
