package com.cruz.lantaw.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cruz.lantaw.R;
import com.cruz.lantaw.models.Movie;

/**
 * A simple {@link Fragment} subclass.
 */
public class PagerTopFragment extends Fragment {

    private static final String ARG_MOVIE = "ARG_MOVIE";
    private Movie movie;
    private View rootView;
    private ImageView movieImg;
    private TextView movieTitle;

    public static PagerTopFragment newInstance(Movie movie) {

        Bundle args = new Bundle();
        PagerTopFragment fragment = new PagerTopFragment();
        args.putParcelable(ARG_MOVIE,movie);
        fragment.setArguments(args);
        return fragment;
    }

    public PagerTopFragment() {
        // Required empty public constructor
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
        rootView =  inflater.inflate(R.layout.fragment_pager_top, container, false);

        movieImg = rootView.findViewById(R.id.image);
        movieTitle = rootView.findViewById(R.id.title);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(movie!=null){
            Glide.with(getContext()).load(movie.getMovieImg()).into(movieImg);
            movieTitle.setText(movie.getMovieTitle());
        }
    }
}
