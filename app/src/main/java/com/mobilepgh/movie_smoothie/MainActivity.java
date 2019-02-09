package com.mobilepgh.movie_smoothie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private MovieAdapter movieAdapter = new MovieAdapter();
    movieAdapter = new MovieAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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


