package com.cruz.lantaw.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cruz.lantaw.R;
import com.cruz.lantaw.Singleton.AppSingleton;
import com.cruz.lantaw.adapters.GridAdapter;
import com.cruz.lantaw.fragments.SavedFragment;
import com.cruz.lantaw.fragments.ShowingFragment;
import com.cruz.lantaw.fragments.UpcomingFragment;
import com.cruz.lantaw.models.Movie;
import com.cruz.lantaw.models.Review;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.qslll.library.fragments.ExpandingFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements ExpandingFragment.OnExpandingClickListener,GoogleApiClient.OnConnectionFailedListener  {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frame;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = "movies";

    private DatabaseReference myRef;
    private FirebaseUser user;
    ShowingFragment showingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("showingMovies");

        vsrMovies("https://api.cinepass.de/v4/movies/?apikey=465NWAaWLP4bkRQrVmArERbwwBuxxIp3");

        if(!isNetworkAvailable()){
            Toast.makeText(this, "Network is not enabled!", Toast.LENGTH_SHORT).show();
        }

        findViews();
        initToolbar();
        initBottomNavigation();

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame,new UpcomingFragment())
                .commit();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        frame = (FrameLayout) findViewById(R.id.frame);
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

    }

    private void initBottomNavigation(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_saved:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame,new SavedFragment())
                                .commit();
                        return true;
                    
                    case R.id.action_upcoming:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame,new UpcomingFragment()
                                )
                                .commit();
                        return true;

                    case R.id.action_showing:
                        showingFragment = new ShowingFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame,showingFragment
                                )
                                .commit();
                        return true;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onExpandingClick(View view) {
//        startActivity(new Intent(this, MovieInfoActivity.class));
        Intent intent = new Intent(this, MovieInfoActivity.class);
        intent = showingFragment.returnId(intent,showingFragment.getCurrentPos());
        Log.d("expandingClickCheck",showingFragment.getCurrentPos() + "");
        Log.d("expandingClickCheck",intent.getStringExtra("id"));
        startActivity(intent);

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void storeShowingMovies(){

    }

    public void vsrMovies(String url){

        String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray obj = response.getJSONArray("movies");

                    for (int i = 0; i < 7; i++) {

                        JSONObject jsonObject = obj.getJSONObject(i);
                        String slug = jsonObject.getString("slug");
                        String title = jsonObject.getString("title");
                        String poster_image_thumbnail = jsonObject.getString("poster_image_thumbnail");
                        poster_image_thumbnail = poster_image_thumbnail.replace("http", "https");
                        String id = jsonObject.getString("id");
                        vsrMoviesId("https://api.cinepass.de/v4/movies/"+id+"/?apikey=465NWAaWLP4bkRQrVmArERbwwBuxxIp3");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);

    }

    public void vsrMoviesId(String url){

        String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = response.getJSONObject("movie");
                    JSONObject release_dates = obj.getJSONObject("release_dates");
                    JSONArray PT = release_dates.getJSONArray("SE");
                    JSONObject dateT = PT.getJSONObject(0);
                    String date = dateT.getString("date");


                    String id = obj.getString("id");
                    String poster_image_thumbnail = obj.getString("poster_image_thumbnail");
                    String synopsis = obj.getString("synopsis");
                    String title = obj.getString("original_title");


                    writeNewPost(id, date, poster_image_thumbnail, synopsis, title);
                    Log.e(TAG, "onResponse: " + id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);

    }

    private void writeNewPost(String id, String releaseDate, String picture, String desc, String title) {
        Movie movie = new Movie(picture, id, title, desc, releaseDate);

        myRef.child(id).setValue(movie);
    }
}
