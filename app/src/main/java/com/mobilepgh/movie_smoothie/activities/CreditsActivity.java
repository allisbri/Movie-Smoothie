package com.mobilepgh.movie_smoothie.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobilepgh.movie_smoothie.R;

public class CreditsActivity extends AppCompatActivity {
    TextView tvCredit;
    ImageView ivCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        tvCredit = findViewById(R.id.credit_tv);
        ivCredit = findViewById(R.id.credit_iv);
        tvCredit.setText(getString(R.string.credit_string));
        ivCredit.setImageResource(R.drawable.mdb);
    }
}
