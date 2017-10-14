package com.cruz.lantaw.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cruz.lantaw.R;
import com.cruz.lantaw.activities.MovieInfoActivity;
import com.cruz.lantaw.adapters.ReviewAdapter;
import com.cruz.lantaw.models.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReviewFragment extends DialogFragment {

    private ArrayList<Review> reviews;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ImageButton btnAdd;
    private EditText etTxt;
    private ProgressBar progressBar;
    private View rootView;
    private int id;
    private String userName;
    private String comment;
    private String time;
    private Review review;

    public static final String TAG = "movies";


    private DatabaseReference myRef;
    private FirebaseUser user;

    public void findViews(){
        reviews = new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        btnAdd = rootView.findViewById(R.id.btnAdd);
        etTxt = rootView.findViewById(R.id.etText);
        progressBar = rootView.findViewById(R.id.progressBar);
    }

    public ReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_review, container, false);
        findViews();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        review = new Review();

        myRef = database.getReference("posts");
        Bundle bundle = this.getArguments();
        id = bundle.getInt("id");

        getReviews();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            String uid = user.getUid();

            Log.e(TAG, "Logged in user: " + name + "\n" + email + "\n" + photoUrl);
        } else {
        }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date currentTime = Calendar.getInstance().getTime();
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (etTxt.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Invalid empty comment", Toast.LENGTH_SHORT).show();

                }else {
                    writeNewPost(user.getPhotoUrl().toString(), user.getDisplayName(), etTxt.getText().toString(), currentTime.toString(), ""+id);
                    Log.e(TAG, "Saved: "+ user.getPhotoUrl().toString() +"\n"+ user.getDisplayName() +"\n"+ etTxt.getText().toString()+"\n"+
                            currentTime.toString() +"\n"+ ""+id);
                    etTxt.setText("");
                }
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("Shit");

        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

    }


    private void writeNewPost(String userImage, String userName, String userComment, String time, String movieName) {
        Review user = new Review(userImage, userName, userComment, time);

        myRef.child("movie").child(movieName).push().setValue(user);
    }

    public void getReviews(){

        myRef.addValueEventListener(new ValueEventListener() {
            public ArrayList<Review> reviews;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                this.reviews = new ArrayList<>();
                progressBar.setVisibility(View.VISIBLE);
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Log.v(TAG,""+ childDataSnapshot.getKey().toString() + " una una una ");

                    for (DataSnapshot childDataSnapshotChildre : childDataSnapshot.getChildren()) {
                        if (childDataSnapshotChildre.getKey().toString().equals(String.valueOf(id))) {
                            for (DataSnapshot childDataSnapshotChildren : childDataSnapshotChildre.getChildren()) {
                                    Log.v(TAG, "naa "+String.valueOf(id)+" na movie");
                                    Log.v(TAG, "" + childDataSnapshotChildren.getKey() + ": " + childDataSnapshotChildren.child("userName").getValue() + " reult"); //displays the key for the node
                                    this.reviews.add(new Review(childDataSnapshotChildren.child("userImage").getValue().toString(),
                                            childDataSnapshotChildren.child("userName").getValue().toString(),
                                            childDataSnapshotChildren.child("userComment").getValue().toString(),
                                            childDataSnapshotChildren.child("time").getValue().toString()));
                                }
                        }
                    }
                }

                adapter = new ReviewAdapter(rootView.getContext(),this.reviews);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
    }

}
