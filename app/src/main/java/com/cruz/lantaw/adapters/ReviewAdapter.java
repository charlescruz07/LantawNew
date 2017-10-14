package com.cruz.lantaw.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cruz.lantaw.R;
import com.cruz.lantaw.models.Review;

import java.util.ArrayList;

/**
 * Created by Acer on 28/09/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Review> reviews;
    private static final int VIEW_REVIEW = 1;
    private static final int VIEW_LOADING = 2;

    public ReviewAdapter(Context context, ArrayList<Review> reviews){
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_REVIEW:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_section,parent,false);
                return new ViewHolder(view);
            case VIEW_LOADING:
//                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
//                return new ViewHolder(view1);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return reviews.get(position) == null ? VIEW_LOADING : VIEW_REVIEW;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            Glide.with(context).load(reviews.get(position).getUserImage()).into(viewHolder.userImage);
            viewHolder.userName.setText(reviews.get(position).getUserName());
            viewHolder.tvTime.setText(reviews.get(position).getTime());
            viewHolder.userComment.setText(reviews.get(position).getUserComment());
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView userName, tvTime, userComment;

        public ViewHolder(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.imageView);
            userName = itemView.findViewById(R.id.userName);
            tvTime = itemView.findViewById(R.id.tvTime);
            userComment = itemView.findViewById(R.id.userComment);
        }
    }
}
