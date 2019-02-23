package com.mobilepgh.movie_smoothie;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

    private ArrayList<Poster> posters = new ArrayList<>();


    private boolean isLoading = true;
    private int pastVisItems,
            visibleItems,
            totalItems,
            previousTotal = 0;

    private int viewThreshold = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        new MovieDataQuery().execute(NetworkUtils.buildURL());
    }

    private void initRecyclerView(){
        //DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        //itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        RecyclerView recyclerView = findViewById(R.id.rv_posters);
        MovieAdapter movieAdapter = new MovieAdapter(posters, MainActivity.this, MainActivity.this);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //recyclerView.addItemDecoration(itemDecorator);


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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "doInBackground: " + s);
            posters = NetworkUtils.parseJSONPosterData(s);
            Log.d(TAG, "test1" + posters.get(0).getPath());
            Log.d(TAG, Integer.toString(posters.get(0).getId()));
            initRecyclerView();
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

    private void doPagination(){

    }
}

//front page grid arrangement of movie posters
    //viewholders/adapter

//get movies by async request

//movie class
    //ratings
    //popularity
    //title
    //plot synopsis
    //user rating
    //release date


