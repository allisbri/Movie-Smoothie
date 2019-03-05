package com.mobilepgh.movie_smoothie.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobilepgh.movie_smoothie.entities.Movie;
import com.mobilepgh.movie_smoothie.R;
import com.mobilepgh.movie_smoothie.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    String imageURL;
    TextView tvPlot;
    TextView tvTitle;
    TextView tvReleaseDate;
    TextView tvRating;
    TextView tvError;
    ImageView ivPoster;
    Movie movie;
    int movieId;
    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tvTitle = findViewById(R.id.title_tv);
        tvPlot = findViewById(R.id.plot_tv);
        tvReleaseDate = findViewById(R.id.release_date_tv);
        ivPoster = findViewById(R.id.poster_iv);
        tvRating = findViewById(R.id.rating_tv);
        tvError = findViewById(R.id.tv_error_activity_detail);

        movie = new Movie();
        imageURL = "http://image.tmdb.org/t/p/w185";
        loadingIndicator = findViewById(R.id.pb_loading_indicator_activity_detail);
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null){
            if (intentThatStartedThisActivity.hasExtra(intentThatStartedThisActivity.EXTRA_TEXT)){
                String idString = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                movieId = Integer.parseInt(idString);
                getMovieDetails();
            }
        }

    }

    public void populateViews(){
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        String releaseDate = "Release Date: " + sdf.format(movie.getReleaseDate());
        String plot = "Plot: " + movie.getPlot();
        String title = movie.getTitle();
        String ratingString = String.valueOf(movie.getRating());
        String rating = "Rating: " + ratingString;
        tvPlot.setText(plot);
        tvTitle.setText(title);
        tvReleaseDate.setText(releaseDate);
        tvRating.setText(rating);

        Glide.with(this)
                //.asBitmap()
                .load(imageURL + movie.getPosterPath())
                .into(ivPoster);
    }

    public void getMovieDetails(){
        loadingIndicator.setVisibility(View.VISIBLE);
        URL url = NetworkUtils.buildMovieDetailsURL(movieId);
        new MovieDataQuery().execute(url);
    }

    public class MovieDataQuery extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (movie == null){
                loadingIndicator.setVisibility(View.INVISIBLE);
                String errorText = getString(R.string.connection_error);
                tvError.setText(errorText);
                tvError.setVisibility(View.VISIBLE);
            }
            else{
                loadingIndicator.setVisibility(View.INVISIBLE);
                movie = NetworkUtils.parseJSONMovieDetailsData(s);
                populateViews();
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
