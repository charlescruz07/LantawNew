package com.cruz.lantaw.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cruz.lantaw.R;
import com.cruz.lantaw.Singleton.AppSingleton;
import com.cruz.lantaw.activities.MovieInfoActivity;
import com.cruz.lantaw.adapters.GridAdapter;
import com.cruz.lantaw.adapters.ReviewAdapter;
import com.cruz.lantaw.models.Movie;
import com.cruz.lantaw.models.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment {

    String ids[];
    String poster_image_thumbnails[];
    private DatabaseReference myRef;
    private FirebaseUser user;
    GridAdapter adapter;
    public static final String TAG = "movies";
    private View rootView;
    private GridView gridView;
    boolean rezlt;

    public SavedFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_saved, container, false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        myRef = database.getReference("saved").child("user");
        user = FirebaseAuth.getInstance().getCurrentUser();

        getSavedMovies();
        gridView = rootView.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!isNetworkAvailable()){
                    Toast.makeText(getContext(), "Network is not enabled!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getActivity(), MovieInfoActivity.class);
                    intent.putExtra("id", ids[i]);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    public void getSavedMovies(){

        myRef.addValueEventListener(new ValueEventListener() {
            public ArrayList<Movie> movie;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                this.movie = new ArrayList<>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    if (childDataSnapshot.getKey().toString().equals(user.getUid().toString())){
                        for (DataSnapshot childDataSnapshotChildre : childDataSnapshot.getChildren()) {
                             Log.v(TAG,""+ childDataSnapshotChildre.getKey().toString() + " duha duha duha");
                            Log.v(TAG, "" + childDataSnapshotChildre.getKey() + ": "+childDataSnapshotChildre.child("movieImg").getValue().toString()+" tulo tulo tulo " );
                            this.movie.add(new Movie(childDataSnapshotChildre.child("movieImg").getValue().toString(), childDataSnapshotChildre.child("movieId").getValue().toString()));
                        }
                    }
                }

                int j = this.movie.size();
                Log.v(TAG,""+ j + " countj");

                poster_image_thumbnails = new String[j];
                ids = new String[j];
                for (int i = 0; i < this.movie.size(); i++) {
                    Log.e(TAG, "onDataChange: " +this.movie.get(i).getMovieId().toString() );
                    poster_image_thumbnails[i] = this.movie.get(i).getMovieImg().toString();
                    ids[i] = this.movie.get(i).getMovieId().toString();
                }
                adapter = new GridAdapter(poster_image_thumbnails,getContext());
                gridView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean checkMOvie(final String id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        myRef = database.getReference("saved").child("user");

        user = FirebaseAuth.getInstance().getCurrentUser();

        myRef.addValueEventListener(new ValueEventListener() {
            public ArrayList<Movie> movie;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                this.movie = new ArrayList<>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    if (childDataSnapshot.getKey().toString().equals(user.getUid().toString())){
                        for (DataSnapshot childDataSnapshotChildre : childDataSnapshot.getChildren()) {
                            if (id.equals(childDataSnapshotChildre.getKey().toString())){
                                rezlt = true;
//                                Log.v(TAG,""+ rezlt + " duha duha duha");
                                return;
//                                Toast.makeText(get, rezlt[0] +"", Toast.LENGTH_SHORT).show();
                            }else {
                                rezlt = false;
//                                Log.v(TAG,""+ rezlt + " duha duha duha");
//                                Toast.makeText(getContext(), rezlt[0] +"", Toast.LENGTH_SHORT).show();
                            }
//                            Log.v(TAG,""+ childDataSnapshotChildre.getKey().toString() + " duha duha duha");
//                            Log.v(TAG, "" + childDataSnapshotChildre.getKey() + ": "+childDataSnapshotChildre.child("movieImg").getValue().toString()+" tulo tulo tulo " );
//                            this.movie.add(new Movie(childDataSnapshotChildre.child("movieImg").getValue().toString(), childDataSnapshotChildre.child("movieId").getValue().toString()));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.v(TAG,""+ rezlt + " duha duha duha");

        return rezlt;
    }

}
