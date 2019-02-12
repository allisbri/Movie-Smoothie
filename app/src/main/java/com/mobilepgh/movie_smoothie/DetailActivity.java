package com.mobilepgh.movie_smoothie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    String mMovieDetails;
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tv = findViewById(R.id.tv_v);
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null){
            if (intentThatStartedThisActivity.hasExtra(intentThatStartedThisActivity.EXTRA_TEXT)){
                String text = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                tv.setText(text);
            }
        }

    }
}
