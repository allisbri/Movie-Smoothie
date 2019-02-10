package com.mobilepgh.movie_smoothie;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static final String TAG = "MovieAdapter";

    private ArrayList<String> mPosterUrls = new ArrayList<>();
    private Context mContext;

    public MovieAdapter(ArrayList<String> mPosters, Context mContext) {
        this.mPosterUrls = mPosters;
        this.mContext = mContext;
    }

    //place view holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.poster_grid_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //insert data in viewholder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called.");
        Picasso
                .get()
                .load(mPosterUrls.get(i))
                .into(viewHolder.poster);
    }

    @Override
    public int getItemCount() {
        return mPosterUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView poster;
        LinearLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.iv_item_number);
            parent = itemView.findViewById(R.id.ll_item_parent);

        }

    }
}
