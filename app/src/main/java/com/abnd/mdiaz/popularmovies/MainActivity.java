package com.abnd.mdiaz.popularmovies;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mProgressBar;
    private RelativeLayout mProgressContainer;

    private boolean popSort;

    private int gridColumns;
    private GridSpacing itemDecoration;
    private static final int LANDSCAPE_GRID_COLUMNS = 5;
    private static final int PORTRAIT_GRID_COLUMNS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //No landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        popSort = true;
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressContainer = (RelativeLayout) findViewById(R.id.progress_container);

        mAdapter = new MovieAdapter(this, new ArrayList<Movie>());
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);

        //Check the screen orientation to alter the number of columns in the GridLayout
        checkOrientation();

        mLayoutManager = new GridLayoutManager(this, gridColumns);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        /*
        I searched online for a way to adjust the column spacing for the grid view so it is
        perfectly centered... this is doing a good job, but it is not exact.
        */
        mRecyclerView.addItemDecoration(itemDecoration);

        getSupportLoaderManager().initLoader(0, null, this);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_menu, menu);

        MenuItem sortMenuItem = menu.findItem(R.id.sort_option);
        if (popSort) {
            sortMenuItem.setIcon(R.drawable.top);
        } else {
            sortMenuItem.setIcon(R.drawable.pop);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort_option) {
            changeSort();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeSort() {
        invalidateOptionsMenu();
        if (popSort) {
            popSort = false;
            //Not a 100% sure if these RestartLoaders are the best option for this.
            getSupportLoaderManager().restartLoader(0, null, this);
        } else {
            popSort = true;
            getSupportLoaderManager().restartLoader(0, null, this);
        }

    }

    private void checkOrientation() {
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
                .getDefaultDisplay();

        int orientation = display.getRotation();

        if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
            gridColumns = LANDSCAPE_GRID_COLUMNS;
            itemDecoration = new GridSpacing(this, R.dimen.item_offset_landscape);
        } else {
            gridColumns = PORTRAIT_GRID_COLUMNS;
            itemDecoration = new GridSpacing(this, R.dimen.item_offset_portrait);
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {

        //Hide the RecyclerView and show the ProgressBar till finished...
        mRecyclerView.setVisibility(View.GONE);
        mProgressContainer.setVisibility(View.VISIBLE);

        if (popSort) {
            return new MovieLoader(this, QueryUtils.getPopularMoviesUrl());
        } else {
            return new MovieLoader(this, QueryUtils.getTopMoviesUrl());
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        //Loader is done and we revert the display process.
        mAdapter.clearData();
        mAdapter.setMovieList(data);

        mProgressContainer.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mAdapter.setMovieList(new ArrayList<Movie>());
    }

}
