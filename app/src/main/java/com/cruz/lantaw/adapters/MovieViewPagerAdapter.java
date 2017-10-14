package com.cruz.lantaw.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.cruz.lantaw.fragments.MovieExpandingFragment;
import com.cruz.lantaw.models.Movie;
import com.qslll.library.ExpandingViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acer on 22/09/2017.
 */

public class MovieViewPagerAdapter extends ExpandingViewPagerAdapter {

    List<Movie> movies;

    public MovieViewPagerAdapter(FragmentManager fm) {
        super(fm);
        movies = new ArrayList<>();
    }

    public void addAll(List<Movie> movies){
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Movie movie = movies.get(position);
        return MovieExpandingFragment.newInstance(movie);
    }

    @Override
    public int getCount() {
        return movies.size();
    }
}
