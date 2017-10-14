package com.cruz.lantaw.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.cruz.lantaw.models.Movie;
import com.qslll.library.fragments.ExpandingFragment;

/**
 * Created by Acer on 22/09/2017.
 */

public class MovieExpandingFragment extends ExpandingFragment {

    static final String ARG_MOVIE = "ARG_MOVIE";
    Movie movie;

    public static MovieExpandingFragment newInstance(Movie movie) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE,movie);
        MovieExpandingFragment fragment = new MovieExpandingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null) {
            movie = args.getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public Fragment getFragmentTop() {
        return PagerTopFragment.newInstance(movie);
    }

    @Override
    public Fragment getFragmentBottom() {
        return PagerBottomFragment.newInstance(movie);
    }
}
