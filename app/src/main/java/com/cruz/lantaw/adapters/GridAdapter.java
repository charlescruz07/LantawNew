package com.cruz.lantaw.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cruz.lantaw.R;

import java.util.ArrayList;

/**
 * Created by Acer on 22/09/2017.
 */

public class GridAdapter extends BaseAdapter {

    private String movies[];
    private Context context;
    private LayoutInflater layoutInflater;

    public GridAdapter(String movies[], Context context) {
        this.movies = movies;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movies.length;
    }

    @Override
    public Object getItem(int i) {
        return movies[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View gridView = view;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        gridView = layoutInflater.inflate(R.layout.grid_layout, null);

        ImageView movieImg = gridView.findViewById(R.id.movieImg);
        Glide.with(context).load(movies[i]).into(movieImg);
        return gridView;
    }
}
