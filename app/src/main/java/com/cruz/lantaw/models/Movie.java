package com.cruz.lantaw.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Acer on 22/09/2017.
 */

public class Movie implements Parcelable{

    private String movieImg;
    private String movieId;
    private String movieTitle;
    private String movieInfo;
    private String movieReleaseDate;

    public Movie(String movieImg, String movieId, String movieTitle, String movieInfo) {
        this.movieImg = movieImg;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieInfo = movieInfo;
    }

    protected Movie(Parcel in) {
        movieImg = in.readString();
        movieTitle = in.readString();
    }

    public Movie(String movieImg, String movieId) {
        this.movieImg = movieImg;
        this.movieId = movieId;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public Movie(String movieImg, String movieId, String movieTitle, String movieInfo, String movieReleaseDate) {
        this.movieImg = movieImg;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieInfo = movieInfo;
        this.movieReleaseDate = movieReleaseDate;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(String movieImg, String movieTitle, String movieInfo) {
        this.movieImg = movieImg;
        this.movieTitle = movieTitle;
        this.movieInfo = movieInfo;
    }

    public String getMovieImg() {
        return movieImg;
    }

    public void setMovieImg(String movieImg) {
        this.movieImg = movieImg;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieInfo() {
        return movieInfo;
    }

    public void setMovieInfo(String movieInfo) {
        this.movieInfo = movieInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieImg);
        parcel.writeString(movieTitle);
        parcel.writeString(movieInfo);
        parcel.writeString(movieId);
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}
