package com.abnd.mdiaz.popularmovies.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abnd.mdiaz.popularmovies.utils.GridSpacing;
import com.abnd.mdiaz.popularmovies.MainActivity;
import com.abnd.mdiaz.popularmovies.views.adapters.MovieAdapter;
import com.abnd.mdiaz.popularmovies.R;
import com.abnd.mdiaz.popularmovies.utils.SensitiveInfo;
import com.abnd.mdiaz.popularmovies.model.Movie;
import com.abnd.mdiaz.popularmovies.model.MoviesResponse;
import com.abnd.mdiaz.popularmovies.rest.ApiClient;
import com.abnd.mdiaz.popularmovies.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.WINDOW_SERVICE;

public class MovieListFragment extends Fragment {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TOP_MOVIES_TAG = "Top";
    private static final String POP_MOVIES_TAG = "Pop";
    private static final String FAV_MOVIES_TAG = "Fav";

    private static final int LANDSCAPE_GRID_COLUMNS = 5;
    private static final int PORTRAIT_GRID_COLUMNS = 3;
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private ProgressBar mProgressBar;
    private String mListType;
    private int gridColumns;
    private GridSpacing itemDecoration;

    private ActionBar mActionBar;

    public MovieListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("ListType", mListType);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mListType = savedInstanceState.getString("ListType", POP_MOVIES_TAG);
            Log.d(TAG, "List Type: " + mListType);
        } else {
            mListType = POP_MOVIES_TAG;
        }

        AppCompatActivity mActivity = (AppCompatActivity) getActivity();
        mActionBar = mActivity.getSupportActionBar();

        setHasOptionsMenu(true);

        mAdapter = new MovieAdapter(getContext(), new ArrayList<Movie>());

        //Check the screen orientation to alter the number of columns in the GridLayout
        checkOrientation();

    }

    @Override
    public void onStart() {
        super.onStart();
        getMovieList(mListType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.movie_list_progress_bar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.main_recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), gridColumns);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        /*
        I searched online for a way to adjust the column spacing for the grid view so it is
        perfectly centered... this is doing a good job, but it is not exact.
        */
        mRecyclerView.addItemDecoration(itemDecoration);

        mProgressBar.setVisibility(View.VISIBLE);
        //mRecyclerView.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movies_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.menu_top_movies:
                if (Objects.equals(mListType, TOP_MOVIES_TAG)) {
                    Toast.makeText(getContext(), "You are looking at the Top Movies list.", Toast.LENGTH_SHORT).show();
                    break;
                }
                mListType = TOP_MOVIES_TAG;
                break;
            case R.id.menu_pop_movies:
                if (Objects.equals(mListType, POP_MOVIES_TAG)) {
                    Toast.makeText(getContext(), "You are looking at the Popular Movies list.", Toast.LENGTH_SHORT).show();
                    break;
                }
                mListType = POP_MOVIES_TAG;
                break;
            case R.id.menu_fav_movies:
                //TODO: Change to Fav when the DB is implemented!
                if (Objects.equals(mListType, FAV_MOVIES_TAG)) {
                    Toast.makeText(getContext(), "You are looking at the Favorite Movies list.", Toast.LENGTH_SHORT).show();
                    break;
                }
                mListType = FAV_MOVIES_TAG;
                break;
        }

        getMovieList(mListType);
        return super.onOptionsItemSelected(item);

    }

    private void loadAdapter(List<Movie> baseMovieList) {

        mAdapter.clearData();
        mAdapter.setMovieList(baseMovieList);

        switch (mListType) {
            case TOP_MOVIES_TAG:
                mActionBar.setTitle("Top Movies");
                break;
            case POP_MOVIES_TAG:
                mActionBar.setTitle("Popular Movies");
                break;
            case FAV_MOVIES_TAG:
                mActionBar.setTitle("Favorite Movies");
                break;
        }

        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

    }

    private void getMovieList(String listType) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call;

        switch (listType) {
            case POP_MOVIES_TAG:
                call = apiService.getPopularMovies(SensitiveInfo.getMoviesApiKey());
                break;
            case TOP_MOVIES_TAG:
                call = apiService.getTopRatedMovies(SensitiveInfo.getMoviesApiKey());
                break;
            case FAV_MOVIES_TAG:
                call = apiService.getTopRatedMovies(SensitiveInfo.getMoviesApiKey());
                break;
            default:
                call = apiService.getPopularMovies(SensitiveInfo.getMoviesApiKey());
                break;
        }

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> movieList = response.body().getResults();
                loadAdapter(movieList);
                //For some reason this method gets executed even when the activity is resumed...
                Log.d(TAG, "(mMovieList) Number of movies received: " + movieList.size());
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

    }

    private void checkOrientation() {
        Display display = ((WindowManager) getContext().getSystemService(WINDOW_SERVICE))
                .getDefaultDisplay();

        int orientation = display.getRotation();

        if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
            gridColumns = LANDSCAPE_GRID_COLUMNS;
            itemDecoration = new GridSpacing(getContext(), R.dimen.item_offset_landscape);
        } else {
            gridColumns = PORTRAIT_GRID_COLUMNS;
            itemDecoration = new GridSpacing(getContext(), R.dimen.item_offset_portrait);
        }
    }

}