package com.cruz.lantaw.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cruz.lantaw.R;
import com.cruz.lantaw.models.Movie;

/**
 * A simple {@link Fragment} subclass.
 */
public class PagerBottomFragment extends Fragment {

    private static final String ARG_MOVIE = "ARG_MOVIE";
    private Movie movie;
    private View rootView;
    private TextView movieInfoTxt;

    public PagerBottomFragment() {
        // Required empty public constructor
    }

    public static PagerBottomFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        PagerBottomFragment fragment = new PagerBottomFragment();
        args.putParcelable(ARG_MOVIE,movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            movie = args.getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_pager_bottom, container, false);
        movieInfoTxt = rootView.findViewById(R.id.movieInfoTxt);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(movie!=null){
            movieInfoTxt.setText(movie.getMovieInfo());
        }
    }
}
