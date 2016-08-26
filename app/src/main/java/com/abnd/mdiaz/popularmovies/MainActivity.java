package com.abnd.mdiaz.popularmovies;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.abnd.mdiaz.popularmovies.model.MoviesResponse;
import com.abnd.mdiaz.popularmovies.model.Movie;
import com.abnd.mdiaz.popularmovies.rest.ApiClient;
import com.abnd.mdiaz.popularmovies.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mProgressBar;
    private RelativeLayout mProgressContainer;

    private boolean popSort;
    private int mDarkColor;
    private int mLightColor;

    private int gridColumns;
    private GridSpacing itemDecoration;
    private static final int LANDSCAPE_GRID_COLUMNS = 5;
    private static final int PORTRAIT_GRID_COLUMNS = 3;

    private static final String TAG = MainActivity.class.getSimpleName();

    private List<Movie> mMovieList = new ArrayList<>();

    private Bitmap colorGenBitmap;

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String SMALL_IMAGE_SIZE = "w92";
    private static final String MEDIUM_IMAGE_SIZE = "w185";
    private static final String LARGE_IMAGE_SIZE = "w500";

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

        getMovieList(popSort);
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
        } else {
            popSort = true;
        }

        getMovieList(popSort);
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

    private void loadAdapter(List<Movie> baseMovieList) {

        mAdapter.clearData();
        mAdapter.setMovieList(baseMovieList);

        mProgressContainer.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

    }

    private void getMovieList(boolean popSort) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call;

        if (popSort) {
            call = apiService.getPopularMovies(SensitiveInfo.getApiKey());
        } else {
            call = apiService.getTopRatedMovies(SensitiveInfo.getApiKey());
        }

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> movieList = response.body().getResults();
                loadAdapter(movieList);
                Log.d(TAG, "(mMovieList) Number of movies received: " + movieList.size());
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

    }

}
