package com.abnd.mdiaz.popularmovies;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>{

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private int gridColumns;
    private static final int LANDSCAPE_GRID_COLUMNS = 5;
    private static final int PORTRAIT_GRID_COLUMNS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check the screen orientation to alter the number of columns in the GridLayout
        checkOrientation();

        mAdapter = new MovieAdapter(this, new ArrayList<Movie>());
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);

        mLayoutManager = new GridLayoutManager(this, gridColumns);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        /*
        I searched online for a way to adjust the column spacing for the grid view so it is
        perfectly centered... this is doing a good job, but it is not exact.
        */
        GridSpacing itemDecoration = new GridSpacing(this, R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);

        getSupportLoaderManager().initLoader(0, null, this);

    }

    private void checkOrientation() {
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
                .getDefaultDisplay();

        int orientation = display.getRotation();

        if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
            gridColumns = LANDSCAPE_GRID_COLUMNS;
        } else {
            gridColumns = PORTRAIT_GRID_COLUMNS;
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(this, QueryUtils.getQueryUrl());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mAdapter.setMovieList(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mAdapter.setMovieList(new ArrayList<Movie>());
    }
}
