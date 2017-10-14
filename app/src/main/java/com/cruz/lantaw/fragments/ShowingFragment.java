package com.cruz.lantaw.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cruz.lantaw.R;
import com.cruz.lantaw.Singleton.AppSingleton;
import com.cruz.lantaw.activities.MovieInfoActivity;
import com.cruz.lantaw.adapters.MovieViewPagerAdapter;
import com.cruz.lantaw.models.Movie;
import com.qslll.library.ExpandingPagerFactory;
import com.qslll.library.fragments.ExpandingFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowingFragment extends Fragment implements ExpandingFragment.OnExpandingClickListener {

    private View rootView;
    private ViewPager viewPager;
    List<Movie> movies = new ArrayList<>();
//    String movies[];
    String ids[];
    String slugs[];
    String titles[];
    MovieViewPagerAdapter adapter;
    String poster_image_thumbnails[];

    int currentPos;

    public static final String TAG = "movies";


    public ShowingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_showing, container, false);
        viewPager = rootView.findViewById(R.id.viewPager);



        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new MovieViewPagerAdapter(getChildFragmentManager());
        volleyStringRequst("https://api.cinepass.de/v4/movies/?apikey=465NWAaWLP4bkRQrVmArERbwwBuxxIp3");


        ExpandingPagerFactory.setupViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ExpandingFragment expandingFragment = ExpandingPagerFactory.getCurrentFragment(viewPager);
                if(expandingFragment != null && expandingFragment.isOpenend()){
                    expandingFragment.close();
                    currentPos = position;
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "onPageSelected: " +position );
                currentPos = position;
//                Intent intent = new Intent(getActivity(), MovieInfoActivity.class);
//                intent.putExtra("id", ids[position]);
//                startActivity(intent);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private List<Movie> generateMovieList(){
        volleyStringRequst("https://api.cinepass.de/v4/movies/?apikey=465NWAaWLP4bkRQrVmArERbwwBuxxIp3");
//        for(int i=0;i<5;++i){
//            movies.add(new Movie(Integer.toString(R.drawable.movie_1),"Beauty And The Beast","Disney's animated classic takes on a new form, with a widened mythology and an all-star cast. A young prince, imprisoned in the form of a beast, can be freed only by true love. What may be his only opportunity arrives when he meets Belle, the only human girl to ever visit the castle since it was enchanted."));
//            movies.add(new Movie(Integer.toString(R.drawable.movie_2),"Fight Club", "A nameless first person narrator (Edward Norton) attends support groups in attempt to subdue his emotional state and relieve his insomniac state. When he meets Marla (Helena Bonham Carter), another fake attendee of support groups, his life seems to become a little more bearable. However when he associates himself with Tyler (Brad Pitt) he is dragged into an underground fight club and soap making scheme. Together the two men spiral out of control and engage in competitive rivalry for love and power. When the narrator is exposed to the hidden agenda of Tyler's fight club, he must accept the awful truth that Tyler may not be who he says he is."));
//            movies.add(new Movie(Integer.toString(R.drawable.movie_3),"Boyz II Men", "An American nanny is shocked that her new English family's boy is actually a life-sized doll. After she violates a list of strict rules, disturbing events make her believe that the doll is really alive."));
//            movies.add(new Movie(Integer.toString(R.drawable.movie_4),"Contract Fulfillment", "Based on the true story of two young men, David Packouz and Efraim Diveroli, who won a $300 million contract from the Pentagon to arm America's allies in Afghanistan."));
//            movies.add(new Movie(Integer.toString(R.drawable.movie_5),"Titanic","A seventeen-year-old aristocrat falls in love with a kind but poor artist aboard the luxurious, ill-fated R.M.S. Titanic."));
//            movies.add(new Movie(Integer.toString(R.drawable.movie_6),"The Hangover", "Two years after the bachelor party in Las Vegas, Phil, Stu, Alan, and Doug jet to Thailand for Stu's wedding. Stu's plan for a subdued pre-wedding brunch, however, goes seriously awry."));
//            movies.add(new Movie(Integer.toString(R.drawable.movie_7),"Maleficent","A vengeful fairy is driven to curse an infant princess, only to discover that the child may be the one person who can restore peace to their troubled land."));
//            movies.add(new Movie(Integer.toString(R.drawable.movie_8),"The Broken", "In London, the radiologist Gina McVey organizes a surprise birthday party to her father John McVey with her boyfriend Stefan Chambers, her brother Daniel McVey and his girlfriend Kate ..."));
//            movies.add(new Movie(Integer.toString(R.drawable.movie_9),"John Wick 2", "After returning to the criminal underworld to repay a debt, John Wick discovers that a large bounty has been put on his life."));
//        }
        return movies;
    }

    @Override
    public void onExpandingClick(View view) {
        View rootView = view.findViewById(R.id.image);
        movies.get(viewPager.getCurrentItem());
//        Toast.makeText(getActivity(), "Start activity after second click", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), MovieInfoActivity.class);
                intent.putExtra("id", ids[1 ]);
                startActivity(intent);
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public Intent returnId(Intent intent, int pos){
        intent.putExtra("id",ids[pos]);
        return intent;
    }

    public void volleyStringRequst(String url){

        String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";
        ProgressDialog progressDialog = new ProgressDialog(getContext());;
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());



                try {
                    // Parsing json object response
                    // response will be a json object
                    JSONArray obj = response.getJSONArray("movies");
                    ids = new String[obj.length()];
                    slugs = new String[obj.length()];
                    titles = new String[obj.length()];
                    poster_image_thumbnails = new String[obj.length()];

                    for (int i = 0; i < 7; i++) {

                        JSONObject jsonObject = obj.getJSONObject(i);
                        String slug = jsonObject.getString("slug");
                        String title = jsonObject.getString("title");
                        String poster_image_thumbnail = jsonObject.getString("poster_image_thumbnail");
                        poster_image_thumbnail = poster_image_thumbnail.replace("http", "https");
                        String id = jsonObject.getString("id");

                        movies.add(new Movie(poster_image_thumbnail, title, ""));
                        ids[i] = id;
                        slugs[i] = slug;
                        titles[i] = title;
                        poster_image_thumbnails[i] = poster_image_thumbnail;


                    }
                    adapter.addAll(movies);
                    viewPager.setAdapter(adapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        // Adding String request to request queue
        AppSingleton.getInstance(getContext()).addToRequestQueue(jsonObjReq, REQUEST_TAG);
        progressDialog.hide();
    }

}
