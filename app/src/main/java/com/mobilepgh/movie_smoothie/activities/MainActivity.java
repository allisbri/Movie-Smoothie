package com.mobilepgh.movie_smoothie.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobilepgh.movie_smoothie.entities.Movie;
import com.mobilepgh.movie_smoothie.adapters.MovieAdapter;
import com.mobilepgh.movie_smoothie.R;
import com.mobilepgh.movie_smoothie.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterClickHandler {
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private ProgressBar loadingIndicator;
    private TextView tvError;
    private static final String TAG = "MainActivity";

    private ArrayList<Movie> movies;
    private boolean isLoading = true;
    private int firstVisibleItemNumber, visibleItemCount,
                previousTotal, totalItemCount = 0;
    private int viewThreshold = 30;
    private int pageNum = 1;

    private NetworkUtils.SortOrder sortOrder;
    private MovieAdapter movieAdapter;
    //min number to load off screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            tvError = findViewById(R.id.tv_error);
            layoutManager = new GridLayoutManager(this, 2);
            layoutManager.setReverseLayout(false);
            loadingIndicator = findViewById(R.id.pb_loading_indicator_main);
            movies = new ArrayList<>();
            initMovieURLVars();
            initRecyclerView();
            updateGrid();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItem = item.getItemId();
        resetGrid();
        switch(selectedItem) {
            case R.id.popular_sort:
                sortOrder = NetworkUtils.SortOrder.POPULAR;
                break;
            case R.id.now_playing_sort:
                sortOrder = NetworkUtils.SortOrder.NOW_PLAYING;
                break;
            case R.id.top_rated_sort:
                sortOrder = NetworkUtils.SortOrder.TOP_RATED;
                break;
            case R.id.credits_item:
                openCredits();
                break;

        }
        //Log.d(TAG, "onOptionsItemSelected: " + sortOrder.getSortOrder());
        updateGrid();
        return super.onOptionsItemSelected(item);
    }

    public void showError(){
        String errorText = getString(R.string.connection_error);
        tvError.setText(errorText);
        tvError.setVisibility(View.VISIBLE);
    }

    private void resetGrid(){
        movieAdapter.clearMovies();
        pageNum = 1;
        isLoading = true;
        firstVisibleItemNumber = 0;
                visibleItemCount = 0;
                previousTotal = 0;
                totalItemCount = 0;
    }

    private void updateGrid(){
        URL url = NetworkUtils.buildMovieListURL(pageNum, sortOrder);
        loadingIndicator.setVisibility(View.VISIBLE);
        new MovieDataQuery().execute(url);
    }

    private void initMovieURLVars(){
        pageNum = 1;
        sortOrder = NetworkUtils.SortOrder.POPULAR;
    }

    private void initRecyclerView(){
        //DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        //itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        recyclerView = findViewById(R.id.rv_posters);
        movieAdapter = new MovieAdapter(movies, this, this);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setLayoutManager(layoutManager);
        setOnScrollListener();
    }


    public void setOnScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //total items loaded
                totalItemCount = layoutManager.getItemCount();
                //number of first visible item on screen
                firstVisibleItemNumber = layoutManager.findFirstVisibleItemPosition();
                //total items visible on screen
                visibleItemCount = layoutManager.getChildCount();

                //load more on scroll only if not already loading more
                if (dy > 0) {
                    if (!isLoading) {
                        //load if scroll has reached a point close to or equal to the totalItemCount
                        if ((totalItemCount) <= (viewThreshold + firstVisibleItemNumber + visibleItemCount)) {
                            pageNum++;
                            updateGrid();
                            isLoading = true;
                        }
                    }
                    //test for where update is complete
                    if (isLoading && (totalItemCount > previousTotal)) {
                        isLoading = false;
                        previousTotal = totalItemCount;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(String movieDetails) {
        Context context = this;
        Class detailActivityClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, detailActivityClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, movieDetails);
        startActivity(intentToStartDetailActivity);
    }

    public void openCredits(){
        Context context = this;
        Class creditsActivityClass = CreditsActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, creditsActivityClass);
        startActivity(intentToStartDetailActivity);
    }

    public class MovieDataQuery extends AsyncTask<URL, Void, String> {
        ArrayList<Movie> newMovies = new ArrayList<>();
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null || s.equals("")) {
                loadingIndicator.setVisibility(View.INVISIBLE);
                showError();
            }
            else{
                newMovies = NetworkUtils.parseJSONMoviePosterData(s);
                loadingIndicator.setVisibility(View.INVISIBLE);
                movieAdapter.addMovies(newMovies);
            }
        }

        //starts new thread
        @Override
        protected String doInBackground(URL...urls) {
            String movieData = null;
            URL url = urls[0];
            try {
                movieData = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e){
                e.printStackTrace();
            }
            return movieData;
        }
    }

}



