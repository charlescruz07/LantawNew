package com.cruz.lantaw.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.cruz.lantaw.R;
import com.cruz.lantaw.Singleton.AppSingleton;
import com.cruz.lantaw.fragments.ReviewFragment;
import com.cruz.lantaw.fragments.SavedFragment;
import com.cruz.lantaw.models.Movie;
import com.cruz.lantaw.models.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class  MovieInfoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView mTvTitle;
    private TextView mTvCast;
    private RatingBar mRbRatingBar;
    private TextView mTvInfo;
    private TextView mTvsypno;
    private ImageView mImgImage;
    private ImageView mImgPlay;
    private Button mBtnSave;
    private ProgressDialog progressDialog;
    private FloatingActionButton fab;
    private String id;
    private ProgressBar mProgressDialog;


    private DatabaseReference myRef;
    private SavedFragment savedFragment;

    public static final String TAG = "movieinfoactivity";
    private FirebaseUser user;
    private String poster_image_thumbnail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("saved").child("user");
        savedFragment = new SavedFragment();

        if(!isNetworkAvailable()){
            Toast.makeText(this, "Network is not enabled!", Toast.LENGTH_SHORT).show();
        }
        id= getIntent().getStringExtra("id");
        setId(id);

        String id= getIntent().getStringExtra("id");


        setContentView(R.layout.activity_movie_info);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        volleyStringRequst("https://api.cinepass.de/v4/movies/"+id+"/?apikey=465NWAaWLP4bkRQrVmArERbwwBuxxIp3");
        toggleButton(savedFragment.checkMOvie(id));


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Bundle arguments = new Bundle();
                arguments.putInt("id", getId());
                ReviewFragment myDialog = new ReviewFragment();
                myDialog.setArguments(arguments);
                myDialog.show(fragmentManager, "dialog");
            }
        });

        Log.e(TAG, "onCreate: " + id );


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    public void volleyStringRequst(String url){


        this.mTvTitle = (TextView)findViewById(R.id.movieTitle);
        this.mTvCast = (TextView)findViewById(R.id.movieCast);
        this.mRbRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        this.mTvInfo = (TextView)findViewById(R.id.infoTxt);
        this.mTvsypno = (TextView)findViewById(R.id.synopsisTxt);
        this.mImgImage = (ImageView) findViewById(R.id.movieImg);
        this.mImgPlay = (ImageView) findViewById(R.id.playBtn);
        this.mBtnSave = (Button) findViewById(R.id.Save);
        this.mProgressDialog = (ProgressBar) findViewById(R.id.progressBar);
        mProgressDialog.setVisibility(View.VISIBLE);



        String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());



                try {
                    JSONObject obj = response.getJSONObject("movie");
                    String title = obj.getString("original_title");
                    String synopsis = obj.getString("synopsis");
                    String runtime = obj.getString("runtime");
                    poster_image_thumbnail = obj.getString("poster_image_thumbnail");
                    final String id = obj.getString("id");
                    toggleButton(savedFragment.checkMOvie(String.valueOf(id)));

                    mBtnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

//                            toggleButton(String.valueOf(id));
                            toggleButton(savedFragment.checkMOvie(id));
                            if(savedFragment.checkMOvie(String.valueOf(id))){
                                UnsaveMovie(String.valueOf(id));
                                Toast.makeText(MovieInfoActivity.this, "Movie is unsaved!", Toast.LENGTH_SHORT).show();
                            }else {
                                Intent intent = new Intent(MovieInfoActivity.this,CreateOrEditAlert.class);
                                intent.putExtra("thumbnail",poster_image_thumbnail);
                                intent.putExtra("id",String.valueOf(id));
                                startActivity(intent);


//                                saveMovie(poster_image_thumbnail, String.valueOf(id));
//                                Toast.makeText(MovieInfoActivity.this, "Movie Saved!", Toast.LENGTH_SHORT).show();
//                                Toast.makeText(MovieInfoActivity.this, "Go to Saved movies tab to view saved movies", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                    String date;
                    JSONObject release_dates = obj.getJSONObject("release_dates");
                    if (!release_dates.has("SE")){
                        date = "Not yet released in Philippines";
                    }
                    else {

                        JSONArray PT = release_dates.getJSONArray("SE");
                        JSONObject dateT = PT.getJSONObject(0);
                        date = dateT.getString("date");
                    }



                    JSONArray crew = obj.getJSONArray("crew");
                    JSONObject crewT = crew.getJSONObject(0);
                    String director = crewT.getString("name");

                    JSONArray genres = obj.getJSONArray("genres");

                    JSONArray cast = obj.getJSONArray("cast");

                    String url = "";
                    if (obj.isNull("trailers")){
                        Toast.makeText(MovieInfoActivity.this, "No Trailer available", Toast.LENGTH_SHORT).show();
                        url = "";
                        mImgPlay.setEnabled(false);
                    }else {
//                        Toast.makeText(MovieInfoActivity.this, "naa elle", Toast.LENGTH_SHORT).show();
                        JSONArray trailers = obj.getJSONArray("trailers");
                        JSONObject jsonObjectTrailer = trailers.getJSONObject(0);
                        JSONArray trailer_files = jsonObjectTrailer.getJSONArray("trailer_files");
                        JSONObject jsonObjecttrailer_files = trailer_files.getJSONObject(0);

                        url = jsonObjecttrailer_files.getString("url");
                        mImgPlay.setEnabled(true);
                    }


                    final String finalUrl = url;
                    mImgPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl)));
                        }
                    });

                    mTvCast.setText("Starring: ");
                    for (int i = 0; i < 3; i++) {

                        JSONObject jsonObject = cast.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        mTvCast.append(" ("+name+")");
                    }

                    mTvTitle.setText(title);

                    mTvInfo.setText("Runtime: "+runtime+" mins\n"+
                                    "Release Date: "+date+"\n"+"Genre: ");

                    for (int i = 0; i < genres.length(); i++) {

                        JSONObject genresT = genres.getJSONObject(i);
                        String genresr = genresT.getString("name");
                        mTvInfo.append(genresr+"  ");
                    }

                    mTvInfo.append("\n"+"Director: "+director);

                    mTvsypno.setText(synopsis);
                    Glide.with(getApplicationContext()).load(poster_image_thumbnail).into(mImgImage);



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

                try {
                    JSONObject obj = response.getJSONObject("movie");

                    JSONObject ratings = obj.getJSONObject("ratings");
                    JSONObject imdb = ratings.getJSONObject("imdb");

                    int vote_count = imdb.getInt("vote_count");
                    mRbRatingBar.setRating(Float.parseFloat(vote_count+""));
                } catch (JSONException e) {
                    e.printStackTrace();
                    mRbRatingBar.setRating(Float.parseFloat(0+""));
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);
        mProgressDialog.setVisibility(View.GONE);

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public int getId() {
        return Integer.parseInt(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    private void saveMovie(String poster_image_thumbnail, String movieId) {
        Movie movie = new Movie(poster_image_thumbnail, movieId);

        user = FirebaseAuth.getInstance().getCurrentUser();
        myRef.child(user.getUid().toString()).child(movieId).setValue(movie);

    }
    private void UnsaveMovie(String movieId) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        myRef.child(user.getUid().toString()).child(movieId).removeValue();

    }

    public void toggleButton(boolean id){
        Log.e(TAG, "toggleButton: "+ id );
        if (id){
            mBtnSave.setText("UNSAVE");
        }else {
            mBtnSave.setText("SAVE");
        }
    }
}
