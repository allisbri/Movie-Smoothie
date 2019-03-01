package com.mobilepgh.movie_smoothie;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobilepgh.movie_smoothie.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterClickHandler {

    private static final String TAG = "MainActivity";
    private int pageNum = 1;
    private NetworkUtils.SortOrder sortOrder = NetworkUtils.SortOrder.popular;
    private ArrayList<Poster> posters;
    private boolean isLoading = true;
    private int firstVisibleItemNumber,
    visibleItemCount,
    previousTotal,
    totalItemCount = 0;
    private int pageNumber = 1;
    private MovieAdapter movieAdapter;
    //min number to load off screen
    private int viewThreshold = 30;
    private GridLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setReverseLayout(false);
        posters = new ArrayList<>();
        initRecyclerView();
        new MovieDataQuery().execute(NetworkUtils.buildURL(pageNumber, sortOrder));
    }

    private void initRecyclerView(){
        //DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        //itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        RecyclerView recyclerView = findViewById(R.id.rv_posters);
        movieAdapter = new MovieAdapter(posters, this, this);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setLayoutManager(layoutManager);
        setOnScrollListener(recyclerView);
    }

    public void setOnScrollListener(RecyclerView rv) {
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            new MovieDataQuery().execute(NetworkUtils.buildURL(pageNum, sortOrder));
                            isLoading = true;
                        }
                    }
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


    public class MovieDataQuery extends AsyncTask<URL, Void, String> {
        ArrayList<Poster> newPosters = new ArrayList<>();
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            newPosters = NetworkUtils.parseJSONPosterData(s);
            movieAdapter.addPosters(newPosters);

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



